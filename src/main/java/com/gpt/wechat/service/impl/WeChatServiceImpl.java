package com.gpt.wechat.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gpt.wechat.common.aspect.LogAround;
import com.gpt.wechat.common.constant.Constants;
import com.gpt.wechat.common.enums.ChatSourceEnum;
import com.gpt.wechat.common.enums.WechatMsgTypeEnum;
import com.gpt.wechat.entity.ChatDetailEntity;
import com.gpt.wechat.entity.TokenHistoryEntity;
import com.gpt.wechat.entity.UserEntity;
import com.gpt.wechat.service.ChatDetailService;
import com.gpt.wechat.service.ChatGptService;
import com.gpt.wechat.service.TokenHistoryService;
import com.gpt.wechat.service.UserService;
import com.gpt.wechat.service.WeChatService;
import com.gpt.wechat.service.bo.QueryTokenBO;
import com.gpt.wechat.service.bo.WeChatSendMsgBO;
import com.gpt.wechat.service.bo.WechatXmlBO;
import com.gpt.wechat.wechat.WXBizMsgCrypt;
import com.zx.utils.util.HttpClientUtil;
import com.zx.utils.util.MethodExecuteUtils;
import com.zx.utils.util.XmlUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;

/**
 * @author ZhaoXu
 * @date 2023/4/22 22:24
 */
@Service
@Slf4j
public class WeChatServiceImpl implements WeChatService {
    private static String ACCESS_TOKEN = "init_token";

    @Resource(name = "chatExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private ChatGptService chatGptService;

    @Autowired
    private ChatDetailService chatDetailService;

    @Autowired
    private TokenHistoryService tokenHistoryService;

    @Autowired
    private UserService userService;

    @Value("${we-chat.secret}")
    private String appSecret;

    @Value("${we-chat.app-id}")
    private String appId;

    @Value("${we-chat.token}")
    private String token;

    @Value("${we-chat.encoding-aes-key}")
    private String encodingAesKey;

    @Value("${we-chat.media-id}")
    private String mediaId;

    @Value("${we-chat.template-id}")
    private String templateId;

    @Override
//    @LogAround
    public String getAccessToken() {
        QueryTokenBO queryTokenBO = QueryTokenBO.builder()
                .grantType(Constants.GRANT_TYPE)
                .secret(appSecret)
                .appId(appId)
                .build();
        String jsonString = JSON.toJSONString(queryTokenBO);
        String response = HttpClientUtil.get(null, Constants.ACCESS_TOKEN_URL, JSONObject.parseObject(jsonString));

        ACCESS_TOKEN = JSON.parseObject(response).getString("access_token");

        TokenHistoryEntity tokenHistoryEntity = TokenHistoryEntity.builder()
                .token(ACCESS_TOKEN)
                .valid(Constants.VALID_TRUE)
                .build();
        tokenHistoryService.save(tokenHistoryEntity);

        return ACCESS_TOKEN;
    }

    @Override
    public boolean sendMessage(WeChatSendMsgBO weChatSendMsgBO) {
        String jsonString = JSON.toJSONString(weChatSendMsgBO);
        String response = HttpClientUtil.post(null, Constants.SEND_MESSAGE_URL + ACCESS_TOKEN, JSONObject.parseObject(jsonString));
        JSONObject responseJson = JSONObject.parseObject(response);
        log.info("sendMessageResponse:{}", responseJson);
        String errCode = responseJson.get("errcode").toString();
        if (Constants.ERR_TOKEN_INVALID.equals(errCode) || Constants.ERR_TOKEN_INVALID2.equals(errCode)) {
            ACCESS_TOKEN = getAccessToken();
        }
        return Constants.SEND_SUCCESS_CODE.equals(errCode);
    }

    @Override
    @LogAround
    @Transactional(isolation = Isolation.READ_UNCOMMITTED, rollbackFor = Exception.class)
    public void eventArrived(String signature, String timestamp, String nonce, String echostr,
                             String encryptType, String msgSignature, String openid, String receivedBody) {
        String realXmlString = receivedBody;
        try {
            // 当是加密模式，需要解密，测试账号需要注释掉
            WXBizMsgCrypt pc = new WXBizMsgCrypt(token, encodingAesKey, appId);
//                realXmlString = pc.DecryptMsg(msgSignature, timestamp, nonce, receivedBody);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        UserEntity userByUserid = userService.findUserByUserid(openid);
        if(userByUserid == null){
            UserEntity userEntity = UserEntity.builder()
                    .userId(openid)
                    .valid(Constants.VALID_TRUE)
                    .build();
            userService.save(userEntity);
        }
        WechatXmlBO wechatXmlBO = XmlUtils.xmlToJavaBean(realXmlString, WechatXmlBO.class);
        String msgType = wechatXmlBO.getMsgType();
        String content = wechatXmlBO.getContent();
        String picUrl = wechatXmlBO.getPicUrl();

        ChatDetailEntity chatDetailEntity = ChatDetailEntity.builder()
                .topicId(1L)
                .valid(Constants.VALID_TRUE)
                .msgType(WechatMsgTypeEnum.getCodeByMsgType(msgType))
                .question(WechatMsgTypeEnum.TEXT_MESSAGE.getMsgType().equals(msgType) ? content : picUrl)
                .userId(openid)
                .chatSource(ChatSourceEnum.WE_CHAT_SOURCE.getCode())
                .build();

        chatDetailEntity = chatDetailService.save(chatDetailEntity);

        WeChatSendMsgBO weChatSendMsgBO = WeChatSendMsgBO.builder()
                .msgType(msgType)
                .toUser(openid)
                .build();
        String answer = null;
        if (WechatMsgTypeEnum.TEXT_MESSAGE.getMsgType().equals(msgType)) {
            answer = chatGptService.chatWithGpt(openid, Constants.TOPIC, content);
            weChatSendMsgBO.setText(new WeChatSendMsgBO.Text(answer));
        } else {
            weChatSendMsgBO.setImage(new WeChatSendMsgBO.Image(mediaId));
        }

        // 将消息再转发回去
        ChatDetailEntity finalChatDetailEntity = chatDetailEntity;
        String finalAnswer = answer;
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void beforeCommit(boolean readOnly) {
                finalChatDetailEntity.setAnswer(finalAnswer);
                chatDetailService.save(finalChatDetailEntity);
                // 最多重试三次
                for (int i = 0; i < 3; i++) {
                    if (!sendMessage(weChatSendMsgBO)) {
                        try {
                            Thread.sleep(3000);
                            continue;
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
                }
            }
        });
    }
}

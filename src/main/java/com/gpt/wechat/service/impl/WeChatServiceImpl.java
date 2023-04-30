package com.gpt.wechat.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gpt.wechat.common.aspect.LogAround;
import com.gpt.wechat.common.constant.Constants;
import com.gpt.wechat.common.enums.ChatSourceEnum;
import com.gpt.wechat.common.enums.WechatEventEnum;
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
import com.gpt.wechat.service.bo.WeatherPredictionResponse;
import com.gpt.wechat.service.bo.WeatherTemplateMessageDTO;
import com.gpt.wechat.service.bo.WeatherWarningResponse;
import com.gpt.wechat.service.bo.WechatXmlBO;
import com.gpt.wechat.wechat.WXBizMsgCrypt;
import com.zx.utils.util.HttpClientUtil;
import com.zx.utils.util.ReflectUtil;
import com.zx.utils.util.StringUtil;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ZhaoXu
 * @date 2023/4/22 22:24
 */
@Service
@Slf4j
public class WeChatServiceImpl implements WeChatService {
    private static String ACCESS_TOKEN = "init_token";

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

    @Value("${we-chat.encoding-aes-key:''}")
    private String encodingAesKey;

    @Value("${we-chat.media-id:''}")
    private String mediaId;

    @Value("${we-chat.template.prediction-id}")
    private String predictionTemplateId;

    @Value("${we-chat.template.warning-id}")
    private String warningTemplateId;

    @Override
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
    public String sendMessage(WeChatSendMsgBO weChatSendMsgBO) {
        String jsonString = JSON.toJSONString(weChatSendMsgBO);
        String response = HttpClientUtil.post(null, Constants.SEND_MESSAGE_URL + ACCESS_TOKEN, JSONObject.parseObject(jsonString));
        log.info("sendMessage.response:{}", response);
        JSONObject responseJson = JSON.parseObject(response);
        String errCode = responseJson.get("errcode").toString();
        if (Constants.ERR_TOKEN_INVALID.equals(errCode) || Constants.ERR_TOKEN_INVALID2.equals(errCode)) {
            ACCESS_TOKEN = getAccessToken();
            return "";
        }
        return response;
    }

    @Override
    public String sendWeatherTemplateMessage(WeatherPredictionResponse.DailyWeather dailyWeather,
                                             WeatherWarningResponse.WeatherWarning warning,
                                             UserEntity userEntity) {
        WeatherTemplateMessageDTO.WeatherData weatherData = buildWeatherRequestData(dailyWeather, warning);
        WeatherTemplateMessageDTO weatherTemplateMessageDTO = WeatherTemplateMessageDTO.builder()
                .page("index")
                .templateId(predictionTemplateId)
                .toUser(userEntity.getUserId())
                .data(weatherData)
                .build();

        JSONObject json = (JSONObject) JSONObject.toJSON(weatherTemplateMessageDTO);
        log.info("sendWeatherTemplateMessage.request:{}", json);
        String response = HttpClientUtil.post(null, Constants.SEND_TEMPLATE_REQUEST_URL + ACCESS_TOKEN, json);
        log.info("sendWeatherTemplateMessage.response:{}", response);

        JSONObject responseJson = JSON.parseObject(response);
        String errCode = responseJson.get("errcode").toString();
        if (Constants.ERR_TOKEN_INVALID.equals(errCode) || Constants.ERR_TOKEN_INVALID2.equals(errCode)) {
            ACCESS_TOKEN = getAccessToken();
            sendWeatherTemplateMessage(dailyWeather, warning, userEntity);
        }
        return response;
    }

    /**
     * 构建请求参数
     *
     * @param dailyWeather
     * @param queryWarning
     * @return
     */
    private static WeatherTemplateMessageDTO.WeatherData buildWeatherRequestData(WeatherPredictionResponse.DailyWeather dailyWeather,
                                                                                 WeatherWarningResponse.WeatherWarning queryWarning) {
        WeatherTemplateMessageDTO.WeatherData weatherData = new WeatherTemplateMessageDTO.WeatherData();

        // 对象映射，找不到直接跳过
        try {
            Map<String, Object> fieldsValue = new HashMap<>(16);
            if (dailyWeather != null) {
                fieldsValue.putAll(ReflectUtil.getFieldsValue(dailyWeather));
            }
            if (queryWarning != null) {
                fieldsValue.putAll(ReflectUtil.getFieldsValue(queryWarning));
            }
            for (Map.Entry<String, Object> stringObjectEntry : fieldsValue.entrySet()) {
                try {
                    WeatherTemplateMessageDTO.Item item = new WeatherTemplateMessageDTO.Item();
                    item.setValue(stringObjectEntry.getValue().toString());
                    item.setColor("#173177");
                    item.setBold(false);
                    ReflectUtil.executeMethod(weatherData, "set" + StringUtil.firstCharToUpperCase(stringObjectEntry.getKey()), item);
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            // 忽略
        }
        weatherData.getCity().setBold(true);
        weatherData.getTextDay().setColor("#FF0000");
        weatherData.getTextNight().setColor("#FF0000");
        return weatherData;
    }

    @Override
    @LogAround
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void eventArrived(String signature, String timestamp, String nonce, String echostr,
                             String encryptType, String msgSignature, String openid, String receivedBody) {
        String realXmlString = receivedBody;
        try {
            // 当是加密模式，需要解密，测试账号需要注释掉
            if (StringUtil.isNotEmpty(token) && StringUtil.isNotEmpty(encodingAesKey)) {
                WXBizMsgCrypt pc = new WXBizMsgCrypt(token, encodingAesKey, appId);
                realXmlString = pc.DecryptMsg(msgSignature, timestamp, nonce, receivedBody);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        WechatXmlBO wechatXmlBO = XmlUtils.xmlToJavaBean(realXmlString, WechatXmlBO.class);
        String msgType = wechatXmlBO.getMsgType();
        String content = wechatXmlBO.getContent();
        String picUrl = wechatXmlBO.getPicUrl();
        String longitude = wechatXmlBO.getLongitude();
        String latitude = wechatXmlBO.getLatitude();
        String event = wechatXmlBO.getEvent();

        UserEntity userEntity = userService.findUserByUserid(openid);
        if (userEntity == null) {
            userEntity = UserEntity.builder()
                    .userId(openid)
                    .valid(Constants.VALID_TRUE)
                    .longitude(longitude)
                    .latitude(latitude)
                    .build();
        }

        ChatDetailEntity chatDetailEntity = ChatDetailEntity.builder()
                .topicId(1L)
                .valid(Constants.VALID_TRUE)
                .msgType(WechatMsgTypeEnum.getCodeByMsgType(msgType))
                .question(WechatMsgTypeEnum.TEXT_MESSAGE.getMsgType().equals(msgType) ? content : picUrl)
                .userId(openid)
                .chatSource(ChatSourceEnum.WE_CHAT_SOURCE.getCode())
                .build();

        WeChatSendMsgBO weChatSendMsgBO = WeChatSendMsgBO.builder()
                .msgType(msgType)
                .toUser(openid)
                .build();
        String answer = null;
        // 文本消息
        if (WechatMsgTypeEnum.TEXT_MESSAGE.getMsgType().equals(msgType) && !content.contains(Constants.MESSAGE_NOT_SUPPORT)) {
            answer = chatGptService.chatWithGpt(openid, Constants.TOPIC, content);
            weChatSendMsgBO.setText(new WeChatSendMsgBO.Text(answer));
        } else if (WechatMsgTypeEnum.EVENT_MESSAGE.getMsgType().equals(msgType)) {
            // 事件消息
            if (WechatEventEnum.SUBSCRIBE_EVENT.getMsgType().equals(event)) {
                // 关注
                weChatSendMsgBO.setMsgType(WechatMsgTypeEnum.TEXT_MESSAGE.getMsgType());
                weChatSendMsgBO.setText(new WeChatSendMsgBO.Text("感谢关注，请允许获取地理位置以便订阅天气提醒！"));
            } else if (WechatEventEnum.UNSUBSCRIBE_EVENT.getMsgType().equals(event)) {
                // 取关
                userEntity.setValid(Constants.VALID_FALSE);
                userService.save(userEntity);
                return;
            }
            if (StringUtil.isNotEmpty(longitude) && StringUtil.isNotEmpty(latitude)) {
                userEntity.setLatitude(latitude);
                userEntity.setLongitude(longitude);
                userService.save(userEntity);
                log.info("用户地理位置信息发生变更，userEntity：{}", JSON.toJSONString(userEntity));
                return;
            }
        } else {
            // 其它消息
            weChatSendMsgBO.setMsgType(WechatMsgTypeEnum.IMAGE_MESSAGE.getMsgType());
            weChatSendMsgBO.setImage(new WeChatSendMsgBO.Image(mediaId));
        }

        chatDetailEntity = chatDetailService.save(chatDetailEntity);
        userService.save(userEntity);
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
                    String sendResponse = sendMessage(weChatSendMsgBO);
                    if (StringUtil.isEmpty(sendResponse)) {
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

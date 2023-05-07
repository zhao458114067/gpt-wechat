package com.gpt.wechat.controller;

import com.gpt.wechat.common.aspect.LogAround;
import com.gpt.wechat.common.enums.EventKeyEnum;
import com.gpt.wechat.common.enums.WechatEventEnum;
import com.gpt.wechat.service.bo.CreateMenuBO;
import com.gpt.wechat.service.WeChatService;
import com.gpt.wechat.service.bo.WeChatSendMsgBO;
import com.gpt.wechat.service.bo.WechatXmlBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;

/**
 * @author ZhaoXu
 * @date 2023/4/22 20:56
 */
@RestController
@RequestMapping("/v1/gpt/wechat")
@Slf4j
public class WechatController {
    @Autowired
    private WeChatService weChatService;

    @Resource(name = "chatExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * 检测接口
     *
     * @return
     */
    @GetMapping("/event")
    @LogAround
    public String event(@RequestParam(name = "echostr", required = false) String echostr) {
        return echostr;
    }

    /**
     * 公众平台事件
     * 测试账号申请：https://mp.weixin.qq.com/debug/cgi-bin/sandboxinfo?action=showinfo&t=sandbox/index
     *
     * @param signature    签名，token、timestamp、nonce合成
     * @param timestamp    时间
     * @param nonce        随机数
     * @param echostr      随机串
     * @param encryptType  加密类型，aes
     * @param msgSignature 消息体签名
     * @param openid       用户id
     * @param receivedBody 消息体
     * @return
     */
    @PostMapping("/event")
    @LogAround
    public String event(@RequestParam(value = "signature", required = false) String signature,
                        @RequestParam(value = "timestamp", required = false) String timestamp,
                        @RequestParam(value = "nonce", required = false) String nonce,
                        @RequestParam(name = "echostr", required = false) String echostr,
                        @RequestParam(name = "encrypt_type", required = false) String encryptType,
                        @RequestParam(name = "msg_signature", required = false) String msgSignature,
                        @RequestParam(name = "openid", required = false) String openid,
                        @RequestBody(required = false) String receivedBody) {
        WechatXmlBO wechatXmlBO = weChatService.getWechatXmlBO(timestamp, nonce, msgSignature, receivedBody);
        CompletableFuture.runAsync(() -> {
            weChatService.eventArrived(wechatXmlBO);
        }, threadPoolTaskExecutor);

        if (WechatEventEnum.CLICK_EVENT.getMsgType().equals(wechatXmlBO.getEvent()) && !EventKeyEnum.WEATHER_PREDICTION.getEventKey().equals(wechatXmlBO.getEventKey())) {
            return weChatService.replayImageMessage(wechatXmlBO, timestamp, nonce);
        }
        return "";
    }

    @PostMapping("/message/send")
    public String sendMessage(@RequestBody WeChatSendMsgBO weChatSendMsgBO) {
        return weChatService.sendMessage(weChatSendMsgBO);
    }

    @PostMapping("/menu")
    public String createMenu(@RequestBody CreateMenuBO createMenuBO) {
        return weChatService.createMenu(createMenuBO);
    }
}

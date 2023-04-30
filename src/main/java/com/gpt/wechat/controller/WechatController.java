package com.gpt.wechat.controller;

import com.gpt.wechat.service.WeChatService;
import com.zx.utils.util.MethodExecuteUtils;
import com.zx.utils.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * 检测接口
     *
     * @return
     */
    @GetMapping("/event")
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
    @PostMapping(value = "/event")
    public String event(@RequestParam(value = "signature", required = false) String signature,
                        @RequestParam(value = "timestamp", required = false) String timestamp,
                        @RequestParam(value = "nonce", required = false) String nonce,
                        @RequestParam(name = "echostr", required = false) String echostr,
                        @RequestParam(name = "encrypt_type", required = false) String encryptType,
                        @RequestParam(name = "msg_signature", required = false) String msgSignature,
                        @RequestParam(name = "openid", required = false) String openid,
                        @RequestBody(required = false) String receivedBody) {
        CompletableFuture.runAsync(() -> {
            weChatService.eventArrived(signature, timestamp, nonce, echostr, encryptType, msgSignature, openid, receivedBody);
        });
        return "";
    }
}

package com.gpt.wechat.service;

import com.gpt.wechat.service.bo.WeChatSendMsgBO;

import java.io.IOException;

/**
 * @author ZhaoXu
 * @date 2023/4/22 22:24
 */
public interface WeChatService {
    /**
     * 获取token
     * @return
     * @throws IOException
     */
    String getAccessToken();

    /**
     * 消息事件
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @param encryptType
     * @param msgSignature
     * @param openid
     * @param receivedBody
     * @return
     */
    void eventArrived(String signature, String timestamp, String nonce, String echostr,
                        String encryptType, String msgSignature, String openid, String receivedBody);

    /**
     * 发送消息
     * @param weChatSendMsgBO
     * @return
     */
    boolean sendMessage(WeChatSendMsgBO weChatSendMsgBO);
}

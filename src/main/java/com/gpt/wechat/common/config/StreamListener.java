package com.gpt.wechat.common.config;

import com.plexpt.chatgpt.listener.AbstractStreamListener;
import com.plexpt.chatgpt.util.SseHelper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @author KuiChi
 * @date 2023/5/3 0:29
 */
@Slf4j
@AllArgsConstructor
public class StreamListener extends AbstractStreamListener {

    final SseEmitter sseEmitter;

    @Override
    public void onMsg(String message) {
        boolean contains = this.lastMessage.contains("\n\n");
        if (contains) {
            log.info(lastMessage);
            lastMessage = "";
        }
        SseHelper.send(sseEmitter, message);
    }

    @Override
    public void onError(Throwable throwable, String response) {
        SseHelper.complete(sseEmitter);
    }

}
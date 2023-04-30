package com.gpt.wechat.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author KuiChi
 * @date 2023/4/30 2:48
 */
@Component
@Data
@ConfigurationProperties(prefix = "chat-gpt.api")
public class ChatGptProperties {
    private String[] keys;
}

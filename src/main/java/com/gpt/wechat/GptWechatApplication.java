package com.gpt.wechat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author zhaoxu
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class GptWechatApplication {

    public static void main(String[] args) {
        SpringApplication.run(GptWechatApplication.class, args);
    }
}

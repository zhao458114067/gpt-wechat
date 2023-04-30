package com.gpt.wechat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author zhaoxu
 */
@SpringBootApplication
@EnableJpaAuditing
public class GptWechatApplication {

    public static void main(String[] args) {
        SpringApplication.run(GptWechatApplication.class, args);
    }
}

package com.gpt.wechat;

import com.gpt.wechat.common.constant.Constants;
import com.gpt.wechat.service.ChatGptService;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ChatDemoApplicationTests {

    @Autowired
    private ChatGptService chatGptService;

    @Test
    @Ignore
    public void contextLoads() {
//        chatGptService.chatWithGpt(Constants.TOPIC, "帮我画一张小猫的图片");
    }

}

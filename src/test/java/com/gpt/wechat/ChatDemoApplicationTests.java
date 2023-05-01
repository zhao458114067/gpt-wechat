package com.gpt.wechat;

import com.gpt.wechat.service.ChatGptService;
import com.gpt.wechat.service.UserService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ChatDemoApplicationTests {

    @Autowired
    private ChatGptService chatGptService;

    @Autowired
    private UserService userService;

    @Test
    @Ignore
    public void contextLoads() {
//        chatGptService.chatWithGpt(Constants.TOPIC, "帮我画一张小猫的图片");

//        MethodExecuteUtils.logAround(() -> getXxx("qqq"));
//        retryMonitor.registryRetry((item) -> dexe(""), 1);
    }

    String getXxx(String q) {
        return "xxx";
    }

}

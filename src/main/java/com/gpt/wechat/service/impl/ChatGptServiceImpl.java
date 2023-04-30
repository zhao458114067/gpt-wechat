package com.gpt.wechat.service.impl;

import com.alibaba.fastjson.JSON;
import com.gpt.wechat.common.config.ChatGptProperties;
import com.gpt.wechat.common.constant.Constants;
import com.gpt.wechat.service.ChatDetailService;
import com.gpt.wechat.service.ChatGptService;
import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingRegistry;
import com.knuddels.jtokkit.api.EncodingType;
import com.plexpt.chatgpt.ChatGPT;
import com.plexpt.chatgpt.entity.chat.ChatCompletion;
import com.plexpt.chatgpt.entity.chat.ChatCompletionResponse;
import com.plexpt.chatgpt.entity.chat.Message;
import com.plexpt.chatgpt.util.Proxys;
import com.zx.utils.util.MethodExecuteUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.Proxy;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.hibernate.validator.internal.util.logging.Messages.MESSAGES;

/**
 * @author ZhaoXu
 * @date 2023/4/22 20:57
 */
@Service
@Slf4j
public class ChatGptServiceImpl implements ChatGptService, ApplicationRunner {
    private static ChatGPT chatGpt;

    private static Encoding ENC;

    @Value("${spring.profiles.active}")
    String activeEnvironment;

    @Value("${chat-gpt.agent.ip:#{ null }}")
    private String agentIp;

    @Value("${chat-gpt.agent.port:#{ null }}")
    private Integer agentPort;

    @Autowired
    private ChatGptProperties chatGptProperties;

    @Autowired
    private ChatDetailService chatDetailService;

    @Override
    synchronized public String chatWithGpt(String userId, String topic, String question) {
        List<Message> messageList = new ArrayList<>();
        Message userMessage = Message.of(question);
        Message systemMessage = Message.ofSystem(topic);
        messageList.add(userMessage);
        messageList.add(systemMessage);

        List<String> chatQuestions = chatDetailService.pageQueryChatDetailUserId(userId, 1, 500);
        int questionToken = 0;
        int canUseTokenCount = Constants.ALL_TOKENS - Constants.MAX_TOKENS;
        // 计算token值
        for (String chatQuestion : chatQuestions) {
            if (StringUtils.isNotEmpty(chatQuestion)) {
                int sum = ENC.countTokens(chatQuestion);
                if (questionToken + sum < canUseTokenCount) {
                    questionToken += sum;
                    Message assistant = new Message();
                    assistant.setRole(Message.Role.ASSISTANT.getValue());
                    assistant.setContent(chatQuestion);
                    messageList.add(assistant);
                } else {
                    break;
                }
            }
        }
        ChatCompletion chatCompletion = ChatCompletion.builder()
                .model(ChatCompletion.Model.GPT_3_5_TURBO.getName())
                .messages(messageList)
                .maxTokens(Constants.MAX_TOKENS)
                .temperature(0.9)
                .build();
        ChatCompletionResponse response = MethodExecuteUtils.logAround(chatCompletion, chatGpt::chatCompletion);
        Message chatGptRes = response.getChoices().get(0).getMessage();
        return chatGptRes.getContent();
    }

    @Override
    public void run(ApplicationArguments args) {
        // 国内需要代理 国外不需要

        String[] keys = chatGptProperties.getKeys();
        chatGpt = ChatGPT.builder()
                .apiKeyList(Arrays.asList(keys))
                .timeout(900)
                .apiHost("https://api.openai.com/")
                .build();

        if (Constants.ENVIRONMENT_DEV.equals(activeEnvironment)) {
            Proxy proxy = Proxys.socks5(agentIp, agentPort);
            chatGpt.setProxy(proxy);
        }
        chatGpt.init();

        EncodingRegistry registry = Encodings.newDefaultEncodingRegistry();
        ENC = registry.getEncoding(EncodingType.CL100K_BASE);
    }
}

package com.gpt.wechat.service.impl;

import com.alibaba.fastjson.JSON;
import com.gpt.wechat.common.config.ChatGptProperties;
import com.gpt.wechat.common.constant.Constants;
import com.gpt.wechat.entity.ChatDetailEntity;
import com.gpt.wechat.entity.ChatTopicEntity;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.net.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
    public String chatWithGpt(String userId, ChatTopicEntity chatTopicEntity, String question) {
        List<Message> messageList = new ArrayList<>();
        List<ChatDetailEntity> chatDetailEntityList = chatDetailService.pageQueryChatDetailUserIdAndTopicId(userId, chatTopicEntity.getId(), 1, 500);
        String systemText = chatTopicEntity.getTopicText();
        int tokens = ENC.countTokens(systemText);
        int canUseTokenCount = Constants.ALL_TOKENS - Constants.MAX_TOKENS;
        // 计算token值
        for (ChatDetailEntity chatDetailEntity : chatDetailEntityList) {
            String chatQuestion = chatDetailEntity.getQuestion();
            String answer = chatDetailEntity.getAnswer();
            if (StringUtils.isAnyEmpty(chatQuestion, answer)) {
                continue;
            }
            // user
            Message userMessage = Message.of(chatQuestion);
            tokens += ENC.countTokens(JSON.toJSONString(userMessage));
            // assistant
            Message assistantMessage = new Message(Message.Role.ASSISTANT.getValue(), answer);
            int sum = ENC.countTokens(JSON.toJSONString(assistantMessage));
            if (tokens + sum < canUseTokenCount) {
                tokens += sum;
            } else {
                log.info("目前tokens:{}, 超限token：{}", tokens, JSON.toJSONString(chatDetailEntity));
                break;
            }
            messageList.add(assistantMessage);
            messageList.add(userMessage);
        }
        messageList.add(Message.ofSystem(systemText));
        Collections.reverse(messageList);
        if (StringUtils.isNotEmpty(question)) {
            messageList.add(Message.of(question));
        } else {
            messageList.add(Message.ofSystem(systemText));
        }
        ChatCompletion chatCompletion = ChatCompletion.builder()
                .model(ChatCompletion.Model.GPT_3_5_TURBO.getName())
                .messages(messageList)
                .maxTokens(Constants.MAX_TOKENS)
                .temperature(0.7)
                .topP(1)
                .n(1)
                .frequencyPenalty(0)
                .presencePenalty(0)
                .build();
        log.info("chatGpt.chatCompletion方法执行,request:{}", JSON.toJSONString(chatCompletion));
        ChatCompletionResponse response = chatGpt.chatCompletion(chatCompletion);
        log.info("chatGpt.chatCompletion方法执行,response:{}", JSON.toJSONString(response));
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

package com.gpt.wechat.service;

/**
 * @author ZhaoXu
 * @date 2023/4/22 20:57
 */
public interface ChatGptService {
    /**
     * 问问题
     * @param userId
     * @param topic
     * @param question
     * @return
     */
    String chatWithGpt(String userId, String topic, String question);
}

package com.gpt.wechat.service;

import com.gpt.wechat.entity.ChatTopicEntity;

/**
 * @author ZhaoXu
 * @date 2023/4/22 20:57
 */
public interface ChatGptService {
    /**
     * 问问题
     * @param userId
     * @param chatTopicEntity
     * @param question
     * @return
     */
    String chatWithGpt(String userId, ChatTopicEntity chatTopicEntity, String question);
}

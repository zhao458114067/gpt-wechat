package com.gpt.wechat.service.impl;

import com.gpt.wechat.entity.ChatDetailEntity;
import com.gpt.wechat.entity.ChatTopicEntity;
import com.gpt.wechat.repository.ChatTopicRepository;
import com.gpt.wechat.service.ChatTopicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author KuiChi
 * @date 2023/5/1 0:40
 */
@Service
@Slf4j
public class ChatTopicServiceImpl implements ChatTopicService {
    @Autowired
    private ChatTopicRepository chatTopicRepository;

    @Override
    public ChatTopicEntity sava(ChatTopicEntity chatTopicEntity) {
        return chatTopicRepository.save(chatTopicEntity);
    }

    @Override
    public List<ChatTopicEntity> findByAttr(String attr, String condition) {
        return chatTopicRepository.findByAttr(attr, condition);
    }
}

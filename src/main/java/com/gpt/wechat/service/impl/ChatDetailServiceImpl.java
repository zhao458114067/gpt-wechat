package com.gpt.wechat.service.impl;

import com.gpt.wechat.common.enums.WechatMsgTypeEnum;
import com.gpt.wechat.entity.ChatDetailEntity;
import com.gpt.wechat.repository.ChatDetailRepository;
import com.gpt.wechat.service.ChatDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author KuiChi
 * @date 2023/4/29 23:09
 */
@Slf4j
@Service
public class ChatDetailServiceImpl implements ChatDetailService {
    @Autowired
    private ChatDetailRepository chatDetailRepository;

    @Override
    public ChatDetailEntity save(ChatDetailEntity chatDetailEntity) {
        return chatDetailRepository.save(chatDetailEntity);
    }

    @Override
    public List<String> pageQueryChatDetailUserId(String userId, Integer page, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<ChatDetailEntity> byPage = chatDetailRepository.pageQueryByUserIdMsgType(userId, WechatMsgTypeEnum.TEXT_MESSAGE.getCode(), pageRequest);
        if (byPage != null) {
            return byPage.stream().map(ChatDetailEntity::getQuestion).distinct().collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}

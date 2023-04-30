package com.gpt.wechat.service;

import com.gpt.wechat.entity.ChatDetailEntity;
import com.gpt.wechat.entity.ChatTopicEntity;

import java.util.List;

/**
 * @author KuiChi
 * @date 2023/5/1 0:40
 */
public interface ChatTopicService {

    /**
     * 保存
     * @param chatTopicEntity
     * @return
     */
    ChatTopicEntity sava(ChatTopicEntity chatTopicEntity);

    /**
     * 通过属性查询
     * @param attr
     * @param condition
     * @return
     */
    List<ChatTopicEntity> findByAttr(String attr, String condition);
}

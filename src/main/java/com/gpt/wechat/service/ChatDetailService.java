package com.gpt.wechat.service;

import com.gpt.wechat.entity.ChatDetailEntity;

import java.util.List;

/**
 * @author KuiChi
 * @date 2023/4/29 23:08
 */
public interface ChatDetailService {
    /**
     * 保存对应数据
     * @param chatDetailEntity
     * @return
     */
    ChatDetailEntity save(ChatDetailEntity chatDetailEntity);

    /**
     * 分页查询历史聊天
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    List<String> pageQueryChatDetailUserId(String userId, Integer page, Integer pageSize);
}

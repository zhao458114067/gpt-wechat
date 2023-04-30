package com.gpt.wechat.service;

import com.gpt.wechat.entity.TokenHistoryEntity;

/**
 * @author KuiChi
 * @date 2023/4/30 0:36
 */
public interface TokenHistoryService {
    /**
     * 保存
     * @param tokenHistoryEntity
     * @return
     */
    TokenHistoryEntity save(TokenHistoryEntity tokenHistoryEntity);
}

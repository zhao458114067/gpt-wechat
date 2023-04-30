package com.gpt.wechat.service.impl;

import com.gpt.wechat.entity.TokenHistoryEntity;
import com.gpt.wechat.repository.TokenHistoryRepository;
import com.gpt.wechat.service.TokenHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author KuiChi
 * @date 2023/4/30 0:37
 */
@Service
@Slf4j
public class TokenHistoryServiceImpl implements TokenHistoryService {
    @Autowired
    private TokenHistoryRepository tokenHistoryRepository;

    @Override
    public TokenHistoryEntity save(TokenHistoryEntity tokenHistoryEntity) {
        return tokenHistoryRepository.save(tokenHistoryEntity);
    }
}

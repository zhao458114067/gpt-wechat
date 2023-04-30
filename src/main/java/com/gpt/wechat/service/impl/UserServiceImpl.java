package com.gpt.wechat.service.impl;

import com.gpt.wechat.entity.UserEntity;
import com.gpt.wechat.repository.UserRepository;
import com.gpt.wechat.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author KuiChi
 * @date 2023/4/29 23:08
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    @Override
    public UserEntity findUserByUserid(String userId) {
        return userRepository.findOneByAttr("userId", userId);
    }
}

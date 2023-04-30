package com.gpt.wechat.service;

import com.gpt.wechat.entity.UserEntity;

import java.util.List;

/**
 * @author KuiChi
 * @date 2023/4/29 23:08
 */
public interface UserService {
    /**
     * 保存用户
     * @param userEntity
     * @return
     */
    UserEntity save(UserEntity userEntity);

    /**
     * 通过userid查询
     * @param userId
     * @return
     */
    UserEntity findUserByUserid(String userId);

    /**
     * 查询所有用户信息
     * @return
     */
    List<UserEntity> queryAllUserInfoList();
}

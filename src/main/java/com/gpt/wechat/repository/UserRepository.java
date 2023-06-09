package com.gpt.wechat.repository;

import com.gpt.wechat.entity.UserEntity;
import com.zx.utils.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author KuiChi
 * @date 2023/4/29 10:45
 */
public interface UserRepository extends BaseRepository<UserEntity, Long> {

    /**
     * 查询所有有效用户
     * @return
     */
    @Query("from UserEntity ue where ue.valid = 1")
    List<UserEntity> findAllByValid();
}

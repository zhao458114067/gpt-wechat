package com.gpt.wechat.repository;

import com.gpt.wechat.entity.ChatDetailEntity;
import com.zx.utils.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author KuiChi
 * @date 2023/4/29 10:45
 */
public interface ChatDetailRepository extends BaseRepository<ChatDetailEntity, Long> {

    /**
     * 通过用户名和消息类型分页
     * @param userId
     * @param topicId
     * @param msgType
     * @param pageable
     * @return
     */
    @Query("from ChatDetailEntity cd where cd.userId = :userId and cd.msgType = :msgType and cd.topicId = :topicId and cd.valid = 1 order by cd.gmtCreate desc")
    Page<ChatDetailEntity> pageQueryChatDetailUserIdAndTopicId(String userId, Long topicId, Integer msgType, Pageable pageable);
}

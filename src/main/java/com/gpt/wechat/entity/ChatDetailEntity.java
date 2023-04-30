package com.gpt.wechat.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @author KuiChi
 * @date 2023/4/29 10:42
 */
@Entity
@Table(name = "chat_detail_info")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ChatDetailEntity {

    /**
     * id
     */
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户openId
     */
    String userId;

    /**
     * @see com.gpt.wechat.common.enums.ChatSourceEnum
     * chat来源
     */
    @Column
    Integer chatSource;

    /**
     * 主题id
     */
    Long topicId;

    /**
     * 回答
     */
    @Column(name = "answer", columnDefinition = "text")
    String answer;

    /**
     * 消息类型
     */
    @Column
    Integer msgType;

    /**
     * 问题
     */
    @Column(name = "question", columnDefinition = "text")
    String question;

    @CreatedDate
    @Column(name = "gmt_create", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date gmtCreate;

    @LastModifiedDate
    @Column(name = "gmt_modified", insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date gmtModified;

    @Column(name = "valid", columnDefinition = "integer")
    private Integer valid;
}

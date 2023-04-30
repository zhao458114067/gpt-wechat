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
@Table(name = "user_info")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class UserEntity {

    /**
     * id
     */
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     *  微信用户id
     */
    @Column(name = "user_id")
    private String userId;

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

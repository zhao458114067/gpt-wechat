package com.gpt.wechat.entity;

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
import java.io.Serializable;
import java.util.Date;

/**
 * @author KuiChi
 * @date 2023/4/30 21:54
 */
@Entity
@Table(name = "user_info")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class WeatherWarningEntity implements Serializable {

    private static final long serialVersionUID = 7920339548887055755L;
    /**
     * id
     */
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 气象预警推送的id
     */
    @Column
    private String warningId;

    /**
     * 推送机构
     */
    @Column
    private String sender;

    /**
     * 城市
     */
    @Column
    private String city;

    /**
     * 发布时间
     */
    @Column
    private String pubTime;

    /**
     * 标题
     */
    @Column
    private String title;
    @Column
    private String typeName;

    /**
     * 详情
     */
    @Column
    private String text;

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

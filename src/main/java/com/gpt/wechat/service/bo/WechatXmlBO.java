package com.gpt.wechat.service.bo;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author KuiChi
 * @date 2023/4/29 22:15
 */
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class WechatXmlBO implements Serializable {
    private static final long serialVersionUID = 6534263487504905664L;

    /**
     * 向用户发送
     */
    @XmlElement(name = "ToUserName")
    private String toUserName;

    /**
     * 从哪个用户来的
     */
    @XmlElement(name = "FromUserName")
    private String fromUserName;

    /**
     * 消息创建时间
     */
    @XmlElement(name = "CreateTime")
    private Long createTime;

    /**
     * 消息类型
     */
    @XmlElement(name = "MsgType")
    private String msgType;

    /**
     * 消息体
     */
    @XmlElement(name = "Content")
    private String content;

    /**
     * 图片地址
     */
    @XmlElement(name = "PicUrl")
    private String picUrl;

    /**
     * 图片id
     */
    @XmlElement(name = "MediaId")
    private String mediaId;

    /**
     * 关注/取关时间
     */
    @XmlElement(name = "Event")
    private String event;

    /**
     * 地理位置经度
     */
    @XmlElement(name = "Longitude")
    private String longitude;

    /**
     * 地理位置纬度
     */
    @XmlElement(name = "Latitude")
    private String latitude;

    /**
     * 地理位置精度
     */
    @XmlElement(name = "Precision")
    private String precision;

    /**
     * 事件KEY值，与自定义菜单接口中KEY值对应
     */
    @XmlElement(name = "EventKey")
    private String eventKey;

    /**
     * 消息id
     */
    @XmlElement(name = "MsgId")
    private String msgId;
}

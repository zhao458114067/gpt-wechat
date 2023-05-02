package com.gpt.wechat.service.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@Builder
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
     * 事件
     */
    @XmlElement(name = "Event")
    private String event;

    /**
     * 事件KEY值，与自定义菜单接口中KEY值对应
     */
    @XmlElement(name = "EventKey")
    private String eventKey;

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
     * 消息id
     */
    @XmlElement(name = "MsgId")
    private String msgId;

    @XmlElement(name = "Image")
    private XmlImage image;

    @XmlRootElement(name = "Image")
    @XmlAccessorType(XmlAccessType.FIELD)
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class XmlImage implements Serializable {
        private static final long serialVersionUID = -5295892258858739320L;

        @XmlElement(name = "MediaId")
        private String mediaId;
    }
}

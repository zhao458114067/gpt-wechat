package com.gpt.wechat.service.bo;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.annotation.JSONBuilder;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author KuiChi
 * @date 2023/4/29 23:29
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class WeChatSendMsgBO implements Serializable {
    private static final long serialVersionUID = -6005696259204539349L;
    /**
     * 向哪一个用户发送的用户id
     */
    @JSONField(name = "touser")
    private String toUser;

    /**
     * 消息类型
     */
    @JSONField(name = "msgtype")
    private String msgType;

    /**
     * 消息体
     */
    private Text text;

    /**
     * 图片内容
     */
    private Image image;

    @Data
    @AllArgsConstructor
    public static class Text implements Serializable {
        private static final long serialVersionUID = -5295892258858739320L;

        private String content;
    }

    @Data
    @AllArgsConstructor
    public static class Image implements Serializable {
        private static final long serialVersionUID = -5295892258858739320L;

        @JSONField(name = "media_id")
        private String mediaId;
    }
}

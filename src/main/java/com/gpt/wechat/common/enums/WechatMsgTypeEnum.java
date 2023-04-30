package com.gpt.wechat.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author KuiChi
 * @date 2023/4/29 23:01
 */
@AllArgsConstructor
@Getter
public enum WechatMsgTypeEnum {
    /**
     * text:回复文本消息，image：回复图片消息，voice：回复语音消息，video：回复视频消息，music：回复音乐消息，news：回复图文消息
     */
    TEXT_MESSAGE(1, "text"),
    IMAGE_MESSAGE(2, "image"),
    VOICE_MESSAGE(3, "voice"),
    VIDEO_MESSAGE(4, "video"),
    MUSIC_MESSAGE(5, "music"),
    NEWS_MESSAGE(6, "news"),

    ;

    final int code;
    final String msgType;

    public static int getCodeByMsgType(String msgType) {
        for (WechatMsgTypeEnum value : WechatMsgTypeEnum.values()) {
            if (value.getMsgType().equals(msgType)) {
                return value.getCode();
            }
        }
        return 0;
    }
}

package com.gpt.wechat.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author KuiChi
 * @date 2023/4/30 1:20
 */

@AllArgsConstructor
@Getter
public enum WechatEventEnum {
    /**
     * 关注
     */
    SUBSCRIBE_EVENT(1, "subscribe"),

    /**
     * 取关
     */
    UNSUBSCRIBE_EVENT(2, "unsubscribe")
    ,

    /**
     * 取关
     */
    CLICK_EVENT(2, "CLICK")
    ;

    final int code;
    final String msgType;
}

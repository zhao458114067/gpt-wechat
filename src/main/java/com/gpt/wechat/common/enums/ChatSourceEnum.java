package com.gpt.wechat.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author KuiChi
 * @date 2023/4/30 1:34
 */
@Getter
@AllArgsConstructor
public enum ChatSourceEnum {
    /**
     * chat来源，微信、qq。。。。
     */
    WE_CHAT_SOURCE(10);

    final int code;
}

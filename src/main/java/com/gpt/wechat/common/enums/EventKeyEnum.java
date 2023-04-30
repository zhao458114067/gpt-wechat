package com.gpt.wechat.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author KuiChi
 * @date 2023/5/1 1:55
 */

@Getter
@AllArgsConstructor
public enum EventKeyEnum {

    WEATHER_PREDICTION(6, "V1006")
    ;

    final int code;
    final String eventKey;
}

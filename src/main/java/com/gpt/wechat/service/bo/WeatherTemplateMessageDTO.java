package com.gpt.wechat.service.bo;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author KuiChi
 * @date 2023/4/30 16:12
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherTemplateMessageDTO implements Serializable {
    private static final long serialVersionUID = -2589940862378642960L;

    @JSONField(name = "touser")
    private String toUser;
    @JSONField(name = "template_id")
    private String templateId;
    private String url;
    private String page;

    @JSONField(name = "topcolor")
    private String topColor;

    private WeatherData data;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WeatherData implements Serializable{
        private static final long serialVersionUID = 1479762696025076638L;
        /**
         * 预报日期
         */
        private Item city;
        private Item fxDate;
        private Item sunrise;
        private Item sunset;
        private Item moonPhase;
        private Item tempMax;
        private Item tempMin;
        private Item textDay;
        private Item textNight;
        private Item windScaleDay;
        private Item precip;

        private Item id;
        private Item title;
        private Item startTime;
        private Item endTime;
        private Item status;
        private Item severity;
        private Item typeName;
        private Item urgency;
        private Item certainty;
        private Item text;
    }

    @Data
    @NoArgsConstructor
    public static class Item {
        private String value;
        private String color;
        private Boolean bold;
    }

}

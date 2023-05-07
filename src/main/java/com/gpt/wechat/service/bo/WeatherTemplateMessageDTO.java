package com.gpt.wechat.service.bo;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.print.attribute.standard.MediaSize;
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
        @JSONField(name = "phrase2")
        private Item city;

        @JSONField(name = "date1")
        private Item fxDate;
        private Item sunrise;
        private Item sunset;
        private Item moonPhase;
        @JSONField(name = "character_string4")
        private Item temp;
        private Item tempMax;
        private Item tempMin;
        private Item textDay;
        private Item textNight;
        @JSONField(name = "short_thing17")
        private Item windScaleDay;

//        @JSONField(name = "thing6")
        private Item precip;

        /**
         * 天气预警
         */
        private Item id;
        @JSONField(name = "keyword3")
        private Item title;

        @JSONField(name = "thing1")
        private Item publishOrg;

        @JSONField(name = "time5")
        private Item startTime;
        private Item endTime;
        private Item status;
        private Item severity;

        @JSONField(name = "thing7")
        private Item warningArea;

        @JSONField(name = "short_thing2")
        private Item typeName;

        @JSONField(name = "thing6")
        private Item weatherTitle;
        private Item urgency;
        private Item certainty;

        @JSONField(name = "remark")
        private Item text;
    }

    @Data
    public static class Item {
        private String value;
        private String color;
        private Boolean bold;
    }

}

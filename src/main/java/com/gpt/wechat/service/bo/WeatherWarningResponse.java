package com.gpt.wechat.service.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author KuiChi
 * @date 2023/4/30 14:51
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherWarningResponse implements Serializable {
    private static final long serialVersionUID = -6088690851293542905L;

    private String code;
    private String updateTime;
    private String fxLink;
    private List<WeatherWarning> warning;
    private WeatherPredictionResponse.Refer refer;

    @Data
    public static class WeatherWarning implements Serializable{
        private static final long serialVersionUID = -3225719949609919074L;
        private String id;
        private String sender;
        private String city;
        private String pubTime;
        private String title;
        private String startTime;
        private String endTime;
        private String status;
        private String level;
        private String severity;
        private String severityColor;
        private String type;
        private String typeName;
        private String urgency;
        private String certainty;
        private String text;
        private String related;
    }
}

package com.gpt.wechat.service.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author KuiChi
 * @date 2023/4/30 14:49
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherPredictionResponse implements Serializable {
    private static final long serialVersionUID = 5321208000403038333L;
    private String code;
    private String updateTime;
    private String fxLink;
    private List<DailyWeather> daily;
    private Refer refer;

    @Data
    public static class Refer implements Serializable{
        private static final long serialVersionUID = -1818623652215387338L;
        private List<String> sources;
        private List<String> license;
    }

    @Data
    public static class DailyWeather implements Serializable{
        private static final long serialVersionUID = 11356131854083735L;
        private String city;
        private String fxDate;
        private String sunrise;
        private String sunset;
        private String moonrise;
        private String moonset;
        private String moonPhase;
        private String moonPhaseIcon;
        private String tempMax;
        private String tempMin;
        private String iconDay;
        private String textDay;
        private String iconNight;
        private String textNight;
        private String wind360Day;
        private String windDirDay;
        private String windScaleDay;
        private String windSpeedDay;
        private String wind360Night;
        private String windDirNight;
        private String windScaleNight;
        private String windSpeedNight;
        private String humidity;
        private String precip;
        private String pressure;
        private String vis;
        private String cloud;
        private String uvIndex;
    }


}


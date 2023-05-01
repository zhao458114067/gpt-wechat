package com.gpt.wechat.service;

import com.gpt.wechat.entity.UserEntity;
import com.gpt.wechat.entity.WeatherWarningEntity;
import com.gpt.wechat.service.bo.WeatherPredictionResponse;
import com.gpt.wechat.service.bo.WeatherWarningResponse;

import java.util.List;

/**
 * @author KuiChi
 * @date 2023/4/30 14:40
 */
public interface WeatherService {
    /**
     * 通过位置查询天气预报
     * @param location
     * @return
     */
    WeatherPredictionResponse.DailyWeather queryWeatherPrediction(String location);

    /**
     * 通过位置查询天气预警
     * @param location
     * @return
     */
    List<WeatherWarningResponse.WeatherWarning> queryWeatherWarning(String location);

    /**
     * 保存
     * @param warningEntity
     * @return
     */
    WeatherWarningEntity save(WeatherWarningEntity warningEntity);

    /**
     * 推送天气模板消息
     * @param isPrediction
     */
    void pushWeatherTemplateMessage(boolean isPrediction);

    /**
     * 推送天气模板消息
     * @param isPrediction
     * @param userId
     * @return
     */
    String pushWeatherTemplateMessageToUser(boolean isPrediction, UserEntity userId);
}

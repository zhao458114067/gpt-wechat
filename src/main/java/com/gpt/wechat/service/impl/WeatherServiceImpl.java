package com.gpt.wechat.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.gpt.wechat.common.aspect.LogAround;
import com.gpt.wechat.common.constant.Constants;
import com.gpt.wechat.entity.UserEntity;
import com.gpt.wechat.entity.WeatherWarningEntity;
import com.gpt.wechat.repository.WeatherWarningRepository;
import com.gpt.wechat.service.UserService;
import com.gpt.wechat.service.WeChatService;
import com.gpt.wechat.service.WeatherService;
import com.gpt.wechat.service.bo.QueryWeatherBO;
import com.gpt.wechat.service.bo.WeatherPredictionResponse;
import com.gpt.wechat.service.bo.WeatherTemplateMessageDTO;
import com.gpt.wechat.service.bo.WeatherWarningResponse;
import com.zx.utils.util.DynamicObject;
import com.zx.utils.util.HttpClientUtil;
import com.zx.utils.util.ListUtil;
import com.zx.utils.util.ReflectUtil;
import com.zx.utils.util.RetryMonitor;
import com.zx.utils.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author KuiChi
 * @date 2023/4/30 14:40
 */
@Service
@Slf4j
public class WeatherServiceImpl implements WeatherService {
    @Value("${console.weather.key:''}")
    private String weatherApiKey;

    @Autowired
    private UserService userService;

    @Autowired
    private WeChatService weChatService;

    @Autowired
    private WeatherWarningRepository weatherWarningRepository;

    @Autowired
    private RetryMonitor retryMonitor;

    @Scheduled(cron = "0 0 8 1/1 * ? ", zone = "Asia/Shanghai")
    public void pushWeatherPrediction() {
        log.info("推送当日天气预报开始！");
        pushWeatherTemplateMessage(Boolean.TRUE);
    }

    @Scheduled(cron = "0 0/30 * * * ? ", zone = "Asia/Shanghai")
//    @Scheduled(cron = "30 1/1 * * * ? ", zone = "Asia/Shanghai")
    public void pushWeatherWarning() {
        log.info("推送天气预警开始！");
        pushWeatherTemplateMessage(Boolean.FALSE);
    }

    @Override
    public void pushWeatherTemplateMessage(boolean isPrediction) {
        if (StringUtil.isNotEmpty(weatherApiKey)) {
            List<UserEntity> userEntities = userService.queryAllUserInfoList();
            for (UserEntity userEntity : userEntities) {
                retryMonitor.registryRetry(() -> pushWeatherTemplateMessageToUser(isPrediction, userEntity));
            }
        }
    }

    @Override
    public String pushWeatherTemplateMessageToUser(boolean isPrediction, UserEntity userEntity) {
        String longitude = userEntity.getLongitude();
        String latitude = userEntity.getLatitude();
        if (StringUtil.isNotEmpty(longitude) && StringUtil.isNotEmpty(latitude)) {
            String location = longitude + "," + latitude;

            JSONObject queryWeatherJson = QueryWeatherBO.builder()
                    .location(location)
                    .key(weatherApiKey)
                    .build()
                    .toJsonObject();

            String response = HttpClientUtil.get(null, Constants.QUERY_CITY_BY_LOCATION, queryWeatherJson);

            JsonNode locationNode = DynamicObject.parseString(response).get("location").get(0);
            String cityName = locationNode.get("adm1").asText();
            cityName += locationNode.get("name").asText();
            if (isPrediction) {
                WeatherPredictionResponse.DailyWeather dailyWeather = queryWeatherPrediction(location);
                dailyWeather.setCity(cityName);
                weChatService.sendWeatherTemplateMessage(dailyWeather, null, userEntity);
            } else {
                List<WeatherWarningResponse.WeatherWarning> weatherWarnings = queryWeatherWarning(location);
                if (ListUtil.isNotEmpty(weatherWarnings)) {
                    WeatherWarningResponse.WeatherWarning weatherWarning = weatherWarnings.get(0);
                    List<WeatherWarningEntity> warningByWarningId = weatherWarningRepository.findByAttr("warningId", weatherWarning.getId());
                    if (ListUtil.isEmpty(warningByWarningId)) {
                        weatherWarning.setCity(cityName);
                        weChatService.sendWeatherTemplateMessage(null, weatherWarning, userEntity);

                        WeatherWarningEntity weatherWarningEntity = WeatherWarningEntity.builder()
                                .pubTime(weatherWarning.getPubTime())
                                .text(weatherWarning.getText())
                                .title(weatherWarning.getTitle())
                                .sender(weatherWarning.getSender())
                                .city(weatherWarning.getCity())
                                .typeName(weatherWarning.getTypeName())
                                .warningId(weatherWarning.getId())
                                .valid(Constants.VALID_TRUE)
                                .build();
                        weatherWarningRepository.save(weatherWarningEntity);
                    }
                }
            }
        } else {
            log.info("用户：{}，无地理信息！", userEntity.getUserId());
        }
        return "success";
    }

    /**
     * 构建请求参数
     *
     * @param dailyWeather
     * @param queryWarning
     * @return
     */
    public static WeatherTemplateMessageDTO.WeatherData buildWeatherRequestData(WeatherPredictionResponse.DailyWeather dailyWeather,
                                                                                WeatherWarningResponse.WeatherWarning queryWarning) {
        WeatherTemplateMessageDTO.WeatherData weatherData = new WeatherTemplateMessageDTO.WeatherData();

        // 对象映射，找不到直接跳过
        try {
            Map<String, Object> fieldsValue = new HashMap<>(16);
            if (dailyWeather != null) {
                fieldsValue.putAll(ReflectUtil.getFieldsValue(dailyWeather));
            }
            if (queryWarning != null) {
                fieldsValue.putAll(ReflectUtil.getFieldsValue(queryWarning));
            }
            for (Map.Entry<String, Object> stringObjectEntry : fieldsValue.entrySet()) {
                try {
                    WeatherTemplateMessageDTO.Item item = new WeatherTemplateMessageDTO.Item();
                    String fieldName = stringObjectEntry.getKey();
                    String value = stringObjectEntry.getValue().toString();
                    if (fieldName.toLowerCase().contains("time") || fieldName.toLowerCase().contains("date")) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mmXXX");
                        LocalDateTime localDateTime = LocalDateTime.parse(value, formatter);
                        value = localDateTime.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm"));
                    }
                    item.setValue(value);
                    item.setColor("#173177");
                    item.setBold(true);
                    ReflectUtil.executeMethod(weatherData, "set" + StringUtil.firstCharToUpperCase(fieldName), item);
                } catch (Exception e) {
                }
            }
            weatherData.getCity().setBold(true);
            weatherData.getTextDay().setColor("#FF0000");
            weatherData.getTextNight().setColor("#FF0000");
        } catch (Exception e) {
            // 忽略
        }

        if (dailyWeather != null) {
            WeatherTemplateMessageDTO.Item tempItem = new WeatherTemplateMessageDTO.Item();
            tempItem.setValue(weatherData.getTempMin().getValue() + "~" + weatherData.getTempMax().getValue() + "℃");
            weatherData.setTemp(tempItem);
            weatherData.getPrecip().setValue("超过 " + weatherData.getPrecip().getValue() + " 毫米");
        } else {
            WeatherTemplateMessageDTO.Item title = weatherData.getTitle();
            String value = title.getValue();
            String[] split = value.split("发布");
            String publishOrg = split[0];
            String publishTitle = split[1];

            WeatherTemplateMessageDTO.Item publishOrgItem = new WeatherTemplateMessageDTO.Item();
            publishOrgItem.setValue(publishOrg);
            publishOrgItem.setBold(true);
            weatherData.setPublishOrg(publishOrgItem);
            WeatherTemplateMessageDTO.Item publishTitleItem = new WeatherTemplateMessageDTO.Item();
            publishTitleItem.setValue(publishTitle);
            weatherData.setWeatherTitle(publishTitleItem);
            WeatherTemplateMessageDTO.Item warningAreaItem = new WeatherTemplateMessageDTO.Item();
            String city = weatherData.getCity().getValue();
            warningAreaItem.setValue(city);
            weatherData.setWarningArea(warningAreaItem);

        }
        return weatherData;
    }

    @Override
    @LogAround
    public WeatherPredictionResponse.DailyWeather queryWeatherPrediction(String location) {
        JSONObject queryWeatherJson = QueryWeatherBO.builder()
                .key(weatherApiKey)
                .location(location)
                .build()
                .toJsonObject();

        String response = HttpClientUtil.get(null, Constants.QUERY_WEATHER_PREDICTION_URL, queryWeatherJson);
        WeatherPredictionResponse weatherPredictionResponse = JSON.parseObject(response, WeatherPredictionResponse.class);
        String code = weatherPredictionResponse.getCode();
        if (Constants.WEATHER_REQUEST_SUCCESS_CODE.equals(code)) {
            if (ListUtil.isNotEmpty(weatherPredictionResponse.getDaily())) {
                return weatherPredictionResponse.getDaily().get(0);

            }
        }
        return null;
    }

    @Override
    public List<WeatherWarningResponse.WeatherWarning> queryWeatherWarning(String location) {
        JSONObject queryWeatherJson = QueryWeatherBO.builder()
                .key(weatherApiKey)
                .location(location)
                .build()
                .toJsonObject();

        String response = HttpClientUtil.get(null, Constants.QUERY_WEATHER_WARNING_URL, queryWeatherJson);
        WeatherWarningResponse weatherPredictionResponse = JSON.parseObject(response, WeatherWarningResponse.class);
        String code = weatherPredictionResponse.getCode();
        if (Constants.WEATHER_REQUEST_SUCCESS_CODE.equals(code)) {
            if (ListUtil.isNotEmpty(weatherPredictionResponse.getWarning())) {
                return weatherPredictionResponse.getWarning();
            }
        }
        return Collections.emptyList();
    }

    @Override
    public WeatherWarningEntity save(WeatherWarningEntity warningEntity) {
        return weatherWarningRepository.save(warningEntity);
    }
}

package com.gpt.wechat.service;

import com.gpt.wechat.entity.UserEntity;
import com.gpt.wechat.service.bo.CreateMenuBO;
import com.gpt.wechat.service.bo.WeChatSendMsgBO;
import com.gpt.wechat.service.bo.WeatherPredictionResponse;
import com.gpt.wechat.service.bo.WeatherWarningResponse;

import java.io.IOException;
import java.util.List;

/**
 * @author ZhaoXu
 * @date 2023/4/22 22:24
 */
public interface WeChatService {
    /**
     * 获取token
     *
     * @return
     * @throws IOException
     */
    String getAccessToken();

    /**
     * 消息事件
     *
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @param encryptType
     * @param msgSignature
     * @param openid
     * @param receivedBody
     * @return
     */
    void eventArrived(String signature, String timestamp, String nonce, String echostr,
                      String encryptType, String msgSignature, String openid, String receivedBody);

    /**
     * 发送消息
     *
     * @param weChatSendMsgBO
     * @return
     */
    String sendMessage(WeChatSendMsgBO weChatSendMsgBO);

    /**
     * 发送模板消息
     *
     * @param dailyWeather
     * @param warning
     * @param userEntity
     * @return
     */
    String sendWeatherTemplateMessage(WeatherPredictionResponse.DailyWeather dailyWeather,
                                      WeatherWarningResponse.WeatherWarning warning,
                                      UserEntity userEntity);

    /**
     * 创建菜单
     * @param createMenuBO
     * @return
     */
    String createMenu(CreateMenuBO createMenuBO);
}

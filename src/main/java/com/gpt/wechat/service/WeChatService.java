package com.gpt.wechat.service;

import com.gpt.wechat.entity.UserEntity;
import com.gpt.wechat.service.bo.CreateMenuBO;
import com.gpt.wechat.service.bo.WeChatSendMsgBO;
import com.gpt.wechat.service.bo.WeatherPredictionResponse;
import com.gpt.wechat.service.bo.WeatherWarningResponse;
import com.gpt.wechat.service.bo.WechatXmlBO;

import java.io.IOException;

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
     * @param wechatXmlBO
     * @return
     */
    void eventArrived(WechatXmlBO wechatXmlBO);

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

    /**
     * 解析为bo
     * @param timestamp
     * @param nonce
     * @param msgSignature
     * @param receivedBody
     * @return
     */
    WechatXmlBO getWechatXmlBO(String timestamp, String nonce, String msgSignature, String receivedBody);

    /**
     * 回复图片
     * @param wechatXmlBO
     * @param msgSignature
     * @param timestamp
     * @param nonce
     * @param receivedBody
     * @return
     */
    String replayImageMessage(WechatXmlBO wechatXmlBO, String msgSignature, String timestamp, String nonce, String receivedBody);
}

package com.gpt.wechat.controller;

import com.gpt.wechat.entity.WeatherWarningEntity;
import com.gpt.wechat.service.WeatherService;
import com.zx.utils.controller.BaseControllerModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author KuiChi
 * @date 2023/4/30 21:57
 */
@RestController
@RequestMapping("/v1/gpt/wechat")
public class WeatherWarningController extends BaseControllerModel<WeatherWarningEntity, WeatherWarningEntity> {
    @Autowired
    private WeatherService weatherService;

    @GetMapping("/pushWeatherTemplate")
    @ApiOperation(value = "推送天气预报/天气预警信息", notes = "必须isPrediction值")
    public void pushWeatherTemplate(@RequestParam(name = "isPrediction") Boolean isPrediction) {
        weatherService.pushWeatherTemplateMessage(isPrediction);
    }
}

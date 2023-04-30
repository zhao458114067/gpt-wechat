package com.gpt.wechat.controller;

import com.gpt.wechat.entity.TokenHistoryEntity;
import com.zx.utils.controller.BaseControllerModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author KuiChi
 * @date 2023/4/30 0:38
 */
@RestController
@RequestMapping("/v1/gpt/wechat")
public class TokenHistoryController extends BaseControllerModel<TokenHistoryEntity, TokenHistoryEntity> {
}

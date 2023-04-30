package com.gpt.wechat.controller;

import com.gpt.wechat.entity.ChatTopicEntity;
import com.zx.utils.controller.BaseControllerModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author KuiChi
 * @date 2023/4/29 11:07
 */
@RestController
@RequestMapping("/v1/gpt/wechat")
public class ChatTopicController extends BaseControllerModel<ChatTopicEntity, ChatTopicEntity> {
}

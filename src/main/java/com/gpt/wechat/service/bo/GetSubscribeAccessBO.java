package com.gpt.wechat.service.bo;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author KuiChi
 * @date 2023/5/7 14:09
 */
@Data
@Builder
public class GetSubscribeAccessBO implements Serializable {
    private static final long serialVersionUID = -2084094817709893147L;

    private String action;

    private String appid;

    private String scene;

    @JSONField(name = "template_id")
    private String templateId;

    @JSONField(name = "redirect_url")
    private String redirectUrl;

    private String reserved;
}

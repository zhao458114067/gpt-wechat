package com.gpt.wechat.service.bo;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author KuiChi
 * @date 2023/4/30 0:25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryTokenBO implements Serializable {
    private static final long serialVersionUID = -5365026350929204985L;

    @JSONField(name = "grant_type")
    private String grantType;

    @JSONField(name = "appid")
    private String appId;

    private String secret;
}

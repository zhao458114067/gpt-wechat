package com.gpt.wechat.service.bo;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author KuiChi
 * @date 2023/4/30 23:52
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateMenuBO implements Serializable{
    private static final long serialVersionUID = -6199292639883371100L;
    private List<Button> button;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Button implements Serializable {
        private static final long serialVersionUID = -2842828464275644033L;
        private String type;
        private String name;
        private String key;
        private String url;
        private String appid;

        @JSONField(name = "pagepath")
        private String pagePath;
        @JSONField(name = "sub_button")
        private List<Button> subButton;
    }
}

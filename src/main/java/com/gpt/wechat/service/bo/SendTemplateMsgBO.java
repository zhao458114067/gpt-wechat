package com.gpt.wechat.service.bo;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author KuiChi
 * @date 2023/4/30 6:41
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendTemplateMsgBO implements Serializable {
    private static final long serialVersionUID = -2680145263035590583L;

    /**
     * 向哪一个用户发送的用户id
     */
    @JSONField(name = "touser")
    private String toUser;

    /**
     * 模板id
     */
    @JSONField(name = "template_id")
    private String templateId;

    /**
     * 颜色
     */
    @JSONField(name = "topcolor")
    private String topColor;

    @JSONField(name = "data")
    private DataBody data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataBody implements Serializable{
        private static final long serialVersionUID = 3361285906490676756L;

        Weather weather;
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Weather implements Serializable{
        private static final long serialVersionUID = 3361285906490676756L;

        String xian;

        String shanghai;
    }
}

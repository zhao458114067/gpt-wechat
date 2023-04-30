package com.gpt.wechat.service.bo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

/**
 * @author KuiChi
 * @date 2023/4/30 15:01
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryWeatherBO implements Serializable {
    private static final long serialVersionUID = -1175983711815922089L;

    /**
     * 地理位置
     */
    String location;

    /**
     * api key
     */
    String key;

    public JSONObject toJsonObject() {
        return (JSONObject) JSON.toJSON(this);
    }
}

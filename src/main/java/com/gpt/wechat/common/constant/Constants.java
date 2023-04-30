package com.gpt.wechat.common.constant;

/**
 * @author ZhaoXu
 * @date 2023/4/22 22:26
 */
public interface Constants {
    String TOPIC = "你叫赵旭，性格调皮爱玩的男孩，假扮他回答问题";
    String GRANT_TYPE = "client_credential";

    /**
     * 在dev环境下
     */
    String ENVIRONMENT_DEV = "dev";

    /**
     * 公众号获取token链接
     */
    String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";

    /**
     * 公众号发送消息链接
     */
    String SEND_MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=";

    /**
     * 公众号创建自定义菜单
     */
    String CREATE_DESIGN_MENU = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

    /**
     * 公众号token失效
     */
    String ERR_TOKEN_INVALID = "40001";

    /**
     * 公众号请求成功
     */
    String SEND_SUCCESS_CODE = "0";

    String ERR_TOKEN_INVALID2 = "42001";

    /**
     * 最大tokens
     */
    Integer ALL_TOKENS = 4090;

    /**
     * 请求tokens
     */
    Integer MAX_TOKENS = 2000;

    /**
     * 数据生效
     */
    Integer VALID_TRUE = 1;

    /**
     * 数据失效
     */
    Integer VALID_FALSE = 0;
}

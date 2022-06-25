package com.liziyuan.hope.client.common.enums;

/**
 * 授权方式
 *
 * @author zqz
 * @date 2022/6/18
 * @since 1.0.0
 */
public enum GrantTypeEnum {
    //授权码模式
    AUTHORIZATION_CODE("authorization_code");

    private String type;

    GrantTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}

package com.wyu.constant;

/**
 * @author novo
 * @since 2023-05-21
 */
public enum BizCodeEnum {

    SUCCESS("0"),

    ;

    private String code;

    BizCodeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}

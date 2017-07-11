package com.dongql.mybatis.tenant.enums;


import com.dongql.mybatis.tenant.enums.base.BaseEntityEnum;

public enum VipLevel implements BaseEntityEnum {

    NORMAL(1, "普通"),
    GOLD(2, "黄金"),
    DIAMOND(3, "钻石");

    private int code;
    private String description;

    VipLevel(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int getIntValue() {
        return this.code;
    }
}

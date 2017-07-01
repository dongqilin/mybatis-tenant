package com.dongql.mybatis.tenant.enums;


import com.dongql.mybatis.tenant.enums.base.BaseEntityEnum;

public enum Status implements BaseEntityEnum {

    VALID(1,"有效"),
    INVALID(0,"无效");

    private int code;
    private String description;

    Status(int code, String description) {
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

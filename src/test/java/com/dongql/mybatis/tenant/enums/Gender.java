package com.dongql.mybatis.tenant.enums;


import com.dongql.mybatis.tenant.enums.base.BaseEntityEnum;

public enum Gender implements BaseEntityEnum {

    MALE(1,"男"),
    FEMALE(2,"女");

    private int code;
    private String description;

    Gender(int code, String description) {
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

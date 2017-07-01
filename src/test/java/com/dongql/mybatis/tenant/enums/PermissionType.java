package com.dongql.mybatis.tenant.enums;


import com.dongql.mybatis.tenant.enums.base.BaseEntityEnum;

public enum PermissionType implements BaseEntityEnum {

    MENU(1,"菜单"),
    FUNCTION(2,"功能点");

    private int code;
    private String description;

    PermissionType(int code, String description) {
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

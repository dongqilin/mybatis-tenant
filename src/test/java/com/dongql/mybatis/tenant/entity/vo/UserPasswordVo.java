package com.dongql.mybatis.tenant.entity.vo;

import com.dongql.mybatis.tenant.entity.User;

/**
 * Created by dongqilin on 2017/7/11.
 */
public class UserPasswordVo extends User {

    private String password;
    private String salt;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public String toString() {
        return super.toString() + " password:" + password;
    }
}

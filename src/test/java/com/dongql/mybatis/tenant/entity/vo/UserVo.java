package com.dongql.mybatis.tenant.entity.vo;

import com.dongql.mybatis.tenant.entity.User;

/**
 * Created by dongqilin on 2017/7/11.
 */
public class UserVo extends User {

    private String vipName;

    public String getVipName() {
        return vipName;
    }

    public void setVipName(String vipName) {
        this.vipName = vipName;
    }

    @Override
    public String toString() {
        return super.toString() + " vip:" + vipName;
    }
}

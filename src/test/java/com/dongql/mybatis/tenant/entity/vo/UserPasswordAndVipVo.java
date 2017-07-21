package com.dongql.mybatis.tenant.entity.vo;

/**
 * Created by dongqilin on 2017/7/11.
 */
public class UserPasswordAndVipVo extends UserPasswordVo {

    private String vipName;

    public String getVipName() {
        return vipName;
    }

    public void setVipName(String vipName) {
        this.vipName = vipName;
    }

    @Override
    public String toString() {
        return super.toString() + " vipName:" + vipName;
    }

}

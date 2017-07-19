package com.dongql.mybatis.tenant.entity;


import com.dongql.mybatis.tenant.annotations.MultiTenant;
import com.dongql.mybatis.tenant.annotations.MultiTenantColumn;
import com.dongql.mybatis.tenant.annotations.MultiTenantType;
import com.dongql.mybatis.tenant.enums.Gender;
import com.dongql.mybatis.tenant.enums.VipLevel;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * 用户表，共享表实现多租户
 * Created by dongql on 10/01/2016.
 */
@Table(schema = "dongql")
@MultiTenant(type = MultiTenantType.COLUMN)
@MultiTenantColumn(value = "tenant")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    private String userName;
    private Gender gender;
    private VipLevel vip;

    @Transient
    private String tenant;

    @Override
    public String toString() {
        return uid + ">" + userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public VipLevel getVip() {
        return vip;
    }

    public void setVip(VipLevel vip) {
        this.vip = vip;
    }
}

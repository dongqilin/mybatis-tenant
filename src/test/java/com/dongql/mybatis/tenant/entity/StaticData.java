package com.dongql.mybatis.tenant.entity;


import com.dongql.mybatis.tenant.annotations.MultiTenant;
import com.dongql.mybatis.tenant.enums.Status;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户权限定义
 * Created by dongql on 10/01/2016.
 */
@Entity
@Table(name = "sys_static_data")
@MultiTenant
public class StaticData implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dataId;

    private String dataType;
    private String dataKey;
    private String dataValue;
    private Status status;
    private Integer order;
    private Date createTime;

    @Override
    public String toString() {
        return dataType + ":" + dataKey + ">" + dataValue;
    }

    public Long getDataId() {
        return dataId;
    }

    public void setDataId(Long dataId) {
        this.dataId = dataId;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDataKey() {
        return dataKey;
    }

    public void setDataKey(String dataKey) {
        this.dataKey = dataKey;
    }

    public String getDataValue() {
        return dataValue;
    }

    public void setDataValue(String dataValue) {
        this.dataValue = dataValue;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}

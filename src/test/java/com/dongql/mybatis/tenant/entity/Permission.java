package com.dongql.mybatis.tenant.entity;

import com.dongql.mybatis.tenant.enums.PermissionType;
import org.apache.ibatis.type.Alias;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 用户权限定义
 * Created by dongql on 10/01/2016.
 */
@Entity
@Table(name = "user_permission")
@Alias("userPermission")
public class Permission implements Serializable {

    private Long permissionId;
    private String permissionName;
    private String permissionCode;
    private String url;
    private PermissionType permissionType;

    @Override
    public String toString() {
        return permissionId + ":" + permissionName + ">" + permissionCode;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public PermissionType getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(PermissionType permissionType) {
        this.permissionType = permissionType;
    }
}

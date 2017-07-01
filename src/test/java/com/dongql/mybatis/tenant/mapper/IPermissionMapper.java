package com.dongql.mybatis.tenant.mapper;

import com.dongql.mybatis.tenant.entity.Permission;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 权限管理
 * Created by dongql on 10/01/2016.
 */
public interface IPermissionMapper {

    @Select("SELECT * FROM user_permission WHERE permission_id = #{permissionId}")
    Permission getPermission(@Param("permissionId") Long id);

    @Select("SELECT * FROM user_permission")
    List<Permission> getAllPermissions();

    @Insert("insert into user_permission value(#{permissionId}, #{permissionName}, #{permissionCode}, #{url}, #{permissionType})")
    int addPermission(Permission permission);

    List<Permission> getPermissions(@Param("permissions") List<Long> permissions);

    @Delete("delete from user_permission WHERE permission_id = #{permissionId}")
    void deletePermission(@Param("permissionId") Long id);

}

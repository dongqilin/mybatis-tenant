package com.dongql.mybatis.tenant.mapper;

import com.dongql.mybatis.tenant.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * Created by dongql on 10/01/2016.
 */
public interface IUserMapper {

    User getUser(@Param("userId") Long uid);

    @Select("SELECT * FROM user")
    List<User> getUsers();

    List<User> getUserByMap(Map<String, Object> user);

    List<User> getUserByName(@Param("userName") String userName);

    List<User> getUserByBean(@Param("user") User user);

    int insertUser(@Param("user") User user);

    int updateUser(@Param("user") User user);

    void deleteUser(@Param("userId") Long uid);
}

package com.dongql.mybatis.tenant.mapper;

import com.dongql.mybatis.tenant.entity.StaticData;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by dongql on 10/01/2016.
 */
public interface IStaticDataMapper {

    @Select("SELECT * FROM sys_static_data WHERE data_type = #{type} and data_key = #{key}")
    StaticData getData(@Param("type") String type, @Param("key") String key);

    @Select("SELECT * FROM sys_static_data WHERE data_type = #{type}")
    List<StaticData> getDataList(@Param("type") String type);

}

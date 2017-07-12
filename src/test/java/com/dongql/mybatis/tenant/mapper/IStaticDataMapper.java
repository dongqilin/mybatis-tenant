package com.dongql.mybatis.tenant.mapper;

import com.dongql.mybatis.tenant.entity.StaticData;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * Created by dongql on 10/01/2016.
 */
public interface IStaticDataMapper extends Mapper<StaticData> {

    @Select("SELECT * FROM sys_static_data WHERE data_key = #{key} and data_code = #{code}")
    StaticData getData(@Param("key") String key, @Param("code") String code);

    @Select("SELECT * FROM sys_static_data WHERE data_key = #{key}")
    List<StaticData> getDataList(@Param("key") String key);

}

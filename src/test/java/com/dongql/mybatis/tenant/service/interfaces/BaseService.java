package com.dongql.mybatis.tenant.service.interfaces;

import com.dongql.mybatis.tenant.Pager;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

public interface BaseService<T> {

    T get(Object o);

    int save(T o);

    int saveSelective(T o);

    int update(T o);

    List<T> getAllList();

    List<T> getByIds(Class<T> object, String property, List<?> ids);

    T selectOne(T t);

    int selectCount(T t);

    int selectCountByExample(Example example);

    Pager<T> getListPage(Pager<T> pager, Example example);

    Pager<T> getListPage(Pager<T> pager, T o);

    void deleteByPrimaryKey(Object id);

    void deleteByExample(Example example);

    void delete(T t);

    int updateByExampleSelective(T t, Example example);

    int updateByExample(T t, Example example);

    T selectByPrimaryKey(Object id);

    int updateByPrimaryKeySelective(T t);

    List<T> selectByExample(Example t);

}

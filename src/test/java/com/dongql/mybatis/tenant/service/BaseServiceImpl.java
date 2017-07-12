package com.dongql.mybatis.tenant.service;

import com.dongql.mybatis.tenant.Pager;
import com.dongql.mybatis.tenant.service.interfaces.BaseService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public abstract class BaseServiceImpl<T> implements BaseService<T> {

    @Autowired
    private Mapper<T> mapper;

    public Mapper<T> getMapper() {
        return mapper;
    }

    public void setMapper(Mapper<T> mapper) {
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public T get(Object o) {
        return mapper.selectByPrimaryKey(o);
    }

    @Transactional
    public int save(T o) {
        return mapper.insert(o);
    }

    @Transactional
    public int saveSelective(T o) {
        return mapper.insertSelective(o);
    }

    @Transactional
    public int update(T o) {
        return mapper.updateByPrimaryKey(o);
    }

    @Override
    public int updateByPrimaryKeySelective(T t) {
        return mapper.updateByPrimaryKeySelective(t);
    }

    @Override
    @Transactional
    public int updateByExampleSelective(T t, Example example) {
        return mapper.updateByExampleSelective(t, example);
    }

    @Override
    @Transactional
    public int updateByExample(T t, Example example) {
        return mapper.updateByExample(t, example);
    }

    @Transactional
    public List<T> getAllList() {
        return mapper.selectAll();
    }

    @Transactional
    public List<T> getByIds(Class<T> object, String property, List<?> ids) {
        Example example = new Example(object);
        example.createCriteria().andIn(property, ids);
        return mapper.selectByExample(example);
    }

    @Override
    public T selectByPrimaryKey(Object id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public T selectOne(T t) {
        return mapper.selectOne(t);
    }

    @Override
    public int selectCount(T t) {
        return mapper.selectCount(t);
    }

    @Override
    public int selectCountByExample(Example example) {
        return mapper.selectCountByExample(example);
    }

    @Override
    public List<T> selectByExample(Example t) {
        return mapper.selectByExample(t);
    }


    @Override
    public Pager<T> getListPage(Pager<T> pager, final T o) {
        return getListPage(pager, new ListCallBack<T>() {
            @Override
            public List<T> getList() {
                return mapper.select(o);
            }
        });
    }

    @Override
    public Pager<T> getListPage(Pager<T> pager, final Example example) {
        return getListPage(pager, new ListCallBack<T>() {
            @Override
            public List<T> getList() {
                return mapper.selectByExample(example);
            }
        });
    }

    @Transactional
    @Override
    public void delete(T t) {
        mapper.delete(t);
    }

    @Transactional
    @Override
    public void deleteByExample(Example example) {
        mapper.deleteByExample(example);
    }

    @Transactional
    @Override
    public void deleteByPrimaryKey(Object id) {
        mapper.deleteByPrimaryKey(id);
    }

    private Pager<T> getListPage(Pager<T> pager, ListCallBack<T> callback) {
        if (pager == null) {
            pager = new Pager<>();
        }
        PageHelper.startPage(pager.getOffset(), pager.getLimit());
        if (pager.getSort() != null && !pager.getSort().isEmpty()) {
            PageHelper.orderBy(pager.getSort() + " " + pager.getOrder());
        }
        List<T> list = callback.getList();
        PageInfo<T> pageInfo = new PageInfo<T>(list);
        pager.setTotal(pageInfo.getTotal());
        pager.setRows(list);
        return pager;
    }
}

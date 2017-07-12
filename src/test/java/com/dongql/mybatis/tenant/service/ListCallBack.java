package com.dongql.mybatis.tenant.service;

import java.util.List;

public abstract class ListCallBack<T> {
    public abstract List<T> getList();
}

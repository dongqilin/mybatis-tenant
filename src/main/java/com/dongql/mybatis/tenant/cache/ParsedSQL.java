package com.dongql.mybatis.tenant.cache;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongqilin on 02/07/2017.
 */
public class ParsedSQL<T> {

    private String sql;
    private List<ParsedParam<T>> params;

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getSql() {
        return sql;
    }

    public List<ParsedParam<T>> getParams() {
        return params;
    }

    public void addParam(ParsedParam<T> param) {
        if (params == null) {
            params = new ArrayList<>();
        }
        this.params.add(param);
    }
}

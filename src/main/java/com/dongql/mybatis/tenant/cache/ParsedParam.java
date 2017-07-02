package com.dongql.mybatis.tenant.cache;

/**
 * Created by dongqilin on 02/07/2017.
 */
public class ParsedParam<T> {

    private String param;
    private T value;
    private Class<T> javaType;

    public ParsedParam(String param, T value, Class<T> javaType) {
        this.param = param;
        this.value = value;
        this.javaType = javaType;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Class<T> getJavaType() {
        return javaType;
    }

    public void setJavaType(Class<T> javaType) {
        this.javaType = javaType;
    }
}

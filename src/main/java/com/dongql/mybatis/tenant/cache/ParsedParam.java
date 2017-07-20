package com.dongql.mybatis.tenant.cache;

/**
 * Created by dongqilin on 02/07/2017.
 */
public class ParsedParam<T> {

    private String param;
    private T value;
    private Class<T> javaType;
    private int position;

    /**
     * when insert, add = false
     */
    private boolean add = true;

    public ParsedParam(String param, T value, Class<T> javaType, int position) {
        this(param, value, javaType, position, true);
    }

    public ParsedParam(String param, T value, Class<T> javaType, int position, boolean add) {
        this.param = param;
        this.value = value;
        this.javaType = javaType;
        this.position = position;
        this.add = add;
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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isAdd() {
        return add;
    }

    public void setAdd(boolean add) {
        this.add = add;
    }
}

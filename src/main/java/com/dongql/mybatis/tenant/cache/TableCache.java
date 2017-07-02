package com.dongql.mybatis.tenant.cache;

import com.dongql.mybatis.tenant.annotations.MultiTenantType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 所有Entity解析后的缓存对象
 * Created by dongqilin on 01/07/2017.
 */
public class TableCache {

    private static final Map<String, TableCache> CACHE = new ConcurrentHashMap<>(100);

    private String name;
    private MultiTenantType type;
    private String column;

    private TableCache(String name, MultiTenantType type, String column) {
        this.name = name;
        this.type = type;
        this.column = column;
        CACHE.put(name, this);
    }

    public static TableCache newSchemaCache(String name) {
        return new TableCache(name, MultiTenantType.SCHEMA, null);
    }

    public static TableCache newTableCache(String name) {
        return new TableCache(name, MultiTenantType.TABLE, null);
    }

    public static TableCache newColumnCache(String name, String column) {
        return new TableCache(name, MultiTenantType.COLUMN, column);
    }

    public static TableCache newDatabaseCache(String name) {
        return new TableCache(name, MultiTenantType.DATABASE, null);
    }

    public static TableCache none(String name) {
        return new TableCache(name, MultiTenantType.NONE, null);
    }

    public static TableCache get(String name) {
        return CACHE.getOrDefault(name, null);
    }

    public String getName() {
        return name;
    }

    public MultiTenantType getType() {
        return type;
    }

    public String getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return name + ":" + type.name() + (column == null ? "" : "[" + column + "]");
    }

}

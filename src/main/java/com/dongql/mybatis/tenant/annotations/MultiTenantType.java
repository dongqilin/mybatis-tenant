package com.dongql.mybatis.tenant.annotations;

/**
 * 租户数据隔离策略
 * Created by dongqilin on 2017/6/28.
 */
public enum MultiTenantType {

    /**
     * 不是多租户表，仅用作数据校验
     */
    NONE,

    /**
     * 通过数据表列来隔离租户数据，需要与{@link MultiTenantColumn}配合使用
     */
    COLUMN,

    /**
     * 各租户数据在同一schema的不同table中
     */
    TABLE,

    /**
     * 各租户数据在同一数据库实例的不同schema中
     */
    SCHEMA,

    /**
     * 各租户数据在不同数据库实例中（暂不支持）
     */
    DATABASE


}

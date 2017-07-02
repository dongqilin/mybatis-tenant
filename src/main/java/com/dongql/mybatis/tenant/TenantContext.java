package com.dongql.mybatis.tenant;

/**
 * Mybatis - 多租户拦截器
 * Created by dongqilin on 01/07/2017.
 */
public class TenantContext {

    private static ThreadLocal<String> tenant = new ThreadLocal<>();

    public static void set(String tenantId) {
        tenant.set(tenantId);
    }

    public static String get() {
        return tenant.get();
    }

}

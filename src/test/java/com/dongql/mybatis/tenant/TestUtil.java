package com.dongql.mybatis.tenant;

import com.dongql.mybatis.tenant.cache.TableCache;

/**
 * Created by dongqilin on 2017/7/11.
 */
public class TestUtil {


    public static void init() {

        MultiTenantInterceptor.tenantKey = "tenant";
        MultiTenantInterceptor.schemaPrefix = "tenant_";

        initCache();
        initTenant();
    }

    private static void initCache() {
        TableCache.newColumnCache("dongql","user", "tenant");
        TableCache.newColumnCache("dongql","user_password", "tenant_id");
        TableCache.newSchemaCache("sys_static_data");
    }

    private static void initTenant() {
        TenantContext.set("dongql");
    }

}

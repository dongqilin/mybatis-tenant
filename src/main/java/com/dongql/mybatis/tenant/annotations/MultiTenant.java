package com.dongql.mybatis.tenant.annotations;

import java.lang.annotation.*;

/**
 * 多租房数据表主注解，通过ThreadLocal来实现
 * Created by dongqilin on 2017/6/28.
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MultiTenant {

    String defaultContextProperty = "tenant_id";

    MultiTenantType type() default MultiTenantType.SCHEMA;

    String contextProperty() default defaultContextProperty;

}

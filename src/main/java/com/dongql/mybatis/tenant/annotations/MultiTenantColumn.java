package com.dongql.mybatis.tenant.annotations;

import java.lang.annotation.*;

import static com.dongql.mybatis.tenant.annotations.MultiTenant.defaultContextProperty;

/**
 * 当 MultiTenantType = {@code MultiTenantType.COLUMN}时，需指定租户ID所在列
 * Created by dongqilin on 2017/6/28.
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MultiTenantColumn {

    String value() default defaultContextProperty;

    String contextProperty() default defaultContextProperty;

}

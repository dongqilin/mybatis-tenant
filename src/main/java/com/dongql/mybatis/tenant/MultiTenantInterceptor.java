package com.dongql.mybatis.tenant;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.Properties;

/**
 * Mybatis - 多租户拦截器
 * Created by dongqilin on 01/07/2017.
 */
@Intercepts({
//        @Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}),
//        @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
public class MultiTenantInterceptor implements Interceptor {

    private static transient Logger logger = LoggerFactory.getLogger(MultiTenantInterceptor.class);

    Properties props;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        logger.debug("before...");
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);

        String sql = (String) metaStatementHandler.getValue("delegate.boundSql.sql");


        Object result = invocation.proceed();
        logger.debug("after...");
        return result;
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            logger.debug("parse all entity from annotation: @MultiTenant");

        }
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        if (properties.contains("dbType")) {
            logger.debug(properties.get("dbType").toString());
        }
        this.props = properties;
    }

}

package com.dongql.mybatis.tenant;

import com.dongql.mybatis.tenant.annotations.MultiTenant;
import com.dongql.mybatis.tenant.annotations.MultiTenantColumn;
import com.dongql.mybatis.tenant.annotations.MultiTenantType;
import com.dongql.mybatis.tenant.cache.ParsedParam;
import com.dongql.mybatis.tenant.cache.ParsedSQL;
import com.dongql.mybatis.tenant.cache.TableCache;
import com.dongql.mybatis.tenant.exception.TenantAnnotationException;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.lang.annotation.Annotation;
import java.sql.Connection;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static com.dongql.mybatis.tenant.SQLParserUtil.tableName;

/**
 * Mybatis - 多租户拦截器
 * Created by dongqilin on 01/07/2017.
 */
@Intercepts({
//        @Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}),
//        @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
@SuppressWarnings("unchecked")
public class MultiTenantInterceptor implements Interceptor {

    private static transient Logger logger = LoggerFactory.getLogger(MultiTenantInterceptor.class);

    public static String tenantKey = null;

    private static AtomicBoolean initialized = new AtomicBoolean(false);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);

        Configuration configuration = (Configuration) metaStatementHandler.getValue("delegate.configuration");
        List<ParameterMapping> parameterMappings = (List<ParameterMapping>) metaStatementHandler.getValue("delegate.boundSql.parameterMappings");
        Map<String, Object> additionalParameters = (Map<String, Object>) metaStatementHandler.getValue("delegate.boundSql.additionalParameters");

        String sql = (String) metaStatementHandler.getValue("delegate.boundSql.sql");
        logger.debug("before tenant interceptor: " + sql);
        ParsedSQL<String> result = SQLParserUtil.parse(sql);

        List<ParsedParam<String>> params = result.getParams();
        for(ParsedParam<String> p : params){
            ParameterMapping mapping = new ParameterMapping.Builder(configuration, p.getParam(), p.getJavaType()).build();
            parameterMappings.add(mapping);
            additionalParameters.put(p.getParam(), p.getValue());
        }

        metaStatementHandler.setValue("delegate.boundSql.sql", result.getSql());
//        logger.debug("after tenant interceptor: " + result.getSql());

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {

        if (target instanceof Executor && !initialized.get()) {
            initialized.compareAndSet(false, true);
            logger.debug("parse all entity from annotation: @MultiTenant");
            MetaObject metaExecutor = SystemMetaObject.forObject(target);
            HashMap<String, Class> typeAliases = (HashMap<String, Class>) metaExecutor.getValue("delegate.configuration.typeAliasRegistry.TYPE_ALIASES");

            // 找到系统中所有注解了@Entity的类
            Set<Class> entities = typeAliases.values().stream().filter(v -> {
                for (Annotation an : v.getAnnotations()) {
                    if (an instanceof Entity) return true;
                }
                return false;
            }).collect(Collectors.toSet());

            // 进一步分析多租户注解，缓存起来备用
            entities.forEach(entity -> {

                String table = null;
                MultiTenantType type = null;
//                String contextProperty = null;
                String column = null;

                Annotation[] annotations = entity.getAnnotations();
                for (Annotation an : annotations) {
                    if (an instanceof Table) {
                        Table e = (Table) an;
                        table = e.name();
                    } else if (an instanceof MultiTenant) {
                        MultiTenant tenant = (MultiTenant) an;
                        type = tenant.type();
//                        contextProperty = tenant.contextProperty();
                    } else if (an instanceof MultiTenantColumn) {
                        MultiTenantColumn tenant = (MultiTenantColumn) an;
                        column = tenant.value();
                    }
                }

                if (table == null || table.isEmpty()) {
                    table = tableName(entity.getSimpleName());
                }

                if (type == null) {
                    TableCache.none(table);
                } else {
                    switch (type) {
                        case COLUMN:
                            if (column == null)
                                throw new TenantAnnotationException(entity.getName() + " annotated by @MultiTenant[COLUMN] without @MultiTenantColumn");
                            TableCache.newColumnCache(table, column);
                            break;
                        case TABLE:
                            TableCache.newTableCache(table);
                            break;
                        case SCHEMA:
                            TableCache.newSchemaCache(table);
                            break;
                        case DATABASE:
                            TableCache.newDatabaseCache(table);
                            break;
                    }
                }
            });
        }
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        if (properties.containsKey("tenantKey")) {
            tenantKey = properties.get("tenantKey").toString();
        }
    }

}

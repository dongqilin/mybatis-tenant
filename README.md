# MyBatis Tenant plugin

### Usage
1. 配置插件
```XML
<plugins>
    <plugin interceptor="com.dongql.mybatis.tenant.MultiTenantInterceptor">
        <property name="tenantKey" value="tenant_id"/>
        <property name="schemaPrefix" value="tenant_"/>
    </plugin>
</plugins>
```

2. 启用@MultiTenant/@MultiTenantColumn
```java
@Entity
@MultiTenant(type = MultiTenantType.COLUMN)
@MultiTenantColumn(value = "tenant")
public class User implements Serializable {
    
    // 标识字段不持久化，插件处理
    @Transient
    private String tenant;

}
```



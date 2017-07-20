# MyBatis Tenant plugin

### Usage
1. 配置插件
```XML
<plugins>
    <plugin interceptor="com.dongql.mybatis.tenant.MultiTenantInterceptor">
        <property name="tenantStart" value="false"/>
        <property name="schemaPrefix" value="tenant_"/>
    </plugin>
</plugins>
```
* tenantStart  
    为空或false时，需要在服务器启动之后调用
    > TenantContext.start()

    启动拦截。
* schemaPrefix  
    如果租户名为lgl，则对应schema为tenant_lgl。
    
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



package com.dongql.mybatis.tenant;

import com.dongql.mybatis.tenant.cache.ParsedSQL;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by dongqilin on 02/07/2017.
 */
public class UtilTest {

    @Before
    public void init() {
        TenantContext.set("leo");
    }

    @Test
    public void tableName() {
        String name = SQLParserUtil.tableName("TableNameWithALongTitle");
        Assert.assertEquals("table_name_with_a_long_title", name);
    }

    @Test
    public void select() {
        String sql = "select * from user where uid = ?";
        ParsedSQL<String> result = SQLParserUtil.parse(sql);
        System.out.println(result.getSql());
    }

}

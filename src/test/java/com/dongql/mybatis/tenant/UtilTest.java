package com.dongql.mybatis.tenant;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.regex.Pattern;

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
        System.out.println(Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        String sql = "select * from user where uid = ?";
//        ParsedSQL<String> result = SQLParserUtil.parse(sql);
//        System.out.println(result.getSql());
    }

}

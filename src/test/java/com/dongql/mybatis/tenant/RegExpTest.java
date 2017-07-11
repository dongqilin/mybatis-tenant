package com.dongql.mybatis.tenant;

import com.dongql.mybatis.tenant.cache.ParsedSQL;
import org.apache.ibatis.mapping.SqlCommandType;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.regex.Matcher;

import static com.dongql.mybatis.tenant.parser.SelectParser.select;

/**
 * Created by dongqilin on 02/07/2017.
 */
public class RegExpTest {

    @BeforeClass
    public static void before(){
        TestUtil.init();
    }

    @Test
    public void appendReplacement() {
        String sql = "select * from user where uid = ?";
        String tenant = "dongql";
        StringBuffer buffer = new StringBuffer();

        Matcher matcher = select.matcher(sql);
        while (matcher.find()) {
            String group = matcher.group();
            String name = matcher.group(1);
            String after = group.replaceFirst(name, tenant + "." + name);
            matcher.appendReplacement(buffer, after);
        }
        matcher.appendTail(buffer);
        System.out.println(buffer.toString());
    }

    @Test
    public void join(){
//        String sql = "SELECT u.*, d.data_value vipName FROM user u join sys_static_data d on d.data_key='vip_level' and d.data_key=u.vip WHERE uid = ?";
//        ParsedSQL<String> result = SQLParserUtil.parse("sss", SqlCommandType.SELECT, sql);
//        System.out.println(result.getSql());

        String sql = "SELECT u.*, p.password, p.salt FROM user u join user_password p d on u.uid = p.uid WHERE uid = ?";
        ParsedSQL<String> result = SQLParserUtil.parse("sss", SqlCommandType.SELECT, sql);
        System.out.println(result.getSql());
    }

}

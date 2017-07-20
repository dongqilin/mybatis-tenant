package com.dongql.mybatis.tenant;

import com.dongql.mybatis.tenant.cache.ParsedSQL;
import com.dongql.mybatis.tenant.parser.JoinParser;
import com.dongql.mybatis.tenant.parser.SelectParser;
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
    public static void before() {
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
    public void select() {
        String sql;
        Matcher matcher;
//        sql = "SELECT create_time,update_time,room_id,name,floor_id,build_id  FROM basic_room";
//        matcher = SelectParser.select.matcher(sql);
//        if (matcher.find()) {
//            String group = matcher.group();
//            System.out.println(matcher.group(1));
//        }
//        sql = "        r.room_id as basicRoomId,\n" +
//                "        r.name as room_name\n" +
//                "        from\n" +
//                "        basic_build as b\n" +
//                "        left join basic_floor as f on b.build_id=f.build_id\n" +
//                "        left join basic_room as r on f.floor_id=r.floor_id";
        sql = "SELECT bf.*\n" +
                "        FROM basic_floor bf\n" +
                "        RIGHT JOIN basic_room br ON br.`floor_id` = bf.`floor_id`\n" +
                "         WHERE  bf.`build_id` = ? \n" +
                "        GROUP BY bf.`name`";
        matcher = SelectParser.select.matcher(sql);
        if (matcher.find()) {
            String group = matcher.group();
            System.out.println(matcher.group(1));
            System.out.println(matcher.group(3));
        }
        matcher = JoinParser.join.matcher(sql);
        while (matcher.find()) {
            String group = matcher.group();
            System.out.println(matcher.group(2));
            System.out.println(matcher.group(4));
        }
    }

    @Test
    public void join() {
//        String sql = "SELECT u.*, d.data_value vipName FROM user u join sys_static_data d on d.data_key='vip_level' and d.data_key=u.vip WHERE uid = ?";
//        ParsedSQL<String> result = SQLParserUtil.parse("sss", SqlCommandType.SELECT, sql);
//        System.out.println(result.getSql());

        String sql = "SELECT u.*, p.password, p.salt FROM user u join user_password p d on u.uid = p.uid WHERE uid = ?";
        ParsedSQL<String> result = SQLParserUtil.parse("sss", SqlCommandType.SELECT, sql);
        System.out.println(result.getSql());
    }

}

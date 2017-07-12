package com.dongql.mybatis.tenant;

import com.dongql.mybatis.tenant.cache.ParsedSQL;
import com.dongql.mybatis.tenant.parser.DeleteParser;
import com.dongql.mybatis.tenant.parser.InsertParser;
import com.dongql.mybatis.tenant.parser.JoinParser;
import com.dongql.mybatis.tenant.parser.SelectParser;
import com.dongql.mybatis.tenant.parser.UpdateParser;
import org.apache.ibatis.mapping.SqlCommandType;

import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 解析SQL，分析所有表，根据表的租户类型重新生成SQL
 * Created by dongqilin on 01/07/2017.
 */
public class SQLParserUtil {

    private SQLParserUtil() {
    }

    /**
     * 把驼峰形式转化为下划线连接形式，如SysStaticDate -> sys_static_data
     *
     * @param name 驼峰形式
     * @return 下划线连接形式
     */
    public static String tableName(String name) {
        Matcher matcher = Pattern.compile("([A-Z][a-z]*)").matcher(name);
        StringJoiner table = new StringJoiner("_");
        while (matcher.find()) {
            table.add(matcher.group().toLowerCase());
        }
        return table.toString();
    }

    /**
     * SQL解析入口，根据类型进行分发
     *
     * @param id   mapper的唯一ID
     * @param type SQL语句类型
     * @param sql  SQL
     * @return 存在租户配置返回解析之后的SQL与参数，否则返回NULL
     */
    public static ParsedSQL<String> parse(String id, SqlCommandType type, String sql) {
        switch (type) {
            case INSERT:
                return new InsertParser(sql).parse();
            case UPDATE:
                return new UpdateParser(sql).parse();
            case DELETE:
                return new DeleteParser(sql).parse();
            case SELECT:
                ParsedSQL<String> result = new SelectParser(sql).parse();
                if (result == null) return null;
                ParsedSQL<String> joinResult = new JoinParser(result).parse();
                return joinResult == null ? result : joinResult;
            case UNKNOWN:
                break;
            case FLUSH:
                break;
        }
        return null;
    }
}

package com.dongql.mybatis.tenant;

import com.dongql.mybatis.tenant.annotations.MultiTenantType;
import com.dongql.mybatis.tenant.cache.ParsedParam;
import com.dongql.mybatis.tenant.cache.ParsedSQL;
import com.dongql.mybatis.tenant.cache.TableCache;

import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

/**
 * 解析SQL，分析所有表，根据表的租户类型重新生成SQL
 * Created by dongqilin on 01/07/2017.
 */
public class SQLParserUtil {

    private SQLParserUtil() {
    }

    private static String tableNamePattern = "([a-zA-Z0-9_]*)";
    private static Pattern select = Pattern.compile(" from " + tableNamePattern + " " + tableNamePattern + "[, ]+", CASE_INSENSITIVE);
    private static Pattern join = Pattern.compile(" (left|right|outer)+ join " + tableNamePattern + " ", CASE_INSENSITIVE);
    private static Pattern insertInto = Pattern.compile("^insert into " + tableNamePattern + " ", CASE_INSENSITIVE);
    private static Pattern update = Pattern.compile("^update " + tableNamePattern + " ", CASE_INSENSITIVE);
    private static Pattern deleteFrom = Pattern.compile("^delete from " + tableNamePattern + " ", CASE_INSENSITIVE);

    public static ParsedSQL<String> parse(String sql) {
        ParsedSQL<String> parsedSQL = new ParsedSQL<>();
        StringBuilder result = new StringBuilder(sql);
        StringJoiner table = new StringJoiner(",");
        String tenant = TenantContext.get();
        Matcher matcher = select.matcher(sql);
        StringBuffer buffer = new StringBuffer();
        int start = 0, end;
        while (matcher.find()) {
            String group = matcher.group();
            String name = matcher.group(1);
            String alias = matcher.group(2);
            TableCache cache = TableCache.get(name);
            MultiTenantType type = cache.getType();
            int i = matcher.start();
            end = matcher.end();

            switch (type) {
                case NONE:
                    break;
                case COLUMN:
                    String temp = sql.toLowerCase().contains("where") ? " and " : " where ";
                    boolean ifNoAlias = alias == null || alias.isEmpty() || alias.equalsIgnoreCase("where");
                    String column = cache.getColumn();
                    ParsedParam<String> param = new ParsedParam<>(column, tenant, String.class);
                    parsedSQL.addParam(param);
                    result.append(temp)
                            .append(ifNoAlias ? name : alias).append(".")
                            .append(column).append(" = ?");
                    break;
                case TABLE:
                    break;
                case SCHEMA:
                    buffer.append(sql.substring(start, i));
                    matcher.appendReplacement(buffer, tenant + "." + name);
                    break;
                case DATABASE:
                    break;
            }
            start = end;
            table.add(name);
        }
        parsedSQL.setSql(result.toString());
        return parsedSQL;
    }

    /**
     * 把驼峰形式转化为下划线连接形式，如UserPermission -> user_permission
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


}

package com.dongql.mybatis.tenant.parser;

import com.dongql.mybatis.tenant.TenantContext;
import com.dongql.mybatis.tenant.annotations.MultiTenantType;
import com.dongql.mybatis.tenant.cache.ParsedParam;
import com.dongql.mybatis.tenant.cache.ParsedSQL;
import com.dongql.mybatis.tenant.cache.TableCache;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.dongql.mybatis.tenant.MultiTenantInterceptor.getSchemaPrefix;

/**
 * 对SQL语句进行分析并租户化
 * 1.
 * Created by dongqilin on 2017/7/11.
 */
public abstract class BaseParser {

    static final int mask = Pattern.CASE_INSENSITIVE | Pattern.MULTILINE;
    static final String TABLE_NAME = "[\n ]*([a-zA-Z0-9_]*)";
    static final Pattern suffix = Pattern.compile(" (group by|order by|limit)", mask);

    String tenant = TenantContext.get();

    ParsedSQL<String> parsedSQL;
    String sql;
    Pattern pattern;

    StringBuffer result = null;

    public abstract ParsedSQL<String> parse();

    ParsedSQL<String> parse(int nameIndex, int aliasIndex) {
        if (TenantContext.isStarted()) {
            return tenant(nameIndex, aliasIndex);
        } else {
            /* 系统初始化过程中不启动多租户模式，仅启动schema改写 */
            return schema(nameIndex);
        }
    }

    /**
     * transform SQL with schema prefix, tenant prefix or sub clause
     *
     * @param nameIndex  table name position
     * @param aliasIndex table name alias position
     * @return parsed SQL
     */
    private ParsedSQL<String> tenant(int nameIndex, int aliasIndex) {
        Matcher matcher = pattern.matcher(sql);
        while (matcher.find()) {

            String name = matcher.group(nameIndex);
            TableCache cache = TableCache.get(name);
            MultiTenantType type = cache.getType();

            String group = matcher.group();
            String alias = aliasIndex < 1 ? null : matcher.group(aliasIndex);

            switch (type) {
                case NONE:
                    break;
                case COLUMN:
                    parseColumn(nameIndex, name, alias);
                    break;
                case TABLE:
                    break;
                case SCHEMA:
                    parseSchema(matcher, group, name);
                    break;
                case DATABASE:
                    break;
            }
        }

        if (result == null) {
            parsedSQL.setSql(this.sql);
        } else {
            parsedSQL.setSql(result.toString());
        }
        return parsedSQL;
    }

    /**
     * transform SQL with schema prefix only
     *
     * @param nameIndex table name position
     * @return parsed SQL
     */
    public ParsedSQL<String> schema(int nameIndex) {
        Matcher matcher = pattern.matcher(sql);
        StringBuffer tmp = new StringBuffer();
        while (matcher.find()) {
            String valuesKeyword = matcher.group();
            String name = matcher.group(nameIndex);
            valuesKeyword = valuesKeyword.replace(name, TableCache.get(name).getSchema() + name);
            matcher.appendReplacement(tmp, valuesKeyword);
        }
        matcher.appendTail(tmp);
        parsedSQL.setSql(tmp.toString());
        return parsedSQL;
    }

    /**
     * When {@link MultiTenantType#SCHEMA} set, transform table name with tenant schema prefix.
     *
     * @param matcher
     * @param group
     * @param name
     */
    public void parseSchema(Matcher matcher, String group, String name) {
        if (result == null) result = new StringBuffer();
        String after = group.replaceFirst(name, getSchemaPrefix() + tenant + "." + name);
        matcher.appendReplacement(result, after);
        matcher.appendTail(result);
    }

    /**
     * When {@link MultiTenantType#COLUMN} set, transform where clause with tenant_column condition.
     *
     * @param nameIndex table name position
     * @param name      table name
     * @param alias     table name alias
     * @see BaseParser#schema
     */
    public void parseColumn(int nameIndex, String name, String alias) {
        TableCache cache = TableCache.get(name);
        String column = cache.getColumn();

        ParsedSQL<String> schema = schema(nameIndex);

        if (result == null) result = new StringBuffer(schema.getSql());

        String temp = sql.toLowerCase().contains("where") ? " and " : " where ";
        boolean ifNoAlias = alias == null || alias.isEmpty() || alias.equalsIgnoreCase("where");
        StringBuilder tenantClause = new StringBuilder().append(temp)
                .append(ifNoAlias ? name : alias).append(".")
                .append(column).append(" = ?");
        Matcher matcher = suffix.matcher(sql);
        int position = -1;
        if (matcher.find()) {
            int start = matcher.start();
            position = position(sql, start);
            result.insert(start, tenantClause.toString());
        } else {
            result.append(tenantClause.toString());
        }
        parsedSQL.addParam(new ParsedParam<>(column, tenant, String.class, position));
    }

    /**
     * calculate the prepared parameter position
     *
     * @param sql   SQL
     * @param start match position of pattern
     * @return position of a parameter
     */
    private int position(String sql, int start) {
        String substring = sql.substring(start);
        return substring.length() - substring.replaceAll("\\?", "").length();
    }

}

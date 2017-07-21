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

    public static final int mask = Pattern.CASE_INSENSITIVE | Pattern.MULTILINE;
    static final String TABLE_NAME = "[\n ]*([a-zA-Z0-9_]*)";
    private static final Pattern suffix = Pattern.compile(" (group by|order by|limit)", mask);

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
            String newSql = schema(sql, nameIndex);
            parsedSQL.setSql(newSql);
            return parsedSQL;
        }
    }

    /**
     * transform SQL with schema prefix only
     *
     * @param nameIndex table name position
     * @return parsed SQL
     */
    public String schema(String sql, int nameIndex) {
        Matcher matcher = pattern.matcher(sql);
        StringBuffer tmp = new StringBuffer();
        while (matcher.find()) {
            String valuesKeyword = matcher.group();
            String name = matcher.group(nameIndex);
            TableCache cache = TableCache.get(name);
            if (cache != null)
                valuesKeyword = valuesKeyword.replace(name, cache.getSchema() + name);
            matcher.appendReplacement(tmp, valuesKeyword);
        }
        matcher.appendTail(tmp);
        return tmp.toString();
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
        if (matcher.find()) {
            MultiTenantType type = getMultiTenantType(nameIndex, matcher);
            if (type == MultiTenantType.COLUMN) {
                result = new StringBuffer(sql);
                parseColumn(matcher, nameIndex, aliasIndex);
            }
        }
        if (result != null) {
            sql = result.toString();
            result = null;
        }

        matcher = pattern.matcher(sql);
        if (matcher.find()) {
                result = new StringBuffer();
                parseSchema(matcher, nameIndex);
        }
        if (result != null) sql = result.toString();

        sql = schema(sql, nameIndex);

        parsedSQL.setSql(this.sql);

        return parsedSQL;
    }

    private MultiTenantType getMultiTenantType(int nameIndex, Matcher matcher) {
        String name = matcher.group(nameIndex);
        TableCache cache = TableCache.get(name);
        return cache.getType();
    }

    /**
     * When {@link MultiTenantType#SCHEMA} set, transform table name with tenant schema prefix.
     *
     * @param matcher   regexp matcher
     * @param nameIndex table name position
     */
    public void parseSchema(Matcher matcher, int nameIndex) {
        matchSchema(matcher, nameIndex);
        while (matcher.find()) {
            matchSchema(matcher, nameIndex);
        }
        matcher.appendTail(result);
    }

    private void matchSchema(Matcher matcher, int nameIndex) {
        String group = matcher.group();
        String name = matcher.group(nameIndex);
        MultiTenantType type = getMultiTenantType(nameIndex, matcher);
        if(type == MultiTenantType.SCHEMA){
            String after = group.replaceFirst(name, getSchemaPrefix() + tenant + "." + name);
            matcher.appendReplacement(result, after);
        }
    }

    /**
     * When {@link MultiTenantType#COLUMN} set, transform where clause with tenant_column condition.
     *
     * @param matcher    regexp matcher
     * @param nameIndex  table name position
     * @param aliasIndex table name alias position
     * @see BaseParser#schema
     */
    public void parseColumn(Matcher matcher, int nameIndex, int aliasIndex) {
        matchColumn(matcher, nameIndex, aliasIndex);
        while (matcher.find()) {
            matchColumn(matcher, nameIndex, aliasIndex);
        }
    }

    public void matchColumn(Matcher matcher, int nameIndex, int aliasIndex) {
        String name = matcher.group(nameIndex);
        String alias = aliasIndex < 1 ? null : matcher.group(aliasIndex);

        TableCache cache = TableCache.get(name);
        if(cache.getType() == MultiTenantType.COLUMN) {
            String column = cache.getColumn();

            String temp = sql.toLowerCase().contains("where") ? " and " : " where ";
            boolean ifNoAlias = alias == null || alias.isEmpty() || alias.equalsIgnoreCase("where");
            StringBuilder tenantClause = new StringBuilder().append(temp)
                    .append(ifNoAlias ? name : alias).append(".")
                    .append(column).append(" = ?");
            Matcher suffixMatcher = suffix.matcher(sql);
            int position = -1;
            if (suffixMatcher.find()) {
                int start = suffixMatcher.start();
                position = position(sql, start);
                result.insert(start, tenantClause.toString());
            } else {
                result.append(tenantClause.toString());
            }
            parsedSQL.addParam(new ParsedParam<>(column, tenant, String.class, position));
        }
    }

    /**
     * calculate the prepared parameter position
     *
     * @param sql   SQL
     * @param start match position of pattern
     * @return position of a parameter
     */
    public int position(String sql, int start) {
        String substring = sql.substring(start);
        return substring.length() - substring.replaceAll("\\?", "").length();
    }

}

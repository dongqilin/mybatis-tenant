package com.dongql.mybatis.tenant.parser;

import com.dongql.mybatis.tenant.TenantContext;
import com.dongql.mybatis.tenant.annotations.MultiTenantType;
import com.dongql.mybatis.tenant.cache.ParsedParam;
import com.dongql.mybatis.tenant.cache.ParsedSQL;
import com.dongql.mybatis.tenant.cache.TableCache;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.dongql.mybatis.tenant.MultiTenantInterceptor.schemaPrefix;

/**
 * 对SQL语句进行分析并租户化
 * Created by dongqilin on 2017/7/11.
 */
public abstract class BaseParser {

    static final int mask = Pattern.CASE_INSENSITIVE | Pattern.MULTILINE;
    static final String TABLE_NAME = "[\n ]+([a-zA-Z0-9_]*)";
    static final Pattern suffix = Pattern.compile(" (group by|order by|limit)", mask);

    String tenant = TenantContext.get();

    ParsedSQL<String> parsedSQL;
    String sql;
    Pattern pattern;
    List<String> table = new ArrayList<>();

    StringBuffer result = null;

    public abstract ParsedSQL<String> parse();

    ParsedSQL<String> parse(int nameIndex, int aliasIndex) {

        Matcher matcher = pattern.matcher(sql);
        StringBuffer tmp = new StringBuffer();
        if (matcher.find()) {
            String valuesKeyword = matcher.group();
            String name = matcher.group(nameIndex);
            valuesKeyword = valuesKeyword.replace(name, TableCache.get(name).getSchema() + name);
            matcher.appendReplacement(tmp, valuesKeyword);
        }
        matcher.appendTail(tmp);
        parsedSQL.setSql(tmp.toString());
        // 系统初始化过程中不启动多租户模式，仅启动schema改写
        if (!TenantContext.isStarted()) return parsedSQL;

        matcher = pattern.matcher(sql);
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
                    parseColumn(name, alias);
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
            parsedSQL.setSql(sql);
        } else {
            parsedSQL.setSql(result.toString());
        }
        return parsedSQL;
    }

    public void parseSchema(Matcher matcher, String group, String name) {
        if (result == null) result = new StringBuffer();
        String after = group.replaceFirst(name, schemaPrefix + tenant + "." + name);
        matcher.appendReplacement(result, after);
        matcher.appendTail(result);
    }

    public void parseColumn(String name, String alias) {
        TableCache cache = TableCache.get(name);
        String column = cache.getColumn();

        if (result == null) result = new StringBuffer(sql);

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

    public void schema() {
        table.forEach(v -> {
            TableCache cache = TableCache.get(v);
            String schema = cache.getSchema();
            String sql = parsedSQL.getSql();
            if (schema != null && sql != null) {
                parsedSQL.setSql(sql.replaceAll("[\n ]+" + v, " " + schema + v));
            }
        });
    }

    private int position(String sql, int start) {
        String substring = sql.substring(start);
        return substring.length() - substring.replaceAll("\\?", "").length();
    }

}

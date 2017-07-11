package com.dongql.mybatis.tenant.parser;

import com.dongql.mybatis.tenant.TenantContext;
import com.dongql.mybatis.tenant.annotations.MultiTenantType;
import com.dongql.mybatis.tenant.cache.ParsedParam;
import com.dongql.mybatis.tenant.cache.ParsedSQL;
import com.dongql.mybatis.tenant.cache.TableCache;

import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.dongql.mybatis.tenant.MultiTenantInterceptor.schemaPrefix;

/**
 * 对SQL语句进行分析并租户化
 * Created by dongqilin on 2017/7/11.
 */
public abstract class BaseParser {

    static final String TABLE_NAME = "([a-zA-Z0-9_]*)";

    String tenant = TenantContext.get();

    ParsedSQL<String> parsedSQL;
    String sql;
    Pattern pattern;

    StringBuffer result = null;

    public abstract ParsedSQL<String> parse();

    ParsedSQL<String> parse(int nameIndex, int aliasIndex) {

        StringJoiner table = new StringJoiner(",");

        Matcher matcher = pattern.matcher(sql);
        if (matcher.find()) {

            String group = matcher.group();
            String name = matcher.group(nameIndex);
            String alias = aliasIndex < 1 ? null : matcher.group(aliasIndex);

            TableCache cache = TableCache.get(name);
            MultiTenantType type = cache.getType();

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
            table.add(name);
        }
        if (result != null) {
            parsedSQL.setSql(result.toString());
            return parsedSQL;
        }
        return null;
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

        String temp = sql.toLowerCase().contains("where") ? " and " : " where ";
        boolean ifNoAlias = alias == null || alias.isEmpty() || alias.equalsIgnoreCase("where");
        parsedSQL.addParam(new ParsedParam<>(column, tenant, String.class));
        if (result == null) result = new StringBuffer(sql);
        result.append(temp)
                .append(ifNoAlias ? name : alias).append(".")
                .append(column).append(" = ?");
    }

}

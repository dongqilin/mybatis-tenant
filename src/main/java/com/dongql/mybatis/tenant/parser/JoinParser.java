package com.dongql.mybatis.tenant.parser;

import com.dongql.mybatis.tenant.annotations.MultiTenantType;
import com.dongql.mybatis.tenant.cache.ParsedParam;
import com.dongql.mybatis.tenant.cache.ParsedSQL;
import com.dongql.mybatis.tenant.cache.TableCache;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * left|right|cross JOIN语句
 * Created by dongqilin on 2017/7/11.
 */
public class JoinParser extends SelectParser {

    public static final Pattern join = Pattern.compile("(left|right|cross)? join" + TABLE_NAME + "( as)?" + TABLE_NAME + "( on)?", mask);

    public JoinParser(ParsedSQL<String> parsedSQL) {
        super(parsedSQL.getSql());
        this.parsedSQL = parsedSQL;
        this.pattern = join;
    }

    public ParsedSQL<String> parse() {
        return parse(2, 4);
    }

    @Override
    public void matchColumn(Matcher matcher, int nameIndex, int aliasIndex) {
        String name = matcher.group(nameIndex);

        TableCache cache = TableCache.get(name);
        if (cache.getType() == MultiTenantType.COLUMN) {
            String column = cache.getColumn();

            String alias = aliasIndex < 1 ? null : matcher.group(aliasIndex);
            boolean ifNoAlias = alias == null || alias.isEmpty() || alias.equalsIgnoreCase("where");

            String on = matcher.group(5);
            boolean hasOn = on == null || on.isEmpty();
            String tenantClause = (hasOn ? " on " : " ") + (ifNoAlias ? name : alias) + "." + column + " = ?" + (hasOn ? " " : " and ");
            result.insert(matcher.end(), tenantClause);

            int position = position(sql, matcher.start());
            parsedSQL.addParam(new ParsedParam<>(column, tenant, String.class, position));
        }
    }


}

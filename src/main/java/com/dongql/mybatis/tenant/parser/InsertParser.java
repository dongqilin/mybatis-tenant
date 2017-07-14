package com.dongql.mybatis.tenant.parser;

import com.dongql.mybatis.tenant.cache.ParsedParam;
import com.dongql.mybatis.tenant.cache.ParsedSQL;
import com.dongql.mybatis.tenant.cache.TableCache;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

/**
 * INSERT 语句的解析
 * Created by dongqilin on 2017/7/11.
 */
public class InsertParser extends BaseParser {

    private Pattern values;

    public InsertParser(String sql) {
        this.parsedSQL = new ParsedSQL<>();
        this.sql = sql;
        this.pattern = Pattern.compile("^insert into " + TABLE_NAME + " ", CASE_INSENSITIVE);
        this.values = Pattern.compile("[ ]?\\) value[s ]?\\([ ]?", CASE_INSENSITIVE);
    }

    public ParsedSQL<String> parse() {
        return parse(1, -1);
    }

    @Override
    public void parseColumn(String name, String alias) {
        TableCache cache = TableCache.get(name);
        String column = cache.getColumn();

        Matcher matcher = values.matcher(sql);
        if (matcher.find()) {
            if (result == null) result = new StringBuffer();
            String valuesKeyword = matcher.group();
            matcher.appendReplacement(result, "," + column + valuesKeyword + "?,");
            parsedSQL.addParam(new ParsedParam<>(column, tenant, String.class, -1));
        }

        matcher.appendTail(result);
    }

}

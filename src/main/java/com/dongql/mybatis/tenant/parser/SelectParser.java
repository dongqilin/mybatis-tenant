package com.dongql.mybatis.tenant.parser;

import com.dongql.mybatis.tenant.cache.ParsedSQL;

import java.util.regex.Pattern;

/**
 * SELECT 语句的解析
 * Created by dongqilin on 2017/7/11.
 */
public class SelectParser extends BaseParser {

    public static final Pattern select = Pattern.compile(" from" + TABLE_NAME + TABLE_NAME + "?[, ]?", mask);

    public SelectParser(String sql) {
        this.sql = sql;
        this.parsedSQL = new ParsedSQL<>();
        this.pattern = select;
    }

    @Override
    public ParsedSQL<String> parse() {
        return parse(1, 2);
    }

}

package com.dongql.mybatis.tenant.parser;

import com.dongql.mybatis.tenant.cache.ParsedSQL;

import java.util.regex.Pattern;

/**
 * DELETE 语句的解析
 * Created by dongqilin on 2017/7/11.
 */
public class DeleteParser extends BaseParser {

    public DeleteParser(String sql) {
        this.parsedSQL = new ParsedSQL<>();
        this.sql = sql;
        this.pattern = Pattern.compile("^delete from" + TABLE_NAME + "[\n ]*", mask);
    }

    @Override
    public ParsedSQL<String> parse() {
        return parse(1, -1);
    }

}

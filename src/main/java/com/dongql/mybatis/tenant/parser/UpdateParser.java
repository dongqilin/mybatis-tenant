package com.dongql.mybatis.tenant.parser;

import com.dongql.mybatis.tenant.cache.ParsedSQL;

import java.util.regex.Pattern;

/**
 * UPDATE 语句的解析
 * Created by dongqilin on 2017/7/11.
 */
public class UpdateParser extends BaseParser {

    public UpdateParser(String sql) {
        this.parsedSQL = new ParsedSQL<>();
        this.sql = sql;
        this.pattern = Pattern.compile("^update" + TABLE_NAME + " ", mask);
    }

    @Override
    public ParsedSQL<String> parse() {
        return parse(1, -1);
    }

}

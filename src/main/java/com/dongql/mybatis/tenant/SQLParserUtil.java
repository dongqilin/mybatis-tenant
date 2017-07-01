package com.dongql.mybatis.tenant;

/**
 * 解析SQL，分析所有表，根据表的租户类型重新生成SQL
 * Created by dongqilin on 01/07/2017.
 */
public class SQLParserUtil {

    private SQLParserUtil() {
    }

    private static String from = " from ";
    private static String join = " [left|right|outer]+ join ";
    private static String insertInto = "^insert into ";
    private static String update = "^update ";
    private static String deleteFrom = "^delete from ";

    public static String parse(String sql){

        return null;
    }


}

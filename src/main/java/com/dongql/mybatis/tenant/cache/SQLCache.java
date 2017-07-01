package com.dongql.mybatis.tenant.cache;

import com.dongql.mybatis.tenant.SQLParserUtil;
import com.dongql.mybatis.tenant.exception.SQLParseException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * 所有Mapper接口解析后的缓存对象
 * Created by dongqilin on 01/07/2017.
 */
public class SQLCache {

    private static Map<String, SQLCache> SQL_CACHE = new HashMap<>();
    private static String prefix = "${";
    private static String suffix = "}";

    private String sql;
    private List<String> param;

    private SQLCache(String sql, List<String> param) {
        this.sql = SQLParserUtil.parse(sql);
        this.param = param;
    }

    public List<String> param() {
        return param;
    }

    public static void add(String key, String sql, List<String> param) {
        SQL_CACHE.put(key, new SQLCache(sql, param));
    }

    /**
     * 获取解析后的SQL
     * @param key Mapper接口唯一标识：mapperInterface + methodName
     * @param param tenant参数
     * @return SQL
     */
    public String get(String key, Map<String, String> param) {
        SQLCache cache = SQL_CACHE.get(key);
        String sql = cache.toString();
        if (param != null && param.size() > 0) {
            for (String p : cache.param()) {
                sql = sql.replace(prefix + p + suffix, param.get(p));
            }
        }
        StringJoiner joiner = check(cache);
        if (joiner != null) {
            throw new SQLParseException("SQL解析失败，存在未解析的租户标识：" + joiner.toString());
        }
        return sql;
    }

    private StringJoiner check(SQLCache cache) {
        String sql = cache.toString();
        StringJoiner joiner = null;
        while (sql.contains(prefix)) {
            joiner = joiner == null ? new StringJoiner(",") : joiner;
            int start = sql.indexOf(prefix);
            int end = sql.indexOf(suffix, start);
            String code = sql.substring(start, end);
            joiner.add(code);
            sql = sql.substring(end);
        }
        return joiner;
    }


    @Override
    public String toString() {
        return sql;
    }

}

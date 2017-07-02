package com.dongql.mybatis.tenant;

import com.dongql.mybatis.tenant.mapper.IUserMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * Created by dongqilin on 2017/6/28.
 */
@ContextConfiguration(locations = {"classpath*:spring/spring*.xml"})
public class TenantTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private IUserMapper userMapper;

    @Before
    public void init() {
        TenantContext.set("leo");
    }

    @Test
    public void tenant() {
        userMapper.getUser(600841005L);
        userMapper.getUserByName("eo");
    }


}

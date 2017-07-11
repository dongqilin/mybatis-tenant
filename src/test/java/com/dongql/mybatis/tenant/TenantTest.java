package com.dongql.mybatis.tenant;

import com.dongql.mybatis.tenant.entity.StaticData;
import com.dongql.mybatis.tenant.entity.User;
import com.dongql.mybatis.tenant.entity.vo.UserPasswordVo;
import com.dongql.mybatis.tenant.entity.vo.UserVo;
import com.dongql.mybatis.tenant.enums.Gender;
import com.dongql.mybatis.tenant.enums.VipLevel;
import com.dongql.mybatis.tenant.mapper.IStaticDataMapper;
import com.dongql.mybatis.tenant.mapper.IUserMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.util.List;

/**
 * Created by dongqilin on 2017/6/28.
 */
@ContextConfiguration(locations = {"classpath*:spring/spring*.xml"})
public class TenantTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private IUserMapper userMapper;
    @Autowired
    private IStaticDataMapper staticDataMapper;

    @Before
    public void init() {
        TenantContext.set("dongql");
    }

    @Test
    public void tenant() {
        long uid = 11111L;

        userMapper.getUser(600841005L);
        userMapper.getUserByName("其林");

        List<StaticData> vipLevel = staticDataMapper.getDataList("vip_level");
        System.out.println(vipLevel);

        UserVo userWithVip = userMapper.getUserWithVip(600841005L);
        System.out.println(userWithVip);

        UserPasswordVo userWithPassword = userMapper.getUserWithPassword(600841005L);
        System.out.println(userWithPassword);

        User user = new User();
        user.setUid(uid);
        user.setGender(Gender.FEMALE);
        user.setUserName("xxxxx");
        user.setVip(VipLevel.DIAMOND);
        userMapper.insertUser(user);

        user.setUserName("yyyyyyyy");
        userMapper.updateUser(user);

        userMapper.deleteUser(uid);

    }


}

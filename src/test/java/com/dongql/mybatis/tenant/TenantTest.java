package com.dongql.mybatis.tenant;

import com.dongql.mybatis.tenant.entity.User;
import com.dongql.mybatis.tenant.entity.vo.UserVo;
import com.dongql.mybatis.tenant.enums.Gender;
import com.dongql.mybatis.tenant.enums.VipLevel;
import com.dongql.mybatis.tenant.mapper.IStaticDataMapper;
import com.dongql.mybatis.tenant.mapper.IUserMapper;
import com.dongql.mybatis.tenant.service.interfaces.IUserService;
import org.junit.Before;
import org.junit.BeforeClass;
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
    private IUserService userService;
    @Autowired
    private IUserMapper userMapper;
    @Autowired
    private IStaticDataMapper staticDataMapper;

    private User user;

    @BeforeClass
    public static void initG(){

    }

    @Before
    public void init() {
        TenantContext.set("dongql");
        user = new User();
        user.setGender(Gender.FEMALE);
        user.setUserName("xxxxx");
        user.setVip(VipLevel.DIAMOND);
    }

    @Test
    public void tenant() {

//        userMapper.getUsers();
//
//        userMapper.getUser(600841005L);
//        userMapper.getUserByName("其林");
//
//        UserPasswordVo userWithPassword = userMapper.getUserWithPassword(600841005L);
//        System.out.println(userWithPassword);
//
//        userMapper.insertUser(user);
//
//        user.setUserName("yyyyyyyy");
//        userMapper.updateUser(user);
//
//        userMapper.deleteUser(user.getUid());

        TenantContext.start();

//        List<StaticData> vipLevel = staticDataMapper.getDataList("vip_level");
//        System.out.println(vipLevel);

        UserVo userWithVip = userMapper.getUserWithVip(600841005L);
        System.out.println(userWithVip);

    }

    @Test
    public void service(){

        userService.save(user);

        user.setUserName("yyyyyyyy");
        userService.update(user);

        User u = userService.get(user.getUid());
        System.out.println(u);

        userService.delete(this.user);
    }

    @Test
    public void pager(){
        Pager<User> pager = new Pager<>();
        pager.setUsePager(true);
        pager.setOffset(2);
        pager.setLimit(3);
        List<User> user = userService.getUser(pager, "xxx");
        System.out.println(user);
    }

}

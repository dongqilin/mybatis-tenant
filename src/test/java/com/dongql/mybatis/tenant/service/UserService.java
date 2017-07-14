package com.dongql.mybatis.tenant.service;

import com.dongql.mybatis.tenant.Pager;
import com.dongql.mybatis.tenant.entity.User;
import com.dongql.mybatis.tenant.mapper.IUserMapper;
import com.dongql.mybatis.tenant.service.interfaces.IUserService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by dongqilin on 2017/7/12.
 */
@Service
public class UserService extends BaseServiceImpl<User> implements IUserService {

    @Autowired
    private IUserMapper userMapper;

    @Override
    public List<User> getUser(Pager<User> pager, String userName){
        if(pager.getUsePager()){
            PageHelper.offsetPage(pager.getOffset(), pager.getLimit());
        }
        return userMapper.getUserByName(userName);
    }


}

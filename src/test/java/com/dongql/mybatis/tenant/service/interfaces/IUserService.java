package com.dongql.mybatis.tenant.service.interfaces;

import com.dongql.mybatis.tenant.Pager;
import com.dongql.mybatis.tenant.entity.User;

import java.util.List;

/**
 * Created by dongqilin on 2017/7/12.
 */
public interface IUserService extends BaseService<User> {
    List<User> getUser(Pager<User> pager, String userName);
}

package com.xjx.settings.service;

import com.xjx.settings.domain.User;

import java.util.List;
import java.util.Map;


public interface UserService {
    public User queryUserByLoginActAndPwd(Map map);
    public List<User> queryAllUsers();
}

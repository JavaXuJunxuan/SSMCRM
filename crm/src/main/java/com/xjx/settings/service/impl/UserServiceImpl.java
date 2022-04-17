package com.xjx.settings.service.impl;

import com.xjx.settings.dao.UserMapper;
import com.xjx.settings.domain.User;
import com.xjx.settings.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public User queryUserByLoginActAndPwd(Map map) {
        User user = userMapper.selectUserByLoginActAndPwd(map);
        return user;
    }

    @Override
    public List<User> queryAllUsers() {
        return userMapper.selectAllUsers();
    }
}

package com.zybtest.crm.settings.service.impl;

import com.zybtest.crm.settings.domain.User;
import com.zybtest.crm.settings.mapper.UserMapper;
import com.zybtest.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service("UserService")
@ComponentScan
public class UserServiceImpl implements UserService {
    @Autowired

    private UserMapper userMapper;
    @Override
    public User selectUserByLoginActAndPwd(Map<String, Object> map) {
//        System.out.println("123");
        return userMapper.selectUserByLoginActAndPwd(map);
    }

    @Override
    public List<User> quaryAllUser() {
        return userMapper.selectAllUser();
    }
}

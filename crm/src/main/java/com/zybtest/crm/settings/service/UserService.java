package com.zybtest.crm.settings.service;

import com.alibaba.druid.support.json.JSONUtils;
import com.zybtest.crm.settings.domain.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    User selectUserByLoginActAndPwd(Map<String,Object> map);
    List<User> quaryAllUser();
}

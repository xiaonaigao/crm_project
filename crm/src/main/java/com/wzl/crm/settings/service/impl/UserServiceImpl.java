package com.wzl.crm.settings.service.impl;

import com.wzl.crm.settings.mapper.UserMapper;
import com.wzl.crm.settings.service.UserService;
import com.wzl.crm.workbench.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author wang
 * @version 1.0
 */
@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper userMapper;
	@Override
	public User queryUserByLoginActAndPwd(Map<String, Object> map) {
		return userMapper.selectUserByLoginActAndPwd(map);
	}

	@Override
	public List<User> queryAllUsers() {
		return userMapper.selectAllUsers();
	}
}

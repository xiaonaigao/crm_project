package com.wzl.crm.settings.service;

import com.wzl.crm.workbench.domain.User;

import java.util.List;
import java.util.Map;

/**
 * @author wang
 * @version 1.0
 */
public interface UserService {
	// 根据用户名和密码查询
	User queryUserByLoginActAndPwd(Map<String,Object> map);
	// 查询所有用户
	List<User> queryAllUsers();
}

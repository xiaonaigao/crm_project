package com.wzl.crm.settings.service;

import com.wzl.crm.workbench.domain.User;

import java.util.Map;

/**
 * @author wang
 * @version 1.0
 */
public interface UserService {
	User queryUserByLoginActAndPwd(Map<String,Object> map);
}

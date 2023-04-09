package com.wzl.crm.workbench.web.controller;

import com.wzl.crm.settings.service.UserService;
import com.wzl.crm.workbench.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author wang
 * @version 1.0
 */
@Controller
public class ActivityController{
	/**
	 * 注入UserService
	 */
	@Autowired
	private UserService userService;
	/**
	 * 市场活动
	 */
	@RequestMapping("/workbench/activity/index.do")
	public String activityIndex(HttpServletRequest request){
		//调用service层方法，查询所有用户
		List<User> userList = userService.queryAllUsers();
		//存放request
		request.setAttribute("userList",userList);
		return "workbench/activity/index";
	}
}

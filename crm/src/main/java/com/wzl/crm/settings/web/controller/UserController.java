package com.wzl.crm.settings.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {
	/**
	 * 点击主页跳转到登录
	 */
	@RequestMapping("settings/qx/user/toLogin.do")
	public String toLogin(){
		return "settings/qx/user/login";
	}

	/**
	 * 用户登录功能
	 * 返回一个json
	 * 通用性高，返回Object
	 */
	@RequestMapping("settings/qx/user/login.do")
	public @ResponseBody Object login(){
		return null;
	}

}

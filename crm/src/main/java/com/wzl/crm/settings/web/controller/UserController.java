package com.wzl.crm.settings.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
	/**
	 * 跳转到登录
	 */
	@GetMapping("settings/qx/user/toLogin.do")
	public String toLogin(){
		System.out.println("dddd");
		return "settings/qx/user/login";
	}
}

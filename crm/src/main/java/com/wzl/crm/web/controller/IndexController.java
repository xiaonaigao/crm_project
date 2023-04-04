package com.wzl.crm.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
	/**
	 * 主页跳转
	 */
	@GetMapping("/")
	public String index() {
		return "index";
	}
}

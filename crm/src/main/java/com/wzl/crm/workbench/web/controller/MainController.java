package com.wzl.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author wang
 * @version 1.0
 */
@Controller
public class MainController {
	/**
	 * 工作台
	 * @return
	 */
	@RequestMapping("/workbench/main/index.do")
	public String index(){
		return "workbench/main/index";
	}
}

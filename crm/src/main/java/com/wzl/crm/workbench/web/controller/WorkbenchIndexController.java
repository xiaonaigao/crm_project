package com.wzl.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author wang
 * @version 1.0
 */
@Controller
public class WorkbenchIndexController {
	@RequestMapping("workbench/index.do")
	public String index(){
		return "workbench/index";
	}
}

package com.wzl.crm.settings.web.controller;

import com.wzl.crm.commons.contants.Contants;
import com.wzl.crm.commons.domain.ReturnObject;
import com.wzl.crm.commons.utils.DateUtils;
import com.wzl.crm.settings.service.UserService;
import com.wzl.crm.workbench.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {
	//注入userService对象的实现类
	@Autowired
	private UserService userService;

	/**
	 * 点击主页跳转到登录
	 */
	@RequestMapping("/settings/qx/user/toLogin.do")
	public String toLogin() {
		return "settings/qx/user/login";
	}

	/**
	 * 用户登录功能
	 * 返回一个json
	 * 通用性高，返回Object
	 * ResponseBody--转换json
	 */
	@RequestMapping("/settings/qx/user/login.do")
	public @ResponseBody
	Object login(String loginAct, String loginPwd, String isRemPwd, HttpServletRequest request, HttpSession session, HttpServletResponse response) {
		//封装参数
		Map<String, Object> map = new HashMap<>();
		map.put("loginAct", loginAct);
		map.put("loginPwd", loginPwd);
		//调用service层方法查询用户
		User user = userService.queryUserByLoginActAndPwd(map);
		//根据查询结果，生成响应信息
		ReturnObject returnObject = new ReturnObject();
		//根据查询结果响应信息
		if (user == null) {// 登录失败，用户名或密码错误
			returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
			returnObject.setMessage("用户名或密码错误");
		} else if (DateUtils.formateDateTime(new Date()).compareTo(user.getExpireTime()) > 0) { //时间失效了
			returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
			returnObject.setMessage("账号过期");
		} else if ("0".equals(user.getLockState())) { //0 锁定了 失败
			returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
			returnObject.setMessage("账号锁定");
		} else if (!user.getAllowIps().contains(request.getRemoteAddr())) { // ip地址不在范围
			returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
			returnObject.setMessage("ip受限");
		} else {//登录成功
			returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
			// 把user存放到session
			session.setAttribute(Contants.SESSION_USER, user);

			//记住密码
			if ("true".equals(isRemPwd)) {
				Cookie c1 = new Cookie("loginAct", user.getLoginAct());//账号
				//设置生命周期
				c1.setMaxAge(10 * 24 * 60 * 60);
				//保存到浏览器
				response.addCookie(c1);
				Cookie c2 = new Cookie("loginPwd", user.getLoginPwd());//密码
				//设置生命周期
				c2.setMaxAge(10 * 24 * 60 * 60);
				//保存到浏览器
				response.addCookie(c2);
			} else {
				//删除之前cookie
				Cookie c1 = new Cookie("loginAct", "1");//账号
				//设置生命周期
				c1.setMaxAge(0);
				//保存到浏览器
				response.addCookie(c1);
				Cookie c2 = new Cookie("loginPwd", "1");//密码
				//设置生命周期
				c2.setMaxAge(0);
				//保存到浏览器
				response.addCookie(c2);
			}
		}
		return returnObject;
	}

	/**
	 * 安全退出
	 */
	@RequestMapping("/settings/qx/user/logout.do")
	public String logout(HttpServletResponse response,HttpSession session) {
		//清空cookie
		//删除之前cookie
		Cookie c1 = new Cookie("loginAct", "1");//账号
		//设置生命周期
		c1.setMaxAge(0);
		//保存到浏览器
		response.addCookie(c1);
		Cookie c2 = new Cookie("loginPwd", "1");//密码
		//设置生命周期
		c2.setMaxAge(0);
		//保存到浏览器
		response.addCookie(c2);
		//删除Session
		session.invalidate();
		return "redirect:/";
	}

}

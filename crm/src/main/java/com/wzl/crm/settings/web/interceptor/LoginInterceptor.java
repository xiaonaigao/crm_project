package com.wzl.crm.settings.web.interceptor;

import com.wzl.crm.commons.contants.Contants;
import com.wzl.crm.workbench.domain.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author wang
 * @version 1.0
 * 登录拦截器
 */
public class LoginInterceptor implements HandlerInterceptor {
	// 控制器之前执行
	@Override
	public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
		// 如果session有user那么登录了，否则没有。
		HttpSession session = httpServletRequest.getSession();
		User user = (User) session.getAttribute(Contants.SESSION_USER);
		if (user == null) {
			// 拦截器自己重定向要带项目的名字
			httpServletResponse.sendRedirect(httpServletRequest.getContextPath());// "/crm"
			return false; //拦截，未登录
		}
		return true; //放行

	}

	// 控制器之后执行
	@Override
	public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

	}

	// 全部渲染完执行
	@Override
	public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

	}
}

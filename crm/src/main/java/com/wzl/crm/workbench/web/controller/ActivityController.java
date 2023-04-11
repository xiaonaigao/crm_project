package com.wzl.crm.workbench.web.controller;

import com.wzl.crm.commons.contants.Contants;
import com.wzl.crm.commons.domain.ReturnObject;
import com.wzl.crm.commons.utils.DateUtils;
import com.wzl.crm.commons.utils.UUIDUtils;
import com.wzl.crm.settings.service.UserService;
import com.wzl.crm.workbench.domain.Activity;
import com.wzl.crm.workbench.domain.User;
import com.wzl.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

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
	 * 注入ActivityService
	 */
	@Autowired
	private ActivityService activityService;
	/**
	 * 市场活动首页
	 */
	@RequestMapping("/workbench/activity/index.do")
	public String activityIndex(HttpServletRequest request){
		//调用service层方法，查询所有用户
		List<User> userList = userService.queryAllUsers();
		//存放request
		request.setAttribute("userList",userList);
		return "workbench/activity/index";
	}
	/**
	 * 创建市场活动
	 */
	@RequestMapping("/workbench/activity/saveCreateActivity.do")
	public @ResponseBody Object saveCreateActivity(Activity activity, HttpSession session){
		// 封装参数--下面3个需要自己输入
		activity.setId(UUIDUtils.getUUID());
		User user = (User) session.getAttribute(Contants.SESSION_USER);
		activity.setCreateBy(user.getId());
		activity.setCreateTime(DateUtils.formateDateTime(new Date()));
		// 调用service--增删改要捕获异常
		// 生成json
		ReturnObject returnObject = new ReturnObject();
		try {
			int ret = activityService.saveCreateActivity(activity);
			if (ret>0){
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
			}else {
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
				returnObject.setMessage("系统忙，请稍后再试...");
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
			returnObject.setMessage("系统忙，请稍后再试...");
		}
		return returnObject;
	}

	/**
	 * 根据条件，分页
	 */
	@RequestMapping("/workbench/activity/queryActivityByConditionForPage.do")
	public @ResponseBody Object queryActivityByConditionForPage(String name,String owner,String startDate,String endDate,int pageNo,int pageSize){
		//封装参数
		Map<String,Object> map = new HashMap<>();
		map.put("name",name);
		map.put("owner",owner);
		map.put("startDate",startDate);
		map.put("endDate",endDate);
		map.put("beginNo",(pageNo-1)*pageSize);
		map.put("pageSize",pageSize);
		//调用Service
		List<Activity> activityList = activityService.queryActivityByConditionForPage(map);
		int totalRows = activityService.queryCountOfActivityByCondition(map);
		//生成响应信息
		Map<String,Object> retMap = new HashMap<>();
		retMap.put("activityList",activityList);
		retMap.put("totalRows",totalRows);
		return retMap;
	}

}

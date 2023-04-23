package com.wzl.crm.workbench.web.controller;

import com.wzl.crm.commons.contants.Contants;
import com.wzl.crm.commons.domain.ReturnObject;
import com.wzl.crm.commons.utils.DateUtils;
import com.wzl.crm.commons.utils.UUIDUtils;
import com.wzl.crm.workbench.domain.ActivityRemark;
import com.wzl.crm.workbench.domain.User;
import com.wzl.crm.workbench.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author wang
 * @version 1.0
 */
@Controller
public class ActivityRemarkController {
	@Autowired
	private ActivityRemarkService activityRemarkService;

	/**
	 * 发表市场活动备注
	 */
	@RequestMapping("/workbench/activity/saveCreateActivityRemark.do")
	public @ResponseBody
	Object saveCreateActivityRemark(ActivityRemark remark, HttpSession session) {
		// 获取session
		User user = (User) session.getAttribute(Contants.SESSION_USER);
		// 封装参数
		remark.setId(UUIDUtils.getUUID());
		remark.setCreateTime(DateUtils.formateDateTime(new Date()));
		remark.setCreateBy(user.getId());
		remark.setEditFlag(Contants.RETURN_OBJECT_CODE_FAIL);
		// 调用service
		ReturnObject returnObject = new ReturnObject();
		try {
			int ret = activityRemarkService.saveCreateActivityRemark(remark);
			if (ret > 0) {
				//成功
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
				returnObject.setRetDate(remark);
			} else {
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
				returnObject.setMessage("系统繁忙，稍后再试");
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
			returnObject.setMessage("系统繁忙，稍后再试");
		}
		return returnObject;

	}


	/**
	 * 删除市场活动备注
	 */
	@RequestMapping("/workbench/activity/deleteActivityRemarkById.do")
	public @ResponseBody
	Object deleteActivityRemarkById(String id) {
		// 调用service
		ReturnObject returnObject = new ReturnObject();
		try {
			int ret = activityRemarkService.deleteActivityRemarkById(id);
			if (ret > 0) {
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
			} else {
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
				returnObject.setMessage("系统错误稍后再试");
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
			returnObject.setMessage("系统错误稍后再试");
		}
		return returnObject;
	}

	/**
	 * 修改市场活动备注
	 */
	@RequestMapping("/workbench/activity/saveEditActivityRemark.do")
	public @ResponseBody Object saveEditActivityRemark(ActivityRemark remark, HttpSession session){
		// 获取登录的user
		User user = (User) session.getAttribute(Contants.SESSION_USER);
		// 封装参数
		remark.setEditFlag(Contants.RETURN_OBJECT_CODE_SUCCESS);
		remark.setEditBy(user.getId());
		remark.setEditTime(DateUtils.formateDateTime(new Date()));
		// 调用service
		ReturnObject returnObject = new ReturnObject();
		try {
			int ret = activityRemarkService.saveEditActivityRemark(remark);
			if (ret>0) {
				// 成功
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
				returnObject.setRetDate(remark);
			}else {
				// 失败
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
				returnObject.setMessage("系统繁忙稍后再试");
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
			returnObject.setMessage("系统繁忙稍后再试");
		}
		return returnObject;
	}

}

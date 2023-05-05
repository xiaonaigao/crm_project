package com.wzl.crm.workbench.web.controller;

import com.wzl.crm.commons.contants.Contants;
import com.wzl.crm.commons.domain.ReturnObject;
import com.wzl.crm.commons.utils.DateUtils;
import com.wzl.crm.commons.utils.UUIDUtils;
import com.wzl.crm.workbench.domain.ClueRemark;
import com.wzl.crm.workbench.domain.User;
import com.wzl.crm.workbench.service.ClueRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * @author wang
 * @version 1.0
 */
@Controller
public class ClueRemarkController {
	@Autowired
	private ClueRemarkService clueRemarkService;

	/**
	 * 创建线索评论
	 */
	@RequestMapping("/workbench/clue/saveCreateClueRemark.do")
	public @ResponseBody
	Object saveCreateClueRemark(ClueRemark clueRemark, HttpSession session) {
		//获取user
		User user = (User) session.getAttribute(Contants.SESSION_USER);
		// 封装参数
		clueRemark.setId(UUIDUtils.getUUID());
		clueRemark.setCreateBy(user.getId());
		clueRemark.setCreateTime(DateUtils.formateDateTime(new Date()));
		clueRemark.setEditFlag(Contants.RETURN_OBJECT_CODE_FAIL);
		// 调用service
		ReturnObject returnObject = new ReturnObject();
		try {
			int ret = clueRemarkService.saveCreateClueRemark(clueRemark);
			if (ret>0){
				// 成功
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
				returnObject.setRetDate(clueRemark);
			}else {
				// 失败
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
	 * 删除线索
	 */
	@RequestMapping("/workbench/clue/deleteClueRemarkById.do")
	public @ResponseBody
	Object deleteClueRemarkById(String id) {
		// 调用service
		ReturnObject returnObject = new ReturnObject();
		try {
			int ret = clueRemarkService.deleteClueRemarkById(id);
			if (ret>0){
				// 成功
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
			}else {
				// 失败
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
	 * 修改线索
	 */
	@RequestMapping("/workbench/clue/editClueRemarkById.do")
	public @ResponseBody
	Object editClueRemarkById(ClueRemark clueRemark,HttpSession session) {
		User user = (User) session.getAttribute(Contants.SESSION_USER);
		// 封装参数
		clueRemark.setEditFlag(Contants.RETURN_OBJECT_CODE_SUCCESS);
		clueRemark.setEditBy(user.getId());
		clueRemark.setEditTime(DateUtils.formateDateTime(new Date()));
		// 调用service
		// 调用service
		ReturnObject returnObject = new ReturnObject();
		try {
			int ret = clueRemarkService.editClueRemarkById(clueRemark);
			if (ret>0){
				// 成功
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
				returnObject.setRetDate(clueRemark);
			}else {
				// 失败
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


}

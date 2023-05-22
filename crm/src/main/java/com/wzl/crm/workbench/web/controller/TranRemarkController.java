package com.wzl.crm.workbench.web.controller;

import com.wzl.crm.commons.contants.Contants;
import com.wzl.crm.commons.domain.ReturnObject;
import com.wzl.crm.commons.utils.DateUtils;
import com.wzl.crm.commons.utils.UUIDUtils;
import com.wzl.crm.workbench.domain.TranRemark;
import com.wzl.crm.workbench.domain.User;
import com.wzl.crm.workbench.service.TranRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wang
 * @version 1.0
 */
@Controller
public class TranRemarkController {
	@Autowired
	private TranRemarkService tranRemarkService;

	/**
	 * 增加备注
	 */
	@RequestMapping("/workbench/transaction/addTranRemark")
	public @ResponseBody Object addTranRemark(String tranId, String noteContent, HttpSession session){
		//获取user
		User user = (User) session.getAttribute(Contants.SESSION_USER);
		// 封装参数
		TranRemark tranRemark = new TranRemark();
		tranRemark.setId(UUIDUtils.getUUID());
		tranRemark.setNoteContent(noteContent);
		tranRemark.setCreateBy(user.getId());
		tranRemark.setCreateTime(DateUtils.formateDateTime(new Date()));
		tranRemark.setEditFlag(Contants.RETURN_OBJECT_CODE_FAIL);
		tranRemark.setTranId(tranId);
		// 调用service
		ReturnObject returnObject = new ReturnObject();
		try {
			int ret = tranRemarkService.saveTranRemarkById(tranRemark);
			if (ret>0){
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
				returnObject.setRetDate(tranRemark);
			}else {
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
	 * 删除备注
	 */
	@RequestMapping("/workbench/transaction/deleteTranRemark")
	public @ResponseBody Object deleteTranRemark(String id){
		// 调用service
		ReturnObject returnObject = new ReturnObject();
		try {
			int ret = tranRemarkService.deleteTranRemarkById(id);
			if (ret>0){
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
			}else {
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
	 * 修改备注
	 */
	@RequestMapping("/workbench/transaction/editTranRemark")
	public @ResponseBody Object editTranRemark(String id,String noteContent,HttpSession session){
		// 封装参数
		User user = (User) session.getAttribute(Contants.SESSION_USER);
		TranRemark tranRemark =  new TranRemark();
		tranRemark.setId(id);
		tranRemark.setNoteContent(noteContent);
		tranRemark.setEditBy(user.getId());
		tranRemark.setEditTime(DateUtils.formateDateTime(new Date()));
		tranRemark.setEditFlag(Contants.RETURN_OBJECT_CODE_SUCCESS);
		// 调用service
		ReturnObject returnObject = new ReturnObject();
		try {
			int ret = tranRemarkService.deleteTranRemarkById(id);
			if (ret>0){
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
				returnObject.setRetDate(tranRemark);
			}else {
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

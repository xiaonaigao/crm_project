package com.wzl.crm.workbench.web.controller;

import com.wzl.crm.commons.contants.Contants;
import com.wzl.crm.commons.domain.ReturnObject;
import com.wzl.crm.commons.utils.DateUtils;
import com.wzl.crm.commons.utils.UUIDUtils;
import com.wzl.crm.settings.domain.DicValue;
import com.wzl.crm.settings.service.DicValueService;
import com.wzl.crm.settings.service.UserService;
import com.wzl.crm.workbench.domain.Clue;
import com.wzl.crm.workbench.domain.ClueRemark;
import com.wzl.crm.workbench.domain.User;
import com.wzl.crm.workbench.service.ClueRemarkService;
import com.wzl.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wang
 * @version 1.0
 */
@Controller
public class ClueController {
	// 注入userService
	@Autowired
	private UserService userService;
	// 注入dicValueService
	@Autowired
	private DicValueService dicValueService;
	// 注入线索
	@Autowired
	private ClueService clueService;
	@Autowired
	private ClueRemarkService clueRemarkService;

	// 创建线索1：加载下拉框
	@RequestMapping("/workbench/clue/index.do")
	public String index(HttpServletRequest request) {
		// 查询所有用户
		List<User> userList = userService.queryAllUsers();
		// 查询线索状态，线索来源,称呼
		List<DicValue> clueStateList = dicValueService.queryDicValueByTypeCode("clueState");
		List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");
		List<DicValue> appellationList = dicValueService.queryDicValueByTypeCode("appellation");
		// 保存在请求域中
		request.setAttribute("userList", userList);
		request.setAttribute("clueStateList", clueStateList);
		request.setAttribute("sourceList", sourceList);
		request.setAttribute("appellationList", appellationList);
		return "workbench/clue/index";
	}

	// 创建线索2:保存
	@RequestMapping("/workbench/clue/saveCreateClue.do")
	public @ResponseBody
	Object saveCreateClue(Clue clue, HttpSession session) {
		// 封装参数
		User user = (User) session.getAttribute(Contants.SESSION_USER);
		clue.setId(UUIDUtils.getUUID());
		clue.setCreateTime(DateUtils.formateDateTime(new Date()));
		clue.setCreateBy(user.getId());
		// 调用service
		ReturnObject returnObject = new ReturnObject();
		try {
			int ret = clueService.saveCreateClue(clue);
			if (ret > 0) {
				// 成功
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
			} else {
				// 失败
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
				returnObject.setMessage("系统错误，稍后再试");
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 失败
			returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
			returnObject.setMessage("系统错误，稍后再试");
		}
		return returnObject;

	}

	// 查询线索
	@RequestMapping("/workbench/clue/queryClueByCondition.do")
	public @ResponseBody
	Object queryClueByCondition(String fullname, String company, String phone, String mphone, String source, String owner, String state, int pageNo, int pageSize) {
		// 封装参数
		HashMap<String, Object> map = new HashMap<>();
		map.put("fullname", fullname);
		map.put("company", company);
		map.put("phone", phone);
		map.put("mphone", mphone);
		map.put("source", source);
		map.put("owner", owner);
		map.put("state", state);
		map.put("beginNo", (pageNo - 1) * pageSize);
		map.put("pageSize", pageSize);
		// 调用service
		Map<String, Object> retMap = new HashMap<>();
		List<Clue> clueList = clueService.queryClueByConditionForPage(map);
		int totalRows = clueService.queryCountOfClueByCondition(map);
		// 保存在json
		retMap.put("clueList", clueList);
		retMap.put("totalRows", totalRows);
		return retMap;
	}
	// 删除线索
	@RequestMapping("/workbench/clue/deleteClueByIds.do")
	public @ResponseBody
	Object deleteClueByIds(String[]id) {
		// 调用Service
		ReturnObject returnObject = new ReturnObject();
		try {
			int ret = clueService.deleteClueByIds(id);
			if (ret>0){
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
			}else {
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
				returnObject.setMessage("系统错误，稍后再试");
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
			returnObject.setMessage("系统错误，稍后再试");
		}
		return returnObject;
	}

	/**
	 *查询id线索
	 */
	@RequestMapping("/workbench/clue/queryClueById.do")
	public @ResponseBody
	Object queryClueById(String id) {
		Clue clue = clueService.queryClueById(id);
		return clue;
	}
	/**
	 * 修改线索
	 */
	@RequestMapping("/workbench/clue/updateClue.do")
	public @ResponseBody
	Object updateClue(Clue clue,HttpSession session) {
		// 获取user
		User user = (User) session.getAttribute(Contants.SESSION_USER);
		// 封装参数
		clue.setEditBy(user.getId());
		clue.setEditTime(DateUtils.formateDateTime(new Date()));
		// 调用service
		ReturnObject returnObject = new ReturnObject();
		try {
			int ret = clueService.updateClue(clue);
			if (ret>0){
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
			}else {
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
				returnObject.setMessage("系统错误，稍后再试");
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
			returnObject.setMessage("系统错误，稍后再试");
		}
		return returnObject;
	}

	/**
	 * 线索详情
	 */
	@RequestMapping("/workbench/clue/selectClueForDetailById.do")
	public String selectClueForDetailById(String id,HttpServletRequest request){
		// 调用service
		Clue clue = clueService.queryClueForDetailById(id);
		List<ClueRemark> clueRemarkList = clueRemarkService.queryClueRemarkByClueId(id);
		// 保存请求域
		request.setAttribute("clue",clue);
		request.setAttribute("clueRemarkList",clueRemarkList);

		// 返回页面
		return "workbench/clue/detail";
	}

}

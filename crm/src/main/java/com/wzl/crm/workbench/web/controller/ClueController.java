package com.wzl.crm.workbench.web.controller;

import com.wzl.crm.commons.contants.Contants;
import com.wzl.crm.commons.domain.ReturnObject;
import com.wzl.crm.commons.utils.DateUtils;
import com.wzl.crm.commons.utils.UUIDUtils;
import com.wzl.crm.settings.domain.DicValue;
import com.wzl.crm.settings.service.DicValueService;
import com.wzl.crm.settings.service.UserService;
import com.wzl.crm.workbench.domain.*;
import com.wzl.crm.workbench.service.ActivityService;
import com.wzl.crm.workbench.service.ClueActivityRelationService;
import com.wzl.crm.workbench.service.ClueRemarkService;
import com.wzl.crm.workbench.service.ClueService;
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
	// 注入线索评论
	@Autowired
	private ClueRemarkService clueRemarkService;
	// 注入市场活动
	@Autowired
	private ActivityService activityService;
	// 注入线索-市场活动
	@Autowired
	private ClueActivityRelationService clueActivityRelationService;

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
	Object deleteClueByIds(String[] id) {
		// 调用Service
		ReturnObject returnObject = new ReturnObject();
		try {
			int ret = clueService.deleteClueByIds(id);
			if (ret > 0) {
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
			} else {
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
	 * 查询id线索
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
	Object updateClue(Clue clue, HttpSession session) {
		// 获取user
		User user = (User) session.getAttribute(Contants.SESSION_USER);
		// 封装参数
		clue.setEditBy(user.getId());
		clue.setEditTime(DateUtils.formateDateTime(new Date()));
		// 调用service
		ReturnObject returnObject = new ReturnObject();
		try {
			int ret = clueService.updateClue(clue);
			if (ret > 0) {
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
			} else {
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
	public String selectClueForDetailById(String id, HttpServletRequest request) {
		// 调用service
		Clue clue = clueService.queryClueForDetailById(id);
		List<ClueRemark> clueRemarkList = clueRemarkService.queryClueRemarkByClueId(id);
		List<Activity> activityList = activityService.queryActivityForDetailByClueId(id);
		// 保存请求域
		request.setAttribute("clue", clue);
		request.setAttribute("clueRemarkList", clueRemarkList);
		request.setAttribute("activityList", activityList);

		// 返回页面
		return "workbench/clue/detail";
	}

	/**
	 * 关联市场活动1：查询
	 */
	@RequestMapping("/workbench/clue/queryActivityForDetailByNameAndClueId.do")
	public @ResponseBody
	Object queryActivityForDetailByNameAndClueId(String clueId, String activityName) {
		// 获取参数
		Map<String, Object> map = new HashMap<>();
		map.put("clueId", clueId);
		map.put("activityName", activityName);
		// 调用service
		List<Activity> activityList = activityService.queryActivityForDetailByNameAndClueId(map);
		return activityList;
	}

	/**
	 * 关联市场活动2：保存--查询
	 */
	@RequestMapping("/workbench/clue/saveBound.do")
	public @ResponseBody
	Object saveBound(String[] activityId, String clueId) {
		// 封装参数
		ClueActivityRelation clueActivityRelation = null;
		List<ClueActivityRelation> clueActivityRelationList = new ArrayList<>();
		for (String actId:activityId) {
			clueActivityRelation = new ClueActivityRelation();
			clueActivityRelation.setId(UUIDUtils.getUUID());
			clueActivityRelation.setActivityId(actId);
			clueActivityRelation.setClueId(clueId);
			clueActivityRelationList.add(clueActivityRelation);
		}
		// 调用service
		ReturnObject returnObject = new ReturnObject();
		try {
			int ret = clueActivityRelationService.saveCreateClueActivityRelationByList(clueActivityRelationList);
			if (ret>0){
				// 增加成功
				List<Activity> activityList = activityService.queryActivitiesByids(activityId);
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
				returnObject.setRetDate(activityList);
			}else {
				// 失败
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
				returnObject.setMessage("系统繁忙，稍后再试");
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 失败
			returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
			returnObject.setMessage("系统繁忙，稍后再试");
		}

		return returnObject;
	}

	/**
	 * 删除关联市场活动
	 */
	@RequestMapping("/workbench/clue/deleteBound.do")
	public @ResponseBody
	Object deleteBound(ClueActivityRelation relation) {
		//调用service
		ReturnObject returnObject = new ReturnObject();
		try {
			int ret = clueActivityRelationService.deleteClueActivityRelationByClueidAndActivityId(relation);
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
		}
		return returnObject;
	}

	/**
	 * 线索转换1:查询信息加载到模态窗口
	 */
	@RequestMapping("/workbench/clue/toConvert.do")
	public String toConvert(String id,HttpServletRequest request){
		// 调用Service 查询线索
		Clue clue = clueService.queryClueForDetailById(id);
		List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
		// 保存到request
		request.setAttribute("clue",clue);
		request.setAttribute("stageList",stageList);
		return "workbench/clue/convert";
	}
	/**
	 * 线索转换2：根据活动name和线索id，查询关联的市场
	 */
	@RequestMapping("/workbench/clue/convertSearch.do")
	public @ResponseBody Object convertSearch(String activityName,String clueId){
		Map<String,Object> map = new HashMap<>();
		map.put("activityName",activityName);
		map.put("clueId",clueId);
		List<Activity> activityList = activityService.queryActivityForConvertByNameClueId(map);
		return activityList;
	}




}

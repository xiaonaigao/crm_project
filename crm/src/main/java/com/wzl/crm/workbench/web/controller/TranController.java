package com.wzl.crm.workbench.web.controller;

import com.wzl.crm.commons.contants.Contants;
import com.wzl.crm.commons.domain.ReturnObject;
import com.wzl.crm.settings.domain.DicValue;
import com.wzl.crm.settings.service.DicValueService;
import com.wzl.crm.settings.service.UserService;
import com.wzl.crm.workbench.domain.*;
import com.wzl.crm.workbench.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wang
 * @version 1.0
 */
@Controller
public class TranController {
	@Autowired
	private TranService tranService;
	@Autowired
	private DicValueService dicValueService;
	@Autowired
	private UserService userService;
	@Autowired
	private ActivityService activityService;
	@Autowired
	private ContactsService contactsService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private TranRemarkService tranRemarkService;
	@Autowired
	private TranHistoryService tranHistoryService;

	/**
	 * 主页：点击菜单“交易”，跳转到主页
	 */
	@RequestMapping("/workbench/transaction/index.do")
	public String transactionIndex(HttpServletRequest request) {
		// 加载字典
		List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");
		List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
		List<DicValue> transactionTypeList = dicValueService.queryDicValueByTypeCode("transactionType");
		// 保存请求域
		request.setAttribute("sourceList", sourceList);
		request.setAttribute("stageList", stageList);
		request.setAttribute("transactionTypeList", transactionTypeList);
		return "workbench/transaction/index";
	}

	/**
	 * 主页：搜索交易记录列表
	 */
	@RequestMapping("/workbench/transaction/transactionListIndex.do")
	public @ResponseBody
	Object transactionListIndex(int pageNo, int pageSize, String owner, String name, String customerName, String stage, String type, String source, String contactName) {
		// 获取参数
		HashMap<String, Object> map = new HashMap<>();
		map.put("pageNo", (pageNo - 1) * pageSize);
		map.put("pageSize", pageSize);
		map.put("owner", owner);
		map.put("name", name);
		map.put("customerName", customerName);
		map.put("stage", stage);
		map.put("type", type);
		map.put("source", source);
		map.put("contactName", contactName);
		// 调用service
		List<Tran> tranAllList = tranService.queryAllTranDetail(map);
		int totalRows = tranService.queryAllTranCount(map);
		// 返回json
		Map<String, Object> retMap = new HashMap<>();
		retMap.put("tranAllList", tranAllList);
		retMap.put("totalRows", totalRows);
		return retMap;
	}

	/**
	 * 创建交易1：加载下拉框数据，并且跳转到创建页面
	 */
	@RequestMapping("/workbench/transaction/saveIndex.do")
	public String saveIndex(HttpServletRequest request) {
		// 调用service加载下拉数据
		List<User> userList = userService.queryAllUsers();
		List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");
		List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
		List<DicValue> typeList = dicValueService.queryDicValueByTypeCode("transactionType");
		// 保存在请求域中
		request.setAttribute("userList", userList);
		request.setAttribute("sourceList", sourceList);
		request.setAttribute("stageList", stageList);
		request.setAttribute("typeList", typeList);
		return "workbench/transaction/save";

	}

	/**
	 * 创建交易2：查找市场活动
	 */
	@RequestMapping("/workbench/transaction/searchActivity.do")
	public @ResponseBody
	Object searchActivity(String activityName) {
		// 调用service
		List<Activity> activityList = activityService.queryActivityForDetailByName(activityName);
		return activityList;
	}

	/**
	 * 创建交易3：查找联系人
	 */
	@RequestMapping("/workbench/transaction/searchContacts.do")
	public @ResponseBody
	Object searchContacts(String fullname) {
		// 调用service
		List<Contacts> contactsList = contactsService.queryContactsForDetailByName(fullname);
		return contactsList;
	}

	/**
	 * 创建交易4 ：模糊查询客户
	 */
	@RequestMapping("/workbench/transaction/serachCustomer.do")
	public @ResponseBody
	Object serachCustomer(String customerName) {
		return customerService.queryCustomerForNameListByName(customerName);
	}

	/**
	 * 创建交易：点击保存
	 */
	@RequestMapping("/workbench/transaction/saveTran.do")
	public @ResponseBody
	Object saveTran(String owner, String money, String name, String expectedDate, String customerName, String stage, String type, String contactsId, String source, String activityId, String description, String contactSummary, String nextContactTime, HttpSession session) {
		// 封装参数
		Map<String, Object> map = new HashMap<>();
		User user = (User) session.getAttribute(Contants.SESSION_USER);
		map.put("owner", owner);
		map.put("money", money);
		map.put("name", name);
		map.put("expectedDate", expectedDate);
		map.put("customerName", customerName);
		map.put("stage", stage);
		map.put("type", type);
		map.put("contactsId", contactsId);
		map.put("source", source);
		map.put("activityId", activityId);
		map.put("description", description);
		map.put("contactSummary", contactSummary);
		map.put("nextContactTime", nextContactTime);
		map.put(Contants.SESSION_USER, user);
		// 调用service，实现交易添加
		ReturnObject returnObject = new ReturnObject();
		try {
			int ret = tranService.saveCreateTran(map);
			if (ret > 0) {
				//成功
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
			} else {
				//失败
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
				returnObject.setMessage("系统错误，稍后再试");
			}
		} catch (Exception e) {
			e.printStackTrace();
			//失败
			returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
			returnObject.setMessage("系统错误，稍后再试");
		}
		return returnObject;

	}

	/**
	 * 删除交易
	 */
	@RequestMapping("/workbench/transaction/deleteTran.do")
	public @ResponseBody
	Object deleteTran(String[] tranIds) {
		//调用service
		ReturnObject returnObject = new ReturnObject();
		try {
			int ret = tranService.deleteCreateTran(tranIds);
			if (ret > 0) {
				//成功
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
			} else {
				//失败
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
				returnObject.setMessage("系统错误，稍后再试");
			}
		} catch (Exception e) {
			e.printStackTrace();
			//失败
			returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
			returnObject.setMessage("系统错误，稍后再试");
		}
		return returnObject;
	}

	/**
	 * 修改交易·：加载信息
	 */
	@RequestMapping("/workbench/transaction/searchTranForId.do")
	public String searchTranForId(HttpServletRequest request,String tranId){
		// 调用service加载下拉数据
		Tran tran = tranService.queryTranDetailFortranId(tranId);
		Activity activity = activityService.queryActivityForDetail(tran.getActivityId());
		Contacts contacts = contactsService.queryContactsById(tran.getContactsId());
		List<User> userList = userService.queryAllUsers();
		List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");
		List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
		List<DicValue> typeList = dicValueService.queryDicValueByTypeCode("transactionType");
		// 保存在请求域中
		request.setAttribute("userList", userList);
		request.setAttribute("sourceList", sourceList);
		request.setAttribute("stageList", stageList);
		request.setAttribute("typeList", typeList);
		request.setAttribute("tran", tran);
		request.setAttribute("activity", activity);
		request.setAttribute("contacts", contacts);
		return "workbench/transaction/edit";
	}
	/**
	 * 修改交易·：保存信息
	 */
	@RequestMapping("/workbench/transaction/EidtTranForId.do")
	public @ResponseBody Object editTran(HttpSession session,String id,String owner,String money,String name,String expectedDate,String customerName,String stage,String type,String source,String activityId,String contactsId,String description,String contactSummary,String nextContactTime){
		// 封装参数
		Map<String,Object> map = new HashMap<>();
		User user = (User) session.getAttribute(Contants.SESSION_USER);
		map.put("id",id);
		map.put("owner",owner);
		map.put("money",money);
		map.put("name",name);
		map.put("expectedDate",expectedDate);
		map.put("customerName",customerName);
		map.put("stage",stage);
		map.put("type",type);
		map.put("source",source);
		map.put("activityId",activityId);
		map.put("contactsId",contactsId);
		map.put("description",description);
		map.put("contactSummary",contactSummary);
		map.put("nextContactTime",nextContactTime);
		map.put("nextContactTime",nextContactTime);
		map.put(Contants.SESSION_USER,user);
		// 调用service，实现交易修改
		ReturnObject returnObject = new ReturnObject();
		try {
			int ret = tranService.editTranDetailFortranId(map);
			if (ret > 0) {
				//成功
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
			} else {
				//失败
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
				returnObject.setMessage("系统错误，稍后再试");
			}
		} catch (Exception e) {
			e.printStackTrace();
			//失败
			returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
			returnObject.setMessage("系统错误，稍后再试");
		}
		return returnObject;

	}

	/**
	 * 查看交易详情
	 */
	@RequestMapping("/workbench/transaction/queryTranFortranId.do")
	public String queryTranFortranId(HttpServletRequest request,String tranId){
		// 调用Service
		Tran tran = tranService.queryTranFortranId(tranId);
		List<TranRemark> tranRemarkList = tranRemarkService.queryTranHistoryForTranid(tranId);
		List<TranHistory> tranHistoryList = tranHistoryService.queryTranHistoryForTranid(tranId);
		List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
		// 查询阶段所在的order_no
		Map<String,Object> map = new HashMap<>();
		map.put("typeCode","stage");
		map.put("dicValue",tran.getStage());
		int nowStageNo = dicValueService.queryDicToOrderNo(map);
		// 保存请求域
		request.setAttribute("tran",tran);
		request.setAttribute("tranRemarkList",tranRemarkList);
		request.setAttribute("tranHistoryList",tranHistoryList);
		request.setAttribute("nowStageNo",nowStageNo);
		request.setAttribute("stageList",stageList);
		return "workbench/transaction/detail";
	}
}

package com.wzl.crm.workbench.web.controller;

import com.sun.deploy.net.HttpResponse;
import com.wzl.crm.commons.contants.Contants;
import com.wzl.crm.commons.domain.ReturnObject;
import com.wzl.crm.commons.utils.DateUtils;
import com.wzl.crm.commons.utils.HSSFUtils;
import com.wzl.crm.commons.utils.UUIDUtils;
import com.wzl.crm.settings.service.UserService;
import com.wzl.crm.workbench.domain.Activity;
import com.wzl.crm.workbench.domain.ActivityRemark;
import com.wzl.crm.workbench.domain.User;
import com.wzl.crm.workbench.service.ActivityRemarkService;
import com.wzl.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

/**
 * @author wang
 * @version 1.0
 */
@Controller
public class ActivityController {
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
	 * 注入ActivityRemarkService
	 */
	@Autowired
	private ActivityRemarkService activityRemarkService;

	/**
	 * 市场活动首页
	 */
	@RequestMapping("/workbench/activity/index.do")
	public String activityIndex(HttpServletRequest request) {
		//调用service层方法，查询所有用户
		List<User> userList = userService.queryAllUsers();
		//存放request
		request.setAttribute("userList", userList);
		return "workbench/activity/index";
	}

	/**
	 * 创建市场活动
	 */
	@RequestMapping("/workbench/activity/saveCreateActivity.do")
	public @ResponseBody
	Object saveCreateActivity(Activity activity, HttpSession session) {
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
			if (ret > 0) {
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
			} else {
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
	public @ResponseBody
	Object queryActivityByConditionForPage(String name, String owner, String startDate, String endDate, int pageNo, int pageSize) {
		//封装参数
		Map<String, Object> map = new HashMap<>();
		map.put("name", name);
		map.put("owner", owner);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("beginNo", (pageNo - 1) * pageSize);
		map.put("pageSize", pageSize);
		//调用Service
		List<Activity> activityList = activityService.queryActivityByConditionForPage(map);
		int totalRows = activityService.queryCountOfActivityByCondition(map);
		//生成响应信息
		Map<String, Object> retMap = new HashMap<>();
		retMap.put("activityList", activityList);
		retMap.put("totalRows", totalRows);
		return retMap;
	}

	/**
	 * 根据id删除
	 *
	 * @return
	 */
	@RequestMapping("/workbench/activity/deleteActivityIds.do")
	public @ResponseBody
	Object deleteActivityIds(String[] id) {
		ReturnObject returnObject = new ReturnObject();
		try {
			// 调用service方法
			int ret = activityService.deleteActivityByIds(id);
			if (ret > 0) {
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
			} else {
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
	 * 根据id修改--1查询
	 */
	@RequestMapping("/workbench/activity/selectActivityById.do")
	public @ResponseBody
	Object selectActivityById(String id) {
		Activity activity = activityService.queryActivityById(id);
		return activity;
	}

	/**
	 * 根据id修改--2保存
	 */
	@RequestMapping("/workbench/activity/editActivityById.do")
	public @ResponseBody
	Object editActivityById(Activity activity, HttpSession session) {
		//封装参数
		//获取当前时间
		activity.setEditTime(DateUtils.formateDateTime(new Date()));
		//获取session的id
		User user = (User) session.getAttribute(Contants.SESSION_USER);
		activity.setEditBy(user.getId());
		//调用Service
		ReturnObject returnObject = new ReturnObject();
		try {
			int ret = activityService.editActivityById(activity);
			if (ret > 0) {
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
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
	 * 批量导出
	 */
	@RequestMapping("/workbench/activity/exportAllActivities.do")
	public void exportAllActivities(HttpServletResponse response) throws Exception {
		// 1 调用service方法，查询所有的市场活动
		List<Activity> activityList = activityService.queryAllActivities();
		// 2.文件下载
		HSSFUtils.createExcelByActivityList(activityList, Contants.FILE_NAME_ACTIVITY, response);
	}

	/**
	 * 选择导出
	 */
	@RequestMapping("/workbench/activity/exportSelectActivities.do")
	public void exportSelectActivities(String[] id, HttpServletResponse response) throws Exception {
		// 1.调用service
		List<Activity> activityList = activityService.queryActivitiesByids(id);
		// 2.文件下载
		HSSFUtils.createExcelByActivityList(activityList, Contants.FILE_NAME_ACTIVITY, response);
	}

	/**
	 * 批量导入
	 */
	@RequestMapping("/workbench/activity/importActivities.do")
	public @ResponseBody
	Object importActivities(MultipartFile activityFile, HttpSession session) {
		// 1 通过session获取user
		User user = (User) session.getAttribute(Contants.SESSION_USER);
		// 2 创建返回的Object
		ReturnObject returnObject = new ReturnObject();
		// 3 读取文件
		try {
			// 3.1 获取文件输入流
			InputStream is = activityFile.getInputStream();
			// 3.1 创建文件
			HSSFWorkbook wb = new HSSFWorkbook(is);
			// 3.2 获取excel表第一页的信息
			HSSFSheet sheet = wb.getSheetAt(0);
			// 3.3 创建行、列、接收的对象、接收的集合
			HSSFRow row = null;
			HSSFCell cell = null;
			Activity activity = null;
			List<Activity> activityList = new ArrayList<>();
			// 3.4 通过循环读取文件内容
			// 3.4.1 行数
			for (int i = 1; i < sheet.getLastRowNum(); i++) {
				// 3.4.2 读取此行
				row = sheet.getRow(i);
				// 3.4.3 创建activity对象
				activity = new Activity();
				activity.setId(UUIDUtils.getUUID());
				activity.setOwner(user.getId());
				activity.setCreateTime(DateUtils.formateDateTime(new Date()));
				activity.setCreateBy(user.getId());
				// 3.4.3 读取此行的所有列
				for (int j = 0; j < row.getLastCellNum(); j++) {
					// 3.4.4 读取此行的列的值
					cell = row.getCell(j);
					String cellValue = HSSFUtils.getCellValueForStr(cell);
					if (j == 0) {
						activity.setName(cellValue);
					} else if (j == 1) {
						activity.setStartDate(cellValue);
					} else if (j == 2) {
						activity.setEndDate(cellValue);
					} else if (j == 3) {
						activity.setCost(cellValue);
					} else {
						activity.setDescription(cellValue);
					}

				}
				activityList.add(activity);
			}
			// 4 调用service
			int ret = activityService.saveCreateActivityByList(activityList);
			if (ret > 0) {
				// 插入成功
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
				returnObject.setRetDate(ret);

			} else {
				// 插入失败
				returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
				returnObject.setMessage("系统繁忙，稍后再试");
			}


		} catch (IOException e) {
			e.printStackTrace();
			// 插入失败
			returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
			returnObject.setMessage("系统繁忙，稍后再试");
		}
		return returnObject;
	}

	/**
	 * 查看市场活动的详情页面
	 */
	@RequestMapping("/workbench/activity/ActivityDetail.do")
	public String ActivityDetail(String id,HttpServletRequest request){
		// 调用activityService获取市场活动详细
		Activity activity = activityService.queryActivityForDetail(id);
		// 调用activityRemarkService,获取评论信息
		List<ActivityRemark> activityRemarksList = activityRemarkService.queryActivityRemarkByActId(id);
		// 保存在请求域
		request.setAttribute("activity",activity);
		request.setAttribute("activityRemarksList",activityRemarksList);
		return "workbench/activity/detail";
	}

}

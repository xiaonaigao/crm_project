package com.wzl.crm.workbench.service;

import com.wzl.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

/**
 * @author wang
 * @version 1.0
 */
public interface ActivityService {
	/**
	 * 保存市场活动
	 */
	 int saveCreateActivity(Activity activity);
	/**
	 * 分页查询市场活动
	 */
	List<Activity> queryActivityByConditionForPage(Map<String,Object> map);

	/**
	 * 符合市场活动的条数
	 */
	int queryCountOfActivityByCondition(Map<String,Object> map);
}

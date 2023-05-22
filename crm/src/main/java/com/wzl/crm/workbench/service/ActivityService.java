package com.wzl.crm.workbench.service;

import com.wzl.crm.workbench.domain.Activity;
import com.wzl.crm.workbench.domain.FunnelVO;

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

	/**
	 * 根据id删除
	 */
	int deleteActivityByIds(String[] id);

	/**
	 * 根据id查询市场活动
	 */
	Activity queryActivityById(String id);

	/**
	 * 根据id修改市场活动
	 */
	int editActivityById(Activity activity);

	/**
	 * 批量导出
	 */
	List<Activity> queryAllActivities();
	/**
	 * 选择导出
	 */
	List<Activity> queryActivitiesByids(String[] id);
	/**
	 * 批量导入
	 */
	int saveCreateActivityByList(List<Activity>activityList);

	/**
	 * 点击名称，查看市场详细
	 */
	Activity queryActivityForDetail(String id);
	/**
	 * 关联市场活动1：根据线索id,查询该线索下关联的市场活动
	 */
	List<Activity> queryActivityForDetailByClueId(String clueId);
	/**
	 * 关联市场活动2：根据线索id,活动id输入的名称的 市场活动
	 */
	List<Activity> queryActivityForDetailByNameAndClueId(Map<String,Object> map);

	/**
	 * 线索转换：根据活动name和线索id，查询关联的市场
	 */
	List<Activity> queryActivityForConvertByNameClueId(Map<String,Object> map);

	/**
	 * 市场活动：市场活动名称查询
	 */
	List<Activity> queryActivityForDetailByName(String activityName);

	/**
	 * 市场活动图标统计
	 * @return
	 */

	List<FunnelVO> queryCountOfActivityGroupByStage();
}

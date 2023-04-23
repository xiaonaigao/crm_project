package com.wzl.crm.workbench.service;

import com.wzl.crm.workbench.domain.ActivityRemark;

import java.util.List;

/**
 * @author wang
 * @version 1.0
 */
public interface ActivityRemarkService {
	/**
	 * 查看市场活动备注
	 */
	List<ActivityRemark> queryActivityRemarkByActId(String activityId);

	/**
	 * 增加市场活动备注
	 */
	int saveCreateActivityRemark(ActivityRemark activityRemark);

	/**
	 * 删除市场活动备注
	 */
	int deleteActivityRemarkById(String id);

	/**
	 * 修改市场活动备注
	 */
	int saveEditActivityRemark(ActivityRemark remark);
}

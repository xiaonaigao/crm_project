package com.wzl.crm.workbench.service.impl;

import com.wzl.crm.workbench.domain.ActivityRemark;
import com.wzl.crm.workbench.mapper.ActivityRemarkMapper;
import com.wzl.crm.workbench.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wang
 * @version 1.0
 */
@Service("activityRemarkService")
public class ActivityRemarkServiceImpl implements ActivityRemarkService {
	@Autowired
	private ActivityRemarkMapper activityRemarkMapper;

	@Override
	public List<ActivityRemark> queryActivityRemarkByActId(String activityId) {
		return activityRemarkMapper.selectActivityRemarkByActId(activityId);
	}

	@Override
	public int saveCreateActivityRemark(ActivityRemark remark) {
		return activityRemarkMapper.insertActivityRemark(remark);
	}

	@Override
	public int deleteActivityRemarkById(String id) {
		return activityRemarkMapper.deleteActivityRemarkById(id);
	}

	@Override
	public int saveEditActivityRemark(ActivityRemark remark) {
		return activityRemarkMapper.updateActivityRemark(remark);
	}
}

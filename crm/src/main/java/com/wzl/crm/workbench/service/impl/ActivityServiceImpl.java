package com.wzl.crm.workbench.service.impl;

import com.wzl.crm.workbench.domain.Activity;
import com.wzl.crm.workbench.mapper.ActivityMapper;
import com.wzl.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author wang
 * @version 1.0
 */
@Service("activityService")
public class ActivityServiceImpl implements ActivityService {
	@Autowired
	private ActivityMapper activityMapper;
	@Override
	public int saveCreateActivity(Activity activity) {
		return activityMapper.insertActivity(activity);
	}

	@Override
	public List<Activity> queryActivityByConditionForPage(Map<String, Object> map) {
		return activityMapper.selectActivityByConditionForPage(map);
	}

	@Override
	public int queryCountOfActivityByCondition(Map<String, Object> map) {
		return activityMapper.selectCountOfActivityByCondition(map);
	}

	@Override
	public int deleteActivityByIds(String[] id) {
		return activityMapper.deleteActivityByIds(id);
	}

	@Override
	public Activity queryActivityById(String id) {
		return activityMapper.selectActivityById(id);
	}

	@Override
	public int editActivityById(Activity activity) {
		return activityMapper.updateActivity(activity);
	}

	@Override
	public List<Activity> queryAllActivities() {
		return activityMapper.selectAllActivities();
	}

	@Override
	public List<Activity> queryActivitiesByids(String[] id) {
		return activityMapper.selectActivitiesByids(id);
	}

	@Override
	public int saveCreateActivityByList(List<Activity> activityList) {
		return activityMapper.insertActivityByList(activityList);
	}

	@Override
	public Activity queryActivityForDetail(String id) {
		return activityMapper.selectActivityForDetailById(id);
	}

	@Override
	public List<Activity> queryActivityForDetailByClueId(String clueId) {
		return activityMapper.selectActivityForDetailByClueId(clueId);
	}

	@Override
	public List<Activity> queryActivityForDetailByNameAndClueId(Map<String, Object> map) {
		return activityMapper.selectActivityForDetailByNameAndClueId(map);
	}

	@Override
	public List<Activity> queryActivityForConvertByNameClueId(Map<String, Object> map) {
		return activityMapper.selectActivityForConvertByNameClueId(map);
	}

}

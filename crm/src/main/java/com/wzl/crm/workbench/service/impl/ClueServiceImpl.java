package com.wzl.crm.workbench.service.impl;

import com.wzl.crm.workbench.domain.Activity;
import com.wzl.crm.workbench.domain.Clue;
import com.wzl.crm.workbench.mapper.ActivityMapper;
import com.wzl.crm.workbench.mapper.ClueMapper;
import com.wzl.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wang
 * @version 1.0
 */
@Service("clueService")
public class ClueServiceImpl implements ClueService {
	@Autowired
	private ClueMapper clueMapper;
	@Autowired
	private ActivityMapper activityMapper;
	@Override
	public int saveCreateClue(Clue clue) {
		return clueMapper.insertClue(clue);
	}

	@Override
	public List<Clue> queryClueByConditionForPage(Map<String, Object> map) {
		return clueMapper.selectClueByConditionForPage(map);
	}

	@Override
	public int queryCountOfClueByCondition(Map<String, Object> map) {
		return clueMapper.selectCountOfClueByCondition(map);
	}

	@Override
	public int deleteClueByIds(String[] id) {
		return clueMapper.deleteClueByIds(id);
	}

	@Override
	public Clue queryClueById(String id) {
		return clueMapper.selectClueById(id);
	}

	@Override
	public int updateClue(Clue clue) {
		return clueMapper.updateClue(clue);
	}

	@Override
	public Clue queryClueForDetailById(String id) {
		return clueMapper.selectClueForDetailById(id);
	}

	@Override
	public int saveConvertClue(Map<String, Object> map) {
		// 获取参数
		String id = (String) map.get("id");
		// 根据线索id
		Activity activity = activityMapper.selectActivityById(id);

		return 0;
	}

}

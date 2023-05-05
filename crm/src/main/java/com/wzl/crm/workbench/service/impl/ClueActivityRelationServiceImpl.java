package com.wzl.crm.workbench.service.impl;

import com.wzl.crm.workbench.domain.ClueActivityRelation;
import com.wzl.crm.workbench.mapper.ClueActivityRelationMapper;
import com.wzl.crm.workbench.service.ClueActivityRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wang
 * @version 1.0
 */
@Service
public class ClueActivityRelationServiceImpl implements ClueActivityRelationService {
	@Autowired
	private ClueActivityRelationMapper clueActivityRelationMapper;
	@Override
	public int saveCreateClueActivityRelationByList(List<ClueActivityRelation> clueActivityRelationList) {
		return clueActivityRelationMapper.insertCreateClueActivityRelationByList(clueActivityRelationList);
	}

	@Override
	public int deleteClueActivityRelationByClueidAndActivityId(ClueActivityRelation relation) {
		return clueActivityRelationMapper.deleteClueActivityRelationByClueidAndActivityId(relation);
	}
}

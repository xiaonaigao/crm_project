package com.wzl.crm.workbench.service;

import com.wzl.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

/**
 * @author wang
 * @version 1.0
 */
public interface ClueActivityRelationService {
	/**
	 * 保存 线索关联市场活动
	 */
	int saveCreateClueActivityRelationByList(List<ClueActivityRelation> clueActivityRelationList);
	/**
	 * 删除 线索关联市场活动
	 */
	int deleteClueActivityRelationByClueidAndActivityId(ClueActivityRelation relation);
}

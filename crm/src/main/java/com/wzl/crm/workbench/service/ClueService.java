package com.wzl.crm.workbench.service;

import com.wzl.crm.workbench.domain.Clue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wang
 * @version 1.0
 */
public interface ClueService {
	/**
	 * 创建线索
	 */
	int saveCreateClue(Clue clue);
	/**
	 * 查看线索
	 */
	List<Clue> queryClueByConditionForPage(Map<String,Object> map);
	/**
	 * 线索的数量
	 */
	int queryCountOfClueByCondition(Map<String,Object> map);
	/**
	 * 删除线索
	 */
	int deleteClueByIds(String[] id);
	/**
	 * 根据id查询线索
	 */
	Clue queryClueById(String id);
	/**
	 * 修改线索
	 */
	int updateClue(Clue clue);

	/**
	 * 线索详情1：线索明细
	 */
	Clue queryClueForDetailById(String id);

	/**
	 * 线索转换
	 */
	int saveConvertClue(Map<String,Object>map);
}

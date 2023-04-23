package com.wzl.crm.workbench.service;

import com.wzl.crm.workbench.domain.ClueRemark;

import java.util.List;

/**
 * @author wang
 * @version 1.0
 */
public interface ClueRemarkService {
	/**
	 * 根据id查询备注
	 */
	List<ClueRemark> queryClueRemarkByClueId(String id);

	/**
	 * 创建线索评论
	 */
	int saveCreateClueRemark(ClueRemark clueRemark);

	/**
	 * 删除线索评论
	 */
	int deleteClueRemarkById(String id);

	/**
	 * 更新线索评论
	 */
	int editClueRemarkById(ClueRemark clueRemark);
}

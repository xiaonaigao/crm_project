package com.wzl.crm.workbench.service;

import com.wzl.crm.workbench.domain.TranRemark;

import java.util.List;

/**
 * @author wang
 * @version 1.0
 */
public interface TranRemarkService {
	/**
	 * 根据交易id查询交易评论
	 */
	List<TranRemark> queryTranHistoryForTranid(String tranId);

	/**
	 * 添加交易备注
	 */
	int  saveTranRemarkById(TranRemark tranRemark);

	/**
	 * 删除交易备注
	 */
	int deleteTranRemarkById(String id);

	/**
	 * 修改交易备注
	 */
	int  editTranRemarkById(TranRemark tranRemark);
}

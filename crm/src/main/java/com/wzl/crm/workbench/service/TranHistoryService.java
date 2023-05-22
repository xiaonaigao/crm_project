package com.wzl.crm.workbench.service;

import com.wzl.crm.workbench.domain.TranHistory;

import java.util.List;

/**
 * @author wang
 * @version 1.0
 */
public interface TranHistoryService {
	/**
	 * 根据交易id查询阶段历史
	 */
	List<TranHistory> queryTranHistoryForTranid(String tranId);
}

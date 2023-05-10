package com.wzl.crm.workbench.service;

import com.wzl.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

/**
 * @author wang
 * @version 1.0
 */
public interface TranService {
	/**
	 * 主页：线索明细
	 */
	List<Tran> queryAllTranDetail(Map<String,Object> map);
	/**
	 * 线索的全部数量
	 */
	int queryAllTranCount(Map<String,Object> map);
}

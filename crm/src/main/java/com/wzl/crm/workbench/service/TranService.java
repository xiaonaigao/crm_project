package com.wzl.crm.workbench.service;

import com.wzl.crm.workbench.domain.FunnelVO;
import com.wzl.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

/**
 * @author wang
 * @version 1.0
 */
public interface TranService {
	/**
	 * 主页：交易明细
	 */
	List<Tran> queryAllTranDetail(Map<String, Object> map);

	/**
	 * 交易的全部数量
	 */
	int queryAllTranCount(Map<String, Object> map);

	/**
	 * 交易:增加
	 */
	int saveCreateTran(Map<String, Object> map);

	/**
	 * 交易:删除
	 */
	int deleteCreateTran(String[] tranIds);

	/**
	 * 交易:根据id查询
	 */
	Tran queryTranDetailFortranId(String tranId);

	/**
	 * 交易：根据id修改
	 */
	int editTranDetailFortranId(Map<String,Object>map);

	/**
	 * 详细信息：根据id
	 */
	Tran queryTranFortranId(String tranId);

	/**
	 * 交易图标信息
	 * @return
	 */

	List<FunnelVO> queryCountOfTranGroupByStage();
}

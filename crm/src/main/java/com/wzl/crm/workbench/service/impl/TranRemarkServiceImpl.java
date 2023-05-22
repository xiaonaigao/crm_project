package com.wzl.crm.workbench.service.impl;

import com.wzl.crm.workbench.domain.TranRemark;
import com.wzl.crm.workbench.mapper.TranRemarkMapper;
import com.wzl.crm.workbench.service.TranRemarkService;
import com.wzl.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wang
 * @version 1.0
 */
@Service
public class TranRemarkServiceImpl implements TranRemarkService {
	@Autowired
	private TranRemarkMapper tranRemarkMapper;
	@Override
	public List<TranRemark> queryTranHistoryForTranid(String tranId) {
		return tranRemarkMapper.selectTranHistoryForTranid(tranId);
	}

	@Override
	public int saveTranRemarkById(TranRemark tranRemark) {
		return tranRemarkMapper.insertTranRemark(tranRemark);
	}

	@Override
	public int deleteTranRemarkById(String id) {
		return tranRemarkMapper.deleteTranRemarkById(id);
	}

	@Override
	public int editTranRemarkById(TranRemark tranRemark) {
		return tranRemarkMapper.updateTranRemarkById(tranRemark);
	}
}

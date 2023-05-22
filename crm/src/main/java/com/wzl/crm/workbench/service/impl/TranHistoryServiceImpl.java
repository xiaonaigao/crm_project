package com.wzl.crm.workbench.service.impl;

import com.wzl.crm.workbench.domain.TranHistory;
import com.wzl.crm.workbench.mapper.TranHistoryMapper;
import com.wzl.crm.workbench.service.TranHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wang
 * @version 1.0
 */
@Service
public class TranHistoryServiceImpl implements TranHistoryService {
	@Autowired
	private TranHistoryMapper tranHistoryMapper;
	@Override
	public List<TranHistory> queryTranHistoryForTranid(String tranId) {
		return tranHistoryMapper.selectTranHistoryForTranid(tranId);
	}
}

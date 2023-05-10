package com.wzl.crm.workbench.service.impl;

import com.wzl.crm.workbench.domain.Tran;
import com.wzl.crm.workbench.mapper.TranMapper;
import com.wzl.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author wang
 * @version 1.0
 */
@Service
public class TranServiceImpl implements TranService {
	@Autowired
	private TranMapper tranMapper;
	@Override
	public List<Tran> queryAllTranDetail(Map<String, Object> map) {
		return tranMapper.selectAllTranDetail(map);
	}

	@Override
	public int queryAllTranCount(Map<String,Object> map) {
		return tranMapper.selectAllTranCount(map);
	}
}

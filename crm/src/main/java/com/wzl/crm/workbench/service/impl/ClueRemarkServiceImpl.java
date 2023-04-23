package com.wzl.crm.workbench.service.impl;

import com.wzl.crm.workbench.domain.ClueRemark;
import com.wzl.crm.workbench.mapper.ClueRemarkMapper;
import com.wzl.crm.workbench.service.ClueRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wang
 * @version 1.0
 */
@Service
public class ClueRemarkServiceImpl implements ClueRemarkService {
	@Autowired
	private ClueRemarkMapper clueRemarkMapper;

	@Override
	public List<ClueRemark> queryClueRemarkByClueId(String id) {
		return clueRemarkMapper.selectClueRemarkByClueId(id);
	}

	@Override
	public int saveCreateClueRemark(ClueRemark clueRemark) {
		return clueRemarkMapper.insertClueRemark(clueRemark);
	}

	@Override
	public int deleteClueRemarkById(String id) {
		return clueRemarkMapper.deleteClueRemarkById(id);
	}

	@Override
	public int editClueRemarkById(ClueRemark clueRemark) {
		return clueRemarkMapper.updateClueRemarkById(clueRemark);
	}
}

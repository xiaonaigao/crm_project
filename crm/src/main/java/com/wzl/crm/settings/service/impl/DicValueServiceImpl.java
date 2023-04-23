package com.wzl.crm.settings.service.impl;

import com.wzl.crm.settings.domain.DicValue;
import com.wzl.crm.settings.mapper.DicValueMapper;
import com.wzl.crm.settings.service.DicValueService;
import com.wzl.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wang
 * @version 1.0
 */
@Service("dicValueService")
public class DicValueServiceImpl implements DicValueService {
	@Autowired
	private DicValueMapper dicValueMapper;
	@Override
	public List<DicValue> queryDicValueByTypeCode(String typeCode) {
		return dicValueMapper.selectDicValueByTypeCode(typeCode);
	}
}

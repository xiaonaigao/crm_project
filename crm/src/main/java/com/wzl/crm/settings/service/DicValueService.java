package com.wzl.crm.settings.service;

import com.wzl.crm.settings.domain.DicValue;

import java.util.List;

/**
 * @author wang
 * @version 1.0
 */
public interface DicValueService {
	/**
	 * 根据typecode查询字典
	 */
	List<DicValue> queryDicValueByTypeCode(String typeCode);
}

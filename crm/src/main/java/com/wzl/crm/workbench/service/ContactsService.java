package com.wzl.crm.workbench.service;

import com.wzl.crm.workbench.domain.Contacts;
import com.wzl.crm.workbench.domain.FunnelVO;

import java.util.List;

/**
 * @author wang
 * @version 1.0
 */
public interface ContactsService {
	/**
	 * 联系人信息：通过用户名查找
	 */
	List<Contacts> queryContactsForDetailByName(String fullname);
	/**根据主键查询信息
	 */
	Contacts queryContactsById(String id);

	/**
	 * 客户图表
	 * @return
	 */

	List<FunnelVO> queryCountOfCustomerAndContactsGroupByCustomer();
}

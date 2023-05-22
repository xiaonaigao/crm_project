package com.wzl.crm.workbench.service.impl;

import com.wzl.crm.workbench.domain.Contacts;
import com.wzl.crm.workbench.domain.FunnelVO;
import com.wzl.crm.workbench.mapper.ContactsMapper;
import com.wzl.crm.workbench.service.ContactsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wang
 * @version 1.0
 */
@Service
public class ContactsServiceImpl implements ContactsService {
	@Autowired
	private ContactsMapper contactsMapper;
	@Override
	public List<Contacts> queryContactsForDetailByName(String fullname) {
		return contactsMapper.selectContactsForDetailByName(fullname);
	}

	@Override
	public Contacts queryContactsById(String id) {
		return contactsMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<FunnelVO> queryCountOfCustomerAndContactsGroupByCustomer() {
		return contactsMapper.selectCountOfCustomerAndContactsGroupByCustomer();
	}
}

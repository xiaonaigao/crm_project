package com.wzl.crm.workbench.service.impl;

import com.wzl.crm.workbench.domain.Customer;
import com.wzl.crm.workbench.mapper.CustomerMapper;
import com.wzl.crm.workbench.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wang
 * @version 1.0
 */
@Service
public class CustomerServiceImpl implements CustomerService {
	@Autowired
	private CustomerMapper customerMapper;
	@Override
	public String queryCustomerForNameToCustomerId(String customerName) {
		return customerMapper.selectCustomerForNameToCustomerId(customerName);
	}

	@Override
	public void saveCustomerByTran(Customer customer) {
		customerMapper.insertCustomer(customer);
	}

	@Override
	public List<String> queryCustomerForNameListByName(String customerName) {
		return customerMapper.selectCustomerForNameListlByName(customerName);
	}

}

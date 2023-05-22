package com.wzl.crm.workbench.service;

import com.wzl.crm.workbench.domain.Customer;

import java.util.List;

/**
 * @author wang
 * @version 1.0
 */
public interface CustomerService {
	/**
	 * 客户信息：根据name查询
	 */
	String queryCustomerForNameToCustomerId(String customerName);

	/**
	 * 通过交易保存客户信息
	 * @param customer
	 */
	void saveCustomerByTran(Customer customer);

	/**
	 * 通过名称 查询客户信息
	 */
	List<String> queryCustomerForNameListByName(String customerName);
}

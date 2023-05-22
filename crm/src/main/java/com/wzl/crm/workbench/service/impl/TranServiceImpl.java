package com.wzl.crm.workbench.service.impl;

import com.wzl.crm.commons.contants.Contants;
import com.wzl.crm.commons.utils.DateUtils;
import com.wzl.crm.commons.utils.UUIDUtils;
import com.wzl.crm.workbench.domain.*;
import com.wzl.crm.workbench.mapper.CustomerMapper;
import com.wzl.crm.workbench.mapper.TranHistoryMapper;
import com.wzl.crm.workbench.mapper.TranMapper;
import com.wzl.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
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
	@Autowired
	private CustomerMapper customerMapper;
	@Autowired
	private TranHistoryMapper tranHistoryMapper;

	@Override
	public List<Tran> queryAllTranDetail(Map<String, Object> map) {
		return tranMapper.selectAllTranDetail(map);
	}

	@Override
	public int queryAllTranCount(Map<String, Object> map) {
		return tranMapper.selectAllTranCount(map);
	}

	@Override
	public int saveCreateTran(Map<String, Object> map) {
		Tran tran = new Tran();
		User user = (User) map.get(Contants.SESSION_USER);
		tran.setId(UUIDUtils.getUUID());
		tran.setOwner((String) map.get("owner"));
		tran.setMoney((String) map.get("money"));
		tran.setName((String) map.get("name"));
		tran.setExpectedDate((String) map.get("expectedDate"));
		tran.setStage((String) map.get("stage"));
		tran.setType((String) map.get("type"));
		tran.setSource((String) map.get("source"));
		tran.setActivityId((String) map.get("activityId"));
		tran.setContactsId((String) map.get("contactsId"));
		tran.setCreateBy(user.getId());
		tran.setCreateTime(DateUtils.formateDateTime(new Date()));
		tran.setDescription((String) map.get("description"));
		tran.setContactSummary((String) map.get("contactSummary"));
		tran.setNextContactTime((String) map.get("nextContactTime"));
		// 查找setCustomerId
		String customerId = customerMapper.selectCustomerForNameToCustomerId((String) map.get("customerName"));
		if (customerId == null || "".equals(customerId)) {
			// 没有此客户，需要添加
			Customer customer = new Customer();
			customer.setId(UUIDUtils.getUUID());
			customer.setName((String) map.get("customerName"));
			customer.setCreateBy(user.getId());
			customer.setCreateTime(DateUtils.formateDateTime(new Date()));
			customer.setContactSummary(tran.getContactSummary());
			customer.setNextContactTime(tran.getNextContactTime());
			customer.setDescription(tran.getDescription());
			// 保存客户
			customerMapper.insertCustomer(customer);
			tran.setCustomerId(customer.getId());
		} else {
			// 有这个客户 直接存放查询出的id
			tran.setCustomerId(customerId);
		}
		// 创建交易历史
		TranHistory tranHistory = new TranHistory();
		tranHistory.setId(UUIDUtils.getUUID());
		tranHistory.setStage(tran.getStage());
		tranHistory.setMoney(tran.getMoney());
		tranHistory.setExpectedDate(tran.getExpectedDate());
		tranHistory.setCreateTime(tran.getCreateTime());
		tranHistory.setCreateBy(tran.getCreateBy());
		tranHistory.setTranId(tran.getId());
		tranHistoryMapper.insert(tranHistory);
		return tranMapper.insertCreateTran(tran);
	}

	@Override
	public int deleteCreateTran(String[] tranIds) {
		return tranMapper.deleteCreateTran(tranIds);
	}

	@Override
	public Tran queryTranDetailFortranId(String tranId) {
		return tranMapper.selectTranDetailFortranId(tranId);
	}

	@Override
	public int editTranDetailFortranId(Map<String, Object> map) {
		Tran tran = new Tran();
		User user = (User) map.get(Contants.SESSION_USER);
		tran.setId((String) map.get("id"));
		tran.setOwner((String) map.get("owner"));
		tran.setMoney((String) map.get("money"));
		tran.setName((String) map.get("name"));
		tran.setExpectedDate((String) map.get("expectedDate"));
		tran.setStage((String) map.get("stage"));
		tran.setType((String) map.get("type"));
		tran.setSource((String) map.get("source"));
		tran.setActivityId((String) map.get("activityId"));
		tran.setContactsId((String) map.get("contactsId"));
		tran.setEditBy(user.getId());
		tran.setEditTime(DateUtils.formateDateTime(new Date()));
		tran.setDescription((String) map.get("description"));
		tran.setContactSummary((String) map.get("contactSummary"));
		tran.setNextContactTime((String) map.get("nextContactTime"));
		// 查找setCustomerId
		String customerId = customerMapper.selectCustomerForNameToCustomerId((String) map.get("customerName"));
		if (customerId == null || "".equals(customerId)) {
			// 没有此客户，需要添加
			Customer customer = new Customer();
			customer.setId(UUIDUtils.getUUID());
			customer.setName((String) map.get("customerName"));
			customer.setCreateBy(user.getId());
			customer.setCreateTime(DateUtils.formateDateTime(new Date()));
			customer.setContactSummary(tran.getContactSummary());
			customer.setNextContactTime(tran.getNextContactTime());
			customer.setDescription(tran.getDescription());
			// 保存客户
			customerMapper.insertCustomer(customer);
			tran.setCustomerId(customer.getId());
		} else {
			// 有这个客户 直接存放查询出的id
			tran.setCustomerId(customerId);
		}
		// 查询阶段是否存在
		Map<String, Object> tranHistoryMap = new HashMap<>();
		tranHistoryMap.put("stage", tran.getStage());
		tranHistoryMap.put("tranId", tran.getId());
		TranHistory retTran = tranHistoryMapper.selectTranHistoryByStageAndTranId(tranHistoryMap);
		// 此阶段不存在，那么创建记录
		if (retTran == null || "".equals(retTran)) {
			TranHistory tranHistory = new TranHistory();
			tranHistory.setId(UUIDUtils.getUUID());
			tranHistory.setStage(tran.getStage());
			tranHistory.setMoney(tran.getMoney());
			tranHistory.setExpectedDate(tran.getExpectedDate());
			tranHistory.setCreateTime(DateUtils.formateDateTime(new Date()));
			tranHistory.setCreateBy(user.getId());
			tranHistory.setTranId(tran.getId());
			tranHistoryMapper.insert(tranHistory);
		}
		return tranMapper.updateTranDetailFortranId(tran);
	}

	@Override
	public Tran queryTranFortranId(String tranId) {
		return tranMapper.selectTranFortranId(tranId);
	}

	@Override
	public List<FunnelVO> queryCountOfTranGroupByStage() {
		return tranMapper.selectCountOfTranGroupByStage();
	}
}

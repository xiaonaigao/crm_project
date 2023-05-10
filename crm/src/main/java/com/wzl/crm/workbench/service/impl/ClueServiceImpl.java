package com.wzl.crm.workbench.service.impl;

import com.wzl.crm.commons.contants.Contants;
import com.wzl.crm.commons.utils.DateUtils;
import com.wzl.crm.commons.utils.UUIDUtils;
import com.wzl.crm.workbench.domain.*;
import com.wzl.crm.workbench.mapper.*;
import com.wzl.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author wang
 * @version 1.0
 */
@Service("clueService")
public class ClueServiceImpl implements ClueService {
	@Autowired
	private ClueMapper clueMapper;
	@Autowired
	private ActivityMapper activityMapper;
	@Autowired
	private CustomerMapper customerMapper;
	@Autowired
	private ContactsMapper contactsMapper;
	@Autowired
	private ClueRemarkMapper clueRemarkMapper;
	@Autowired
	private CustomerRemarkMapper customerRemarkMapper;
	@Autowired
	private ContactsRemarkMapper contactsRemarkMapper;
	@Autowired
	private ClueActivityRelationMapper clueActivityRelationMapper;
	@Autowired
	private ContactsActivityRelationMapper contactsActivityRelationMapper;
	@Autowired
	private TranMapper tranMapper;
	@Autowired
	private TranRemarkMapper tranRemarkMapper;

	@Override
	public int saveCreateClue(Clue clue) {
		return clueMapper.insertClue(clue);
	}

	@Override
	public List<Clue> queryClueByConditionForPage(Map<String, Object> map) {
		return clueMapper.selectClueByConditionForPage(map);
	}

	@Override
	public int queryCountOfClueByCondition(Map<String, Object> map) {
		return clueMapper.selectCountOfClueByCondition(map);
	}

	@Override
	public int deleteClueByIds(String[] id) {
		return clueMapper.deleteClueByIds(id);
	}

	@Override
	public Clue queryClueById(String id) {
		return clueMapper.selectClueById(id);
	}

	@Override
	public int updateClue(Clue clue) {
		return clueMapper.updateClue(clue);
	}

	@Override
	public Clue queryClueForDetailById(String id) {
		return clueMapper.selectClueForDetailById(id);
	}

	/**
	 * 线索转换功能
	 */
	@Override
	public void saveConvertClue(Map<String, Object> map) {
		// 获取参数
		String clueId = (String) map.get("clueId");
		User user = (User) map.get(Contants.SESSION_USER);
		String isCreateTran = (String) map.get("isCreateTran");
		// 根据线索id查询信息
		Clue clue = clueMapper.selectClueById(clueId);

		// 1.线索：公司信息--客户表
		Customer customer = new Customer();
		customer.setId(UUIDUtils.getUUID());
		customer.setOwner(clue.getOwner());
		customer.setName(clue.getCompany());
		customer.setWebsite(clue.getWebsite());
		customer.setPhone(clue.getPhone());
		customer.setCreateBy(user.getId());
		customer.setCreateTime(DateUtils.formateDateTime(new Date()));
		customer.setContactSummary(clue.getContactSummary());
		customer.setNextContactTime(clue.getNextContactTime());
		customer.setDescription(clue.getDescription());
		customer.setAddress(clue.getAddress());
		customerMapper.insertCustomer(customer);

		//2.线索：个人信息--联系人表
		Contacts contacts = new Contacts();
		contacts.setId(UUIDUtils.getUUID());
		contacts.setOwner(clue.getOwner());
		contacts.setSource(clue.getSource());
		contacts.setCustomerId(customer.getId());
		contacts.setFullname(clue.getFullname());
		contacts.setAppellation(clue.getAppellation());
		contacts.setEmail(clue.getEmail());
		contacts.setMphone(clue.getMphone());
		contacts.setJob(clue.getJob());
		contacts.setCreateBy(user.getId());
		contacts.setCreateTime(DateUtils.formateDateTime(new Date()));
		contacts.setDescription(clue.getDescription());
		contacts.setContactSummary(clue.getContactSummary());
		contacts.setNextContactTime(clue.getNextContactTime());
		contacts.setAddress(clue.getAddress());
		contactsMapper.insertContacts(contacts);

		//3.线索评论--客户评论和联系人评论
		List<ClueRemark> clueRemarkList = clueRemarkMapper.selectInitialClueRemarkByClueId(clueId);
		// 如果线索不是空的
		if (clueRemarkList != null && clueRemarkList.size() > 0) {
			// 遍历线索备注
			CustomerRemark customerRemark = null;
			List<CustomerRemark> customerRemarkList = new ArrayList<>();
			ContactsRemark contactsRemark = null;
			List<ContactsRemark> contactsRemarkList = new ArrayList<>();
			// 遍历备注信息
			for (ClueRemark clueRemark : clueRemarkList) {
				customerRemark = new CustomerRemark();
				customerRemark.setId(UUIDUtils.getUUID());
				customerRemark.setNoteContent(clueRemark.getNoteContent());
				customerRemark.setCreateBy(clueRemark.getCreateBy());
				customerRemark.setCreateTime(clueRemark.getCreateTime());
				customerRemark.setEditBy(clueRemark.getEditBy());
				customerRemark.setEditTime(clueRemark.getEditTime());
				customerRemark.setEditFlag(clueRemark.getEditFlag());
				customerRemark.setCustomerId(customer.getId());
				customerRemarkList.add(customerRemark);

				contactsRemark = new ContactsRemark();
				contactsRemark.setId(UUIDUtils.getUUID());
				contactsRemark.setNoteContent(clueRemark.getNoteContent());
				contactsRemark.setCreateBy(clueRemark.getCreateBy());
				contactsRemark.setCreateTime(clueRemark.getCreateTime());
				contactsRemark.setEditBy(clueRemark.getEditBy());
				contactsRemark.setEditTime(clueRemark.getEditTime());
				contactsRemark.setEditFlag(clueRemark.getEditFlag());
				contactsRemark.setContactsId(contacts.getId());
				contactsRemarkList.add(contactsRemark);
			}
			// 调用sql插入
			customerRemarkMapper.insertCustomerRemarkByList(customerRemarkList);
			contactsRemarkMapper.insertContactsRemarkByList(contactsRemarkList);
		}

		// 4.线索市场活动关系---联系人市场活动
		List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationMapper.selectClueActivityRelationByClueId(clueId);
		// 查询得结果不为空
		if (clueActivityRelationList != null && clueActivityRelationList.size() > 0) {
			ContactsActivityRelation contactsActivityRelation = null;
			List<ContactsActivityRelation> contactsActivityRelationList = new ArrayList<>();
			for (ClueActivityRelation clueActivityRelation : clueActivityRelationList) {
				contactsActivityRelation = new ContactsActivityRelation();
				contactsActivityRelation.setId(UUIDUtils.getUUID());
				contactsActivityRelation.setContactsId(contacts.getId());
				contactsActivityRelation.setActivityId(clueActivityRelation.getActivityId());
				contactsActivityRelationList.add(contactsActivityRelation);
			}
			// 插入市场活动
			contactsActivityRelationMapper.insertContactsActivityRelationByList(contactsActivityRelationList);
		}

		// 5.增加交易
		if ("true".equals(isCreateTran)){
			Tran tran = new Tran();
			tran.setDescription(clue.getDescription());
			tran.setContactSummary(clue.getContactSummary());
			tran.setNextContactTime(clue.getNextContactTime());
			tran.setMoney((String) map.get("money"));
			tran.setName((String) map.get("name"));
			tran.setExpectedDate((String) map.get("expectedDate"));
			tran.setStage((String) map.get("stage"));
			tran.setSource(clue.getSource());
			tran.setActivityId((String) map.get("activityId"));
			tran.setContactsId(contacts.getId());
			tran.setCustomerId(customer.getId());
			tran.setCreateBy(user.getId());
			tran.setCreateTime(DateUtils.formateDateTime(new Date()));
			tran.setId(UUIDUtils.getUUID());
			tran.setOwner(clue.getOwner());
			tranMapper.insertTran(tran);
			//.线索评论--交易评论
			if (clueRemarkList != null && clueRemarkList.size() > 0) {
				TranRemark tranRemark = null;
				List<TranRemark> tranRemarkList = new ArrayList<>();
				for (ClueRemark clueRemark:clueRemarkList) {
					tranRemark = new TranRemark();
					tranRemark.setId(UUIDUtils.getUUID());
					tranRemark.setNoteContent(clueRemark.getNoteContent());
					tranRemark.setCreateBy(clueRemark.getCreateBy());
					tranRemark.setCreateTime(clueRemark.getCreateTime());
					tranRemark.setEditBy(clueRemark.getEditBy());
					tranRemark.setEditTime(clueRemark.getEditTime());
					tranRemark.setEditFlag(clueRemark.getEditFlag());
					tranRemark.setTranId(tran.getId());
					tranRemarkList.add(tranRemark);
				}
				tranRemarkMapper.insertTranRemarkByList(tranRemarkList);

			}
		}


		// 6. 删除 线索备注 线索和市场关联 线索
		String[] clueIds = {clueId};
		// 删除线索备注
		clueRemarkMapper.deleteClueRemarkByClueId(clueIds);
		// 删除线索和市场关联
		clueActivityRelationMapper.deleteClueActivityRelationByClueIds(clueIds);
		// 删除线索
		clueMapper.deleteClueByIds(clueIds);

	}

}

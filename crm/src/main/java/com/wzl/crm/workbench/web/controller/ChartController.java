package com.wzl.crm.workbench.web.controller;

import com.wzl.crm.workbench.domain.FunnelVO;
import com.wzl.crm.workbench.service.ActivityService;
import com.wzl.crm.workbench.service.ClueService;
import com.wzl.crm.workbench.service.ContactsService;
import com.wzl.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wang
 * @version 1.0
 */
@Controller
public class ChartController {
	@Autowired
	private TranService tranService;
	@Autowired
	private ActivityService activityService;
	@Autowired
	private ClueService clueService;
	@Autowired
	private ContactsService contactsService;

	/**
	 * 	1访问交易图标首页
	 */

	@RequestMapping("/workbench/chart/transaction/index")
	public String tranChartIndex(){
		return "workbench/chart/transaction/index";
	}

	/**
	 * 1解析交易数据
	 * @return
	 */
	@RequestMapping("/workbench/chart/transaction/queryCountOfTranGroupByStage.do")
	@ResponseBody
	public Object queryCountOfTranGroupByStage(){
		List<FunnelVO> funnelVOList = tranService.queryCountOfTranGroupByStage();
		return funnelVOList;
	}

	/**
	 * 	2访问市场活动图标首页
	 */

	@RequestMapping("/workbench/chart/activity/index")
	public String activityChartIndex(){
		return "workbench/chart/activity/index";
	}
	/**
	 * 2解析市场活动
	 * @return
	 */
	@RequestMapping("/workbench/chart/activity/queryCountOfActivityGroupByOwner.do")
	@ResponseBody
	public Object queryCountOfActivityGroupByOwner(){
		List<FunnelVO> funnelVOList = activityService.queryCountOfActivityGroupByStage();
		return funnelVOList;
	}

	/**
	 * 	3线索图标首页
	 */
	@RequestMapping("/workbench/chart/clue/index")
	public String clueChartIndex(){
		return "workbench/chart/clue/index";
	}
	/**
	 * 3解析线索
	 * @return
	 */
	@RequestMapping("/workbench/chart/clue/queryCountOfClueGroupByStage.do")
	@ResponseBody
	public Object queryCountOfClueGroupByStage(){
		List<Integer> counts = clueService.queryCountOfClueGroupByClueStage();
		List<String> clueStage = clueService.queryClueStageOfClueGroupByClueStage();
		// 存储在map
		Map<String,Object> map = new HashMap<>();
		map.put("clueStage",clueStage);
		map.put("counts",counts);
		return map;
	}

	/**
	 * 4 客户
	 */
	@RequestMapping("/workbench/chart/customerAndContacts/index.do")
	public String toCustomerAndContactsIndex(){
		return "workbench/chart/customerAndContacts/index";
	}

	@RequestMapping("/workbench/chart/customerAndContacts/queryCountOfCustomerAndContactsGroupByCustomer.do")
	@ResponseBody
	public Object queryCountOfCustomerAndContactsGroupByCustomer(){
		List<FunnelVO> funnelVOList = contactsService.queryCountOfCustomerAndContactsGroupByCustomer();
		// 根据查询结果，返回响应信息
		return funnelVOList;
	}

}

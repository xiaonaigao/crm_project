package com.wzl.crm.workbench.web.controller;

import com.wzl.crm.settings.domain.DicValue;
import com.wzl.crm.settings.service.DicValueService;
import com.wzl.crm.workbench.domain.Tran;
import com.wzl.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wang
 * @version 1.0
 */
@Controller
public class TranController {
	@Autowired
	private TranService tranService;
	@Autowired
	private DicValueService dicValueService;

	/**
	 * 主页：点击菜单“交易”，跳转到主页
	 */
	@RequestMapping("/workbench/transaction/index.do")
	public String transactionIndex(HttpServletRequest request) {
		// 加载字典
		List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");
		List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
		List<DicValue> transactionTypeList = dicValueService.queryDicValueByTypeCode("transactionType");
		// 保存请求域
		request.setAttribute("sourceList",sourceList);
		request.setAttribute("stageList",stageList);
		request.setAttribute("transactionTypeList",transactionTypeList);
		return "workbench/transaction/index";
	}

	/**
	 * 主页：搜索交易记录列表
	 */
	@RequestMapping("/workbench/transaction/transactionListIndex.do")
	public @ResponseBody
	Object transactionListIndex(int pageNo, int pageSize,String owner,String name,String customerName,String stage,String type,String source,String contactName) {
		// 获取参数
		HashMap<String, Object> map = new HashMap<>();
		map.put("pageNo", (pageNo-1) * pageSize);
		map.put("pageSize", pageSize);
		map.put("owner", owner);
		map.put("name", name);
		map.put("customerName", customerName);
		map.put("stage", stage);
		map.put("type", type);
		map.put("source", source);
		map.put("contactName", contactName);
		// 调用service
		List<Tran> tranAllList = tranService.queryAllTranDetail(map);
		int totalRows = tranService.queryAllTranCount(map);
		// 返回json
		Map<String, Object> retMap = new HashMap<>();
		retMap.put("tranAllList",tranAllList);
		retMap.put("totalRows",totalRows);
		return retMap;
	}
}

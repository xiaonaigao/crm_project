package com.wzl.crm.commons.utils;

import com.wzl.crm.workbench.domain.Activity;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author wang
 * @version 1.0
 */
public class HSSFUtils {
	/**
	 * 下载市场活动的Excel
	 * activityList 市场活动集合
	 * fileName文件名
	 */
	public static void createExcelByActivityList(List<Activity> activityList, String fileName, HttpServletResponse response) throws Exception {
		// 2.创建Excel
		// 2.1.1创建文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 2.1.2 创建表
		HSSFSheet sheet = wb.createSheet("市场活动");
		// 2.1.3 创建行和列
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell = row.createCell(0);
		// 2.1.4 列的设置
		cell.setCellValue("ID");
		cell = row.createCell(1);
		cell.setCellValue("所有者");
		cell = row.createCell(2);
		cell.setCellValue("名称");
		cell = row.createCell(3);
		cell.setCellValue("开始日期");
		cell = row.createCell(4);
		cell.setCellValue("结束日期");
		cell = row.createCell(5);
		cell.setCellValue("成本");
		cell = row.createCell(6);
		cell.setCellValue("描述");
		cell = row.createCell(7);
		cell.setCellValue("创建时间");
		cell = row.createCell(8);
		cell.setCellValue("创建者");
		cell = row.createCell(9);
		cell.setCellValue("修改日期");
		cell = row.createCell(10);
		cell.setCellValue("修改者");
		if (activityList != null && activityList.size() > 0) {
			Activity activity = null;
			for (int i = 0; i < activityList.size(); i++) {
				activity = activityList.get(i);
				//生成行
				row = sheet.createRow(i + 1);
				//创建列
				cell = row.createCell(0);
				cell.setCellValue(activity.getId());
				cell = row.createCell(1);
				cell.setCellValue(activity.getOwner());
				cell = row.createCell(2);
				cell.setCellValue(activity.getName());
				cell = row.createCell(3);
				cell.setCellValue(activity.getStartDate());
				cell = row.createCell(4);
				cell.setCellValue(activity.getEndDate());
				cell = row.createCell(5);
				cell.setCellValue(activity.getCost());
				cell = row.createCell(6);
				cell.setCellValue(activity.getDescription());
				cell = row.createCell(7);
				cell.setCellValue(activity.getCreateTime());
				cell = row.createCell(8);
				cell.setCellValue(activity.getCreateBy());
				cell = row.createCell(9);
				cell.setCellValue(activity.getEditTime());
				cell = row.createCell(10);
				cell.setCellValue(activity.getEditBy());
			}
		}
		// 4 文件下载
		// 4.1 设置响应类型  excel文件是application/octet-stream二进制文件
		response.setContentType("application/octet-stream;charset=UTF-8");
		// 激活文件下载窗口 Content-Disposition不打开，attachment附件
		response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
		// 4.2 获取输出流
		ServletOutputStream out = response.getOutputStream();
		wb.write(out);
		// 3.2 关闭资源
		wb.close();
		out.flush(); // 输出流缓存中的内容强制输出,但并不会关闭输出流
	}

	/**
	 * 读取excel表格列的值
	 */
	public static String getCellValueForStr(HSSFCell cell) {
		String ret = "";
		if(cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			ret = cell.getStringCellValue();
		} else if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			ret = cell.getNumericCellValue() + "";
		} else if(cell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {
			ret = cell.getBooleanCellValue() + "";
		} else if(cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
			ret = cell.getCellFormula();
		} else {
			ret = "";
		}
		return ret;
	}

}

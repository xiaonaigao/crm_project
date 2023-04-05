package com.wzl.crm.commons.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wang
 * @version 1.0
 */
public class DateUtils {
	public static String formateDateTime(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	public static String formateDate(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}
}

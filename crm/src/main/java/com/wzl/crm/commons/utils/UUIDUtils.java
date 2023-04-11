package com.wzl.crm.commons.utils;

import java.util.UUID;

/**
 * @author wang
 * @version 1.0
 */
public class UUIDUtils {
	public static String getUUID(){
		return UUID.randomUUID().toString().replaceAll("-","");
	}
}

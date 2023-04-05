package com.wzl.crm.commons.domain;

/**
 * @author wang
 * @version 1.0
 */
public class ReturnObject {
	private String code;//成功或者失败：1成功，0失败
	private String message;//提示信息
	private Object retDate;//返回其他数据

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getRetDate() {
		return retDate;
	}

	public void setRetDate(Object retDate) {
		this.retDate = retDate;
	}
}

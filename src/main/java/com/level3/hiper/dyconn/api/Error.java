/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.level3.hiper.dyconn.api;

/**
 *
 * @author jzendle
 */
public class Error {
	String message = "SUCCESS";
	int code = 0;
	String detail = "The operation was successful";

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDetail() {
		return detail;
	}
	public void setDetail(String st) {
		detail = st;
	}

	
	
}
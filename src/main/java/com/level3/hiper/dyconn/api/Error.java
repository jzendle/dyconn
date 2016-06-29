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

public final static String SUCCESS = "SUCCESS";
public final static String FAILURE = "FAILURE";


	private String message = SUCCESS;
	private int code = 200; // standard success code
	private String detail = "The operation was successful";

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

   @Override
   public String toString() {
      return "Error{" + "message=" + message + ", code=" + code + ", detail=" + detail + '}';
   }

	
	
}

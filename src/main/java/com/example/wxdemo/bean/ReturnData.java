package com.example.wxdemo.bean;

import java.io.Serializable;


public class ReturnData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 响应业务状态码
	private int status;

	// 响应信息
	private String msgInfo;

	// 响应数据
	private Object body;

	private ReturnData(int status, String msgInfo, Object body) {
		this.status = status;
		this.msgInfo = msgInfo;
		this.body = body;
	}

	public static ReturnData error(int status, String msgInfo, Object body) {
		return new ReturnData(status, msgInfo, body);
	}

	public static ReturnData error(int status, String msgInfo) {
		return new ReturnData(status, msgInfo, null);
	}

	public static ReturnData success(Object body) {
		return new ReturnData(ResponseCode.OPERATION_SUCCESS.getStatus(), ResponseCode.OPERATION_SUCCESS.getMsg(),
				body);
	}

	public static ReturnData success() {
		return new ReturnData(ResponseCode.OPERATION_SUCCESS.getStatus(), ResponseCode.OPERATION_SUCCESS.getMsg(),
				null);
	}

	public static ReturnData fail(Object data) {
		return new ReturnData(ResponseCode.OPERATION_FAIL.getStatus(), ResponseCode.OPERATION_FAIL.getMsg(), data);
	}

	public static ReturnData fail() {
		return new ReturnData(ResponseCode.OPERATION_FAIL.getStatus(), ResponseCode.OPERATION_FAIL.getMsg(), null);
	}

	public ReturnData() {
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMsgInfo() {
		return msgInfo;
	}

	public void setMsgInfo(String msgInfo) {
		this.msgInfo = msgInfo;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "ReturnData{" +
				"status=" + status +
				", msgInfo='" + msgInfo + '\'' +
				", body=" + body +
				'}';
	}
}
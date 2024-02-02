package com.fmf.fmf.comfyui.common;

import cn.hutool.core.lang.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class R<T> implements Serializable {
	private Integer code;
	private T data;
	private String msg;
	private String requestId;

	/**
	 * 默认成功返回消息
	 */
	protected final static String DEFAULT_SUCCESS_MSG = "success";
	/**
	 * 默认失败返回消息
	 */
	protected final static String DEFAULT_FAIL_MSG = "fail";
	/**
	 * 成功响应码
	 */
	protected final static Integer SUCCESS = 200;
	/**
	 * 失败响应码
	 */
	protected final static Integer FAIL = 500;
	protected static <T> R<T> restResult(int code, T data,String msg) {
		R<T> resp = new R<>();
		resp.setCode(code);
		resp.setData(data);
		resp.setMsg(msg);
		return resp;
	}

	public static <T> R<T> ok() {
		return restResult( SUCCESS, null, DEFAULT_SUCCESS_MSG);
	}

	public static <T> R<T> ok(T data) {
		return restResult(SUCCESS, data, DEFAULT_SUCCESS_MSG);
	}

	public static <T> R<T> ok(T data, String msg) {
		return restResult(SUCCESS, data, msg);
	}

	public static <T> R<T> fail() {
		return restResult(FAIL, null,DEFAULT_FAIL_MSG);
	}

	public static <T> R<T> fail(String msg) {
		return restResult(FAIL, null, msg);
	}

	public static <T> R<T> fail(T data) {
		return restResult( FAIL, data, DEFAULT_FAIL_MSG);
	}

	public static <T> R<T> fail(String msg, T data) {
		return restResult(FAIL, data, msg);
	}

	public static <T> R<T> fail(int code, String msg) {
		return restResult(code, null, msg);
	}
}
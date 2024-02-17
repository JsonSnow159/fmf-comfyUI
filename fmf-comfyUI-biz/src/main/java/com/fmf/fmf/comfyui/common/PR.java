package com.fmf.fmf.comfyui.common;

import lombok.Data;

import java.util.Collections;

/**
 *	分页消息响应体
 *  @date 2022/4/25
 *  @author Ludan
 */
@Data
public class PR<T> extends R<T> {
	/**
	 * 分页条数
	 */
	private Long count;

	protected static <T> PR<T> restPageResult( int code, T data,String msg, Long count) {
		PR<T> r = new PR<>();
		r.setCode(code);
		r.setData(data);
		r.setMsg(msg);
		r.setCount(count);
		return r;
	}

	public static <T> PR<T> ok(Integer code, T data, String msg, Long count) {
		PR<T> pr = restPageResult(code, data, msg, count);
		return pr;
	}

	public static <T> PR<T> ofEmpty(Long tatal) {
		return (PR<T>) PR.ok(Collections.emptyList(), tatal);
	}

	public static <T> PR<T> ok(T data, Long count) {
		return ok(SUCCESS, data, DEFAULT_SUCCESS_MSG, count);
	}
}

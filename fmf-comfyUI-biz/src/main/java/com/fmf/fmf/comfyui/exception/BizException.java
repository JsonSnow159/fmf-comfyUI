package com.fmf.fmf.comfyui.exception;

import com.alibaba.fastjson.JSONObject;

/**
 * 自定义业务异常
 */
public class BizException extends RuntimeException{
	private int code;

	public BizException(int code, String msg) {
		super(msg);
		this.code = code;
	}

	public <T extends ICodeEnum> BizException(T errCodeEnum) {
		super(errCodeEnum.getMsg());
		this.code = errCodeEnum.getCode();
	}

	public int getCode() {
		return code;
	}

	@Override
	public String toString() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code", code);
		jsonObject.put("msg", getMessage());
		return jsonObject.toJSONString();
	}
}

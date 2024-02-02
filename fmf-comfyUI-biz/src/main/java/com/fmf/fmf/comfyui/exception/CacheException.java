package com.fmf.fmf.comfyui.exception;

public class CacheException extends BizException{
	private static final Integer DEEFAULT_ERROR_CODE = 50004;

	public CacheException(String msg) {
		super(DEEFAULT_ERROR_CODE, msg);
	}

	public CacheException(ErrorCodeEnum codeEnum) {
		super(codeEnum.getCode(), codeEnum.getMsg());
	}
}

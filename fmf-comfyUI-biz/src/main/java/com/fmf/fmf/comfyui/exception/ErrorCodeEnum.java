package com.fmf.fmf.comfyui.exception;

public enum ErrorCodeEnum implements ICodeEnum{
	TOOL_TEMPLATE_ERROR(10000001, "工具未配置模板，暂不可用"),
	CLOUD_MACHINE_ERROR(10000002, "未找到可用云算力服务器"),
	HTTP_CALL_NO_RESPONSE_ERROR(1000013, "远程服务异常断开，无法响应"),
	HTTP_CALL_TIMEOUT_ERROR(1000008, "Http请求超时返回失败"),
	HTTP_CALL_4XX_ERROR(1000010, "Http请求4xx客户端异常"),
	HTTP_CALL_5XX_ERROR(1000009, "Http请求5xx远端服务端异常"),
	HTTP_CALL_UNKNOWN_ERROR(1000007, "Http请求未知错误"),
	INSERT_ERROR(20000001, "数据库插入错误"),
	PARAM_ERROR(10000003, "参数错误"),
	IMAGE_UPLOAD_ERROR(10000004, "图片上传失败"),
	;

	private Integer code;
	private String msg;

	ErrorCodeEnum(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	@Override
	public Integer getCode() {
		return this.code;
	}

	@Override
	public String getMsg() {
		return this.msg;
	}
}

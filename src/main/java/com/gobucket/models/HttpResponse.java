package com.gobucket.models;

public class HttpResponse {
	private final int statusCode;
	private final String statusMessage;
	private final String responseDesc;

	private final Object responseData;

	public HttpResponse(int statusCode, String statusMessage, String responseDesc, Object responseData) {
		this.statusCode = statusCode;
		this.statusMessage = statusMessage;
		this.responseDesc = responseDesc;
		this.responseData = responseData;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public String getResponseDesc() {
		return responseDesc;
	}

	public Object getResponseData() {
		return responseData;
	}
}

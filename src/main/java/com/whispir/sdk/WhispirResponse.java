package com.whispir.sdk;

import java.util.Map;

public class WhispirResponse {
	private int statusCode;
	private String rawResponse;
	private Map<String, String> responseMap;

	public WhispirResponse() {
		this.statusCode = 0;
		this.responseMap = null;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public Map<String, String> getResponse() {
		return responseMap;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public void setResponse(Map<String, String> response) {
		this.responseMap = response;
	}
	
	public String getRawResponse() {
		return rawResponse;
	}

	public void setRawResponse(String rawResponse) {
		this.rawResponse = rawResponse;
	}
}

package com.whispir.sdk;

import java.util.Map;

public class WhispirResponse {
	private int statusCode;
	private Map<String,String> response;
	
	public WhispirResponse() {
		this.statusCode = 0;
		this.response = null;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public Map<String, String> getResponse() {
		return response;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public void setResponse(Map<String, String> response) {
		this.response = response;
	}
}

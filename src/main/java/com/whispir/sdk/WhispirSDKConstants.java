package com.whispir.sdk;

public final class WhispirSDKConstants {
	
	public static final String API_SCHEME = "https://";
	public static final String API_HOST = "api.whispir.com";
	public static final String API_EXT = "?apikey=";
	public static final String NO_AUTH_ERROR = "Whispir API Authentication failed. API Key, Username or Password was not provided.";
	public static final String AUTH_FAILED_ERROR = "Whispir API Authentication failed. API Key, Username or Password were provided but were not correct.";
	
	//Content Types
	public static final String WHISPIR_MESSAGE_HEADER_V1 = "application/vnd.whispir.message-v1+json";
	public static final String WHISPIR_WORKSPACE_HEADER_V1 = "application/vnd.whispir.workspace-v1+json";
	public static final String WHISPIR_SCENARIO_HEADER_V1 = "application/vnd.whispir.scenario-v1+json";
	
	//Resource Types
	public static final String MESSAGES_RESOURCE = "messages";
	public static final String WORKSPACES_RESOURCE = "workspaces";
	public static final String SCENARIOS_RESOURCE = "scenarios";
	
	//HTTP Methods
	public static final String GET = "GET";
	public static final String POST = "POST";
	public static final String PUT = "PUT";
	public static final String DELETE = "DELETE";
	
}

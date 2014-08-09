package com.whispir.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.json.JSONException;
import org.json.JSONObject;

public class WhispirAPI {

	private static final String WHISPIR_MESSAGE_HEADER = "application/vnd.whispir.message-v1+json";
	private static final String API_HOST = "api.whispir.com";
	private static final String API_URL = "https://api.whispir.com/messages?apikey=";
	private String apikey;
	private String username;
	private String password;

	public void setApikey(String apikey) {
		this.apikey = apikey;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public WhispirAPI() {}
	
	public WhispirAPI(String apikey, String username, String password) {
		this.apikey = apikey;
		this.username = username;
		this.password = password;
	}
	
	/**
	 * Allows a user to send a message in the default My Company workspace
	 * @param recipient - the mobile number or email address of the recipient of the message
	 * @param subject - the textual subject of the message
	 * @param content - the textual content of the Push/SMS message.
	 * 
	 * For more complex content, the user should use the Map content overloaded function
	 * 
	 * @return response - the HTTP response code of the performed action.
	 */
	public int sendMessage(String recipient, String subject, String content){		
		return sendMessage("", recipient, subject, content);
	}
	
	/**
	 * Allows a user to send a message in the specified Workspace ID
	 * @param recipient - the mobile number or email address of the recipient of the message
	 * @param subject - the textual subject of the message
	 * @param content - the textual content of the Push/SMS message.
	 * 
	 * For more complex content, the user should use the Map content overloaded function
	 * 
	 * @return response - the HTTP response code of the performed action.
	 */
	public int sendMessage(String workspaceId, String recipient, String subject, String content){
		
		Map<String,String> smsContent = new HashMap<String,String>();
		
		smsContent.put("body", content);
		
		return sendMessage(workspaceId, recipient, subject, smsContent);
	}
	
	/**
	 * Allows a user to send a message in any workspace, with any combination of content within the content map
	 * @param recipient - the mobile number or email address of the recipient of the message
	 * @param subject - the textual subject of the message
	 * @param content - the textual content of the Push/SMS message.
	 * 
	 * For more complex content, the user should use the Map content overloaded function
	 * 
	 * @return response - the HTTP response code of the performed action.
	 */
	public int sendMessage(String workspaceId, String recipient, String subject, Map<String,String> content){
		int response = 0;
		
		if(recipient == null || recipient.length() < 8) {
			//error with the recipient information, returning HTTP 422.
			return 422;
		}
		
		try {
			
			String sms = content.get("body");
			
			String myString = new JSONObject().put("to", recipient)
					.put("subject", subject).put("body", sms)
					.toString();

			response = httpPost(myString);

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return response;
	}

	private int httpPost(String jsonContent) throws Exception {
		String response = "";
		int statusCode = 0;

		// Create an instance of HttpClient.
		HttpClient client = new HttpClient();

		// Create a method instance.
		PostMethod method = new PostMethod(API_URL + this.apikey);

		client.getState().setCredentials(
				new AuthScope(API_HOST, 443, null),
				new UsernamePasswordCredentials(this.username, this.password));

		method.setDoAuthentication(true);
		
		method.setRequestHeader("Content-Type", WHISPIR_MESSAGE_HEADER);
		method.setRequestHeader("Accept", WHISPIR_MESSAGE_HEADER);

		//method.setRequestBody(jsonContent);
		
		RequestEntity request = new StringRequestEntity(jsonContent, WHISPIR_MESSAGE_HEADER, null);
		method.setRequestEntity(request);

		try {
			// Execute the method.
			statusCode = client.executeMethod(method);

			if (statusCode != HttpStatus.SC_ACCEPTED) {
				System.err.println("Method failed: " + method.getStatusLine());
			}

			// Read the response body.
			byte[] responseBody = method.getResponseBody();

			// Deal with the response.
			// Use caution: ensure correct character encoding and is not binary
			// data
			response = new String(responseBody);
			
			System.out.println(response);

		} catch (HttpException e) {
			System.err.println("Fatal protocol violation: " + e.getMessage());
			e.printStackTrace();
			response = "error";
		} catch (IOException e) {
			System.err.println("Fatal transport error: " + e.getMessage());
			e.printStackTrace();
			response = "error";
		} finally {
			// Release the connection.
			method.releaseConnection();
		}

		return statusCode;
	}
}

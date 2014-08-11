package com.whispir.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.OptionsMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.json.JSONException;
import org.json.JSONObject;

import com.whispir.api.exceptions.WhispirAPIException;

/**
 * WhispirAPI 
 * 
 * Wrapper class to simplify the usage of the Whispir API.  
 * 
 * Utilises Apache HTTPClient to post simple messages via JSON.
 * 
 * @author Jordan Walsh
 * @version 1.0
 * 
 */

public class WhispirAPI {

	private static final String WHISPIR_MESSAGE_HEADER_V1 = "application/vnd.whispir.message-v1+json";
	private static final String WHISPIR_MESSAGE_HEADER_V2 = "application/vnd.whispir.message-v2+json";
	private static final String API_HOST = "api.whispir.com";
	private static final String API_URL = "https://" + API_HOST + "/";
	private static final String API_EXT = "?apikey=";
	private static final String NO_AUTH_ERROR = "Whispir API Authentication failed. API Key, Username or Password was not provided.";
	private static final String AUTH_FAILED_ERROR = "Whispir API Authentication failed. API Key, Username or Password were provided but were not correct.";
	
	private String apikey;
	private String username;
	private String password;
	private String version;

	@SuppressWarnings("unused")
	private WhispirAPI() {}
	
	/**
	 * Instantiates the WhispirAPI object.
	 * 
	 * Requires the three parameters to be provided.  Assumes that this is using the v1 API.
	 *  
	 * @param apikey
	 * @param username
	 * @param password
	 */
	
	public WhispirAPI(String apikey, String username, String password) throws WhispirAPIException{	
		this(apikey, username, password, "v1");
	}
	
	/**
	 * Instantiates the WhispirAPI object.
	 * 
	 * Requires the four parameters to be provided.  Version can be provided in the form "v1" or "v2".
	 *  
	 * @param apikey
	 * @param username
	 * @param password
	 * @param version
	 */
	
	public WhispirAPI(String apikey, String username, String password, String version) throws WhispirAPIException{
		
		if(apikey == null || username == null || password == null || version == null ) {
			throw new WhispirAPIException(NO_AUTH_ERROR);
		}
		
		if("".equals(apikey) || "".equals(username) || "".equals(password) || "".equals(version)) {
			throw new WhispirAPIException(NO_AUTH_ERROR);
		}
		
		try {
			this.apikey = apikey;
			this.username = username;
			this.password = password;
			
			//If the GET request fails, then throw an error as the API won't work.
			int response = this.testHttpCall();
			
			if (response != 200) {
				throw new WhispirAPIException(AUTH_FAILED_ERROR);
			} 
			
			if("v2".equals(version)) {
				this.version = WHISPIR_MESSAGE_HEADER_V2;
			} else {
				this.version = WHISPIR_MESSAGE_HEADER_V1;
			}
			
		} catch (WhispirAPIException e) {
			throw e;
		}
	}
	
	public void setApikey(String apikey) {
		this.apikey = apikey;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	/**
	 * <p>Allows a user to send a message in the default My Company workspace.</p>
	 * <p>For more complex content, the user should use the Map content overloaded function.</p>
	 * @param recipient - the mobile number or email address of the recipient of the message
	 * @param subject - the textual subject of the message
	 * @param content - the textual content of the Push/SMS message.
	 * @return response - the HTTP response code of the performed action.
	 */
	public int sendMessage(String recipient, String subject, String content) throws WhispirAPIException{		
		return sendMessage("", recipient, subject, content);
	}
	
	/**
	 * <p>Allows a user to send a message in the specified Workspace ID.</p>
	 * <p>For more complex content, the user should use the Map content overloaded function.</p>
	 * @param recipient - the mobile number or email address of the recipient of the message
	 * @param subject - the textual subject of the message
	 * @param content - the textual content of the Push/SMS message.
	 * @return response - the HTTP response code of the performed action.
	 */
	public int sendMessage(String workspaceId, String recipient, String subject, String content) throws WhispirAPIException{
		Map<String,String> smsContent = new HashMap<String,String>();
		smsContent.put("body", content);
		return sendMessage(workspaceId, recipient, subject, smsContent);
	}
	
	/**
	 * <p>Allows a user to send a message in any workspace, with any combination of content within the content map.</p>
	 * <p>The content Map is expected to provide the following information.</p>
	 * <p>For SMS/Push</p>
	 * <p>- body - The content for the Push/SMS message.</p>
	 * 
	 * @param recipient - the mobile number or email address of the recipient of the message
	 * @param subject - the textual subject of the message
	 * @param content - the Map of content for the Whispir Message
	 * @return response - the HTTP response code of the performed action.
	 */
	public int sendMessage(String workspaceId, String recipient, String subject, Map<String,String> content) throws WhispirAPIException{
		Map<String, String> options = new HashMap<String, String>();
		return sendMessage(workspaceId, recipient, subject, content, options);
	}
	
	/**
	 * Allows a user to send a message in any workspace, with any combination of content within the content map
	 * <p>The content Map is expected to provide the following information:</p>
	 * <p>For SMS/Push</p>
	 * <p>- body - The content of the SMS/Push Message</p>
	 * <p></p>
	 * <p>The options Map is expected to provide the following information:</p>
	 * <p>- type - defaultNoReply (specifies if the message cannot be replied to)</p>
	 * <p>- pushEscalation - true/false (string)</p>
	 * <p>- escalationMins - 3,4,5,10 (string)</p>
	 * <p></p>
	 * @param recipient - the mobile number or email address of the recipient of the message
	 * @param subject - the textual subject of the message
	 * @param content - the Map of content for the Whispir Message
	 * @param options - the Map of options for the Whispir Message
	 * @return response - the HTTP response code of the performed action.
	 */
	public int sendMessage(String workspaceId, String recipient, String subject, Map<String,String> content, Map<String,String> options) throws WhispirAPIException{
		int response = 0;
		
		if(recipient == null || recipient.length() < 8) {
			//error with the recipient information, returning HTTP 422.
			return 422;
		}
		
		try {
			JSONObject request = new JSONObject();
			
			request.put("to", recipient);
			request.put("subject", subject);
			
			//Check for the body in the map
			if(content.containsKey("body") && !"".equals(content.get("body"))) {
				request.put("body", content.get("body"));
			}
			
			//Check for the email in the map
			if(content.containsKey("email") && !"".equals(content.get("email"))) {
				JSONObject email = new JSONObject();
				email.put("body", content.get("email"));
				
				if(content.containsKey("emailType") && !"".equals(content.get("emailType"))) {
					email.put("type", content.get("emailType"));
				}
				
				request.put("email", email);
			}
			
			//Check for the voice content in the map
			if(content.containsKey("voice") && !"".equals(content.get("voice"))) {
				JSONObject voice = new JSONObject();
				voice.put("body", content.get("voice"));
				voice.put("type", "ConfCall:,ConfAccountNo:,ConfPinNo:,ConfModPinNo:,Pin:");
				
				if(content.containsKey("voiceIntro") && !"".equals(content.get("voiceIntro"))) {
					voice.put("header", content.get("voiceIntro"));
				}
				
				request.put("voice", voice);
			}
			
			//Check for the web content in the map
			if(content.containsKey("web") && !"".equals(content.get("web"))) {
				JSONObject web = new JSONObject();
				web.put("body", content.get("web"));
				
				if(content.containsKey("webType") && !"".equals(content.get("webType"))) {
					web.put("type", content.get("webType"));
				}
				
				request.put("web", web);
			}
			
			//Check for the noreply options in the map
			if(options.containsKey("type")) {
				request.put("type", options.get("type"));
			}
			
			//Check for the push to SMS escalation options in the map
			if(options.containsKey("pushNotifications") && "enabled".equalsIgnoreCase(options.get("pushNotifications")) ) {
				
				JSONObject features = new JSONObject();
				features.put("pushNotifications", "enabled");
				
				if(options.containsKey("pushEscalationMins")) {
					features.put("pushEscalationMins", options.get("pushEscalationMins"));
				}
				 
				request.put("features", features);
			}
			
			//Execute the request
			response = httpPost(workspaceId, request.toString());

		} catch (JSONException e) {
			throw new WhispirAPIException("Error occurred parsing the object with the content provided." + e.getMessage());
		} catch (Exception e) {
			throw new WhispirAPIException("Error occurred." + e.getMessage());
		}
		
		return response;
	}
	
	//***************************************************
	//* Private Methods
	//***************************************************
	private int testHttpCall() throws WhispirAPIException {	
		OptionsMethod method = (OptionsMethod)createOptionsMethod();
		return executeHttpMethod(method);
	}

	private int httpPost(String workspace, String jsonContent) throws WhispirAPIException {	
		PostMethod method = (PostMethod)createPostMethod(workspace, jsonContent);
		return executeHttpMethod(method);
	}

	private int executeHttpMethod(HttpMethod method) throws WhispirAPIException {
		int statusCode = 0;

		// Create an instance of HttpClient.
		HttpClient client = new HttpClient();

		client.getState().setCredentials(
				new AuthScope(API_HOST, -1),
				new UsernamePasswordCredentials(this.username, this.password));

		try {
			statusCode = client.executeMethod(method);
		} catch (HttpException e) {
			System.err.println("Fatal protocol violation: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Fatal transport error: " + e.getMessage());
			e.printStackTrace();
		} finally {
			// Release the connection.
			method.releaseConnection();
		}

		return statusCode;
	}
	
	private HttpMethod createPostMethod(String workspaceId, String content) throws WhispirAPIException {
		// Create a method instance.
		String url = "";
		RequestEntity request;
		
		if(workspaceId != null && !"".equals(workspaceId)) {
			url = API_URL + "workspaces/" + workspaceId + "/messages" + API_EXT + this.apikey;
		} else {
			url = API_URL + "messages" + API_EXT + this.apikey;
		}
		PostMethod method = new PostMethod(url);

		method.setDoAuthentication(true);
		
		method.setRequestHeader("Content-Type", this.version);
		method.setRequestHeader("Accept", this.version);
		
		try {
			request = new StringRequestEntity(content, this.version, null);
			method.setRequestEntity(request);
		} catch (UnsupportedEncodingException e) {
			throw new WhispirAPIException(e.getMessage());
		}
		
		return method;
	}
	
	private HttpMethod createOptionsMethod() {
		// Create a method instance.
		final String url = API_URL + "messages" + API_EXT + this.apikey;
		OptionsMethod method = new OptionsMethod(url);
		method.setDoAuthentication(true);
		return method;
	}
}

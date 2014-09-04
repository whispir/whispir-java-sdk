package com.whispir.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
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
	private static final String API_SCHEME = "https://";
	private static final String API_HOST = "api.whispir.com";
	private static final String API_EXT = "?apikey=";
	public static final String NO_AUTH_ERROR = "Whispir API Authentication failed. API Key, Username or Password was not provided.";
	public static final String AUTH_FAILED_ERROR = "Whispir API Authentication failed. API Key, Username or Password were provided but were not correct.";

	private String apikey;
	private String username;
	private String password;

	// Used for debugging/testing purposes
	private String debugHost;
	private boolean debug;

	// Used for proxy purposes
	private RequestConfig proxy;
	private boolean proxyEnabled;

	@SuppressWarnings("unused")
	private WhispirAPI() {
	}

	/**
	 * Instantiates the WhispirAPI object.
	 * 
	 * Requires the three parameters to be provided. Assumes that this is using
	 * the v1 API.
	 * 
	 * @param apikey
	 * @param username
	 * @param password
	 */

	public WhispirAPI(String apikey, String username, String password)
			throws WhispirAPIException {
		this(apikey, username, password, "");
	}

	/**
	 * Instantiates the WhispirAPI object.
	 * 
	 * Requires the four parameters to be provided. DebugHost can be provided in the form xxxxxxx.whispir.net:8080 / xxxx.whispir.com
	 * 
	 * @param apikey
	 * @param username
	 * @param password
	 * @param debugHost
	 */

	public WhispirAPI(String apikey, String username, String password, String debugHost) throws WhispirAPIException {

		if (apikey.equals(null) || username.equals(null) || password.equals(null)) {
			throw new WhispirAPIException(NO_AUTH_ERROR);
		}

		if ("".equals(apikey) || "".equals(username) || "".equals(password)) {
			throw new WhispirAPIException(NO_AUTH_ERROR);
		}

		this.apikey = apikey;
		this.username = username;
		this.password = password;
		this.proxyEnabled = false;

		if (debugHost != null && !"".equals(debugHost)) {
			this.setDebugHost(debugHost);
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

	public void setDebugHost(String debugHost) {
		if (!"".equals(debugHost)) {
			this.debugHost = debugHost;
			this.debug = true;
		} else {
			this.debug = false;
		}
	}

	public void setProxy(String host, int port, boolean httpsEnabled) {
		
		String scheme = "http";
		
		if(httpsEnabled) {
			scheme = "https";
		}
		
		this.proxy = RequestConfig.custom().setProxy(new HttpHost(host, port, scheme)).build();
		this.proxyEnabled = true;
	}

	/**
	 * <p>
	 * Allows a user to send a message in the default My Company workspace.
	 * </p>
	 * <p>
	 * For more complex content, the user should use the Map content overloaded
	 * function.
	 * </p>
	 * 
	 * @param recipient
	 *            - the mobile number or email address of the recipient of the
	 *            message
	 * @param subject
	 *            - the textual subject of the message
	 * @param content
	 *            - the textual content of the Push/SMS message.
	 * @return response - the HTTP response code of the performed action.
	 */
	public int sendMessage(String recipient, String subject, String content)
			throws WhispirAPIException {
		return sendMessage("", recipient, subject, content);
	}

	/**
	 * <p>
	 * Allows a user to send a message in the specified Workspace ID.
	 * </p>
	 * <p>
	 * For more complex content, the user should use the Map content overloaded
	 * function.
	 * </p>
	 * 
	 * @param recipient
	 *            - the mobile number or email address of the recipient of the
	 *            message
	 * @param subject
	 *            - the textual subject of the message
	 * @param content
	 *            - the textual content of the Push/SMS message.
	 * @return response - the HTTP response code of the performed action.
	 */
	public int sendMessage(String workspaceId, String recipient,
			String subject, String content) throws WhispirAPIException {
		Map<String, String> smsContent = new HashMap<String, String>();
		smsContent.put("body", content);
		return sendMessage(workspaceId, recipient, subject, smsContent);
	}

	/**
	 * <p>
	 * Allows a user to send a message in any workspace, with any combination of
	 * content within the content map.
	 * </p>
	 * <p>
	 * The content Map is expected to provide the following information.
	 * </p>
	 * <p>
	 * For SMS/Push
	 * </p>
	 * <p>
	 * - body - The content for the Push/SMS message.
	 * </p>
	 * 
	 * @param recipient
	 *            - the mobile number or email address of the recipient of the
	 *            message
	 * @param subject
	 *            - the textual subject of the message
	 * @param content
	 *            - the Map of content for the Whispir Message
	 * @return response - the HTTP response code of the performed action.
	 */
	public int sendMessage(String workspaceId, String recipient,
			String subject, Map<String, String> content)
			throws WhispirAPIException {
		Map<String, String> options = new HashMap<String, String>();
		return sendMessage(workspaceId, recipient, subject, content, options);
	}

	/**
	 * Allows a user to send a message in any workspace, with any combination of
	 * content within the content map
	 * <p>
	 * The content Map is expected to provide the following information:
	 * </p>
	 * <p>
	 * For SMS/Push
	 * </p>
	 * <p>
	 * - body - The content of the SMS/Push Message
	 * </p>
	 * <p>
	 * </p>
	 * <p>
	 * The options Map is expected to provide the following information:
	 * </p>
	 * <p>
	 * - type - defaultNoReply (specifies if the message cannot be replied to)
	 * </p>
	 * <p>
	 * - pushEscalation - true/false (string)
	 * </p>
	 * <p>
	 * - escalationMins - 3,4,5,10 (string)
	 * </p>
	 * <p>
	 * </p>
	 * 
	 * @param recipient
	 *            - the mobile number or email address of the recipient of the
	 *            message
	 * @param subject
	 *            - the textual subject of the message
	 * @param content
	 *            - the Map of content for the Whispir Message
	 * @param options
	 *            - the Map of options for the Whispir Message
	 * @return response - the HTTP response code of the performed action.
	 */
	public int sendMessage(String workspaceId, String recipient,
			String subject, Map<String, String> content,
			Map<String, String> options) throws WhispirAPIException {
		int response = 0;

		if (recipient == null || recipient.length() < 8) {
			// error with the recipient information, returning HTTP 422.
			return 422;
		}

		try {
			JSONObject request = new JSONObject();

			request.put("to", recipient);
			request.put("subject", subject);

			// Check for the body in the map
			if (content.containsKey("body") && !"".equals(content.get("body"))) {
				request.put("body", content.get("body"));
			}

			// Check for the email in the map
			if (content.containsKey("email")
					&& !"".equals(content.get("email"))) {
				JSONObject email = new JSONObject();
				email.put("body", content.get("email"));

				if (content.containsKey("emailType")
						&& !"".equals(content.get("emailType"))) {
					email.put("type", content.get("emailType"));
				}

				request.put("email", email);
			}

			// Check for the voice content in the map
			if (content.containsKey("voice")
					&& !"".equals(content.get("voice"))) {
				JSONObject voice = new JSONObject();
				voice.put("body", content.get("voice"));
				voice.put("type",
						"ConfCall:,ConfAccountNo:,ConfPinNo:,ConfModPinNo:,Pin:");

				if (content.containsKey("voiceIntro")
						&& !"".equals(content.get("voiceIntro"))) {
					voice.put("header", content.get("voiceIntro"));
				}

				request.put("voice", voice);
			}

			// Check for the web content in the map
			if (content.containsKey("web") && !"".equals(content.get("web"))) {
				JSONObject web = new JSONObject();
				web.put("body", content.get("web"));

				if (content.containsKey("webType")
						&& !"".equals(content.get("webType"))) {
					web.put("type", content.get("webType"));
				}

				request.put("web", web);
			}

			// Check for the noreply options in the map
			if (options.containsKey("type")) {
				request.put("type", options.get("type"));
			}

			// Check for the push to SMS escalation options in the map
			if (options.containsKey("pushNotifications")
					&& "enabled".equalsIgnoreCase(options
							.get("pushNotifications"))) {

				JSONObject pushOptions = new JSONObject();				
				pushOptions.put("notifications", "enabled");

				if (options.containsKey("pushEscalationMins")) {
					pushOptions.put("escalationMins",
							options.get("pushEscalationMins"));
				}
				
				JSONObject features = new JSONObject();
				features.put("pushOptions", pushOptions);

				request.put("features", features);
			}

			//System.out.println("Request: " + request.toString());
			
			// Execute the request
			response = httpPost(workspaceId, request.toString());

		} catch (JSONException e) {
			throw new WhispirAPIException(
					"Error occurred parsing the object with the content provided."
							+ e.getMessage());
		}

		return response;
	}

	// ***************************************************
	// * Private Methods
	// ***************************************************
	private int httpPost(String workspace, String jsonContent)
			throws WhispirAPIException {
		HttpPost httpPost = (HttpPost) createPost(workspace, jsonContent);
		return executeRequest(httpPost);
	}

	private int executeRequest(HttpRequestBase httpRequest)
			throws WhispirAPIException {
		int statusCode = 0;

		CredentialsProvider credsProvider = new BasicCredentialsProvider();

		Credentials creds = new UsernamePasswordCredentials(this.username,
				this.password);

		if (debug) {
			credsProvider.setCredentials(AuthScope.ANY, creds);
		} else {
			credsProvider.setCredentials(
					new AuthScope(this.getHost(), -1), creds);
		}
		
		CloseableHttpClient client = HttpClients.custom()
				.setDefaultCredentialsProvider(credsProvider).build();

		try {
			
			if(proxyEnabled) {
				httpRequest.setConfig(this.proxy);
			}
			
			CloseableHttpResponse response = client.execute(httpRequest);
			
			try {
				statusCode = response.getStatusLine().getStatusCode();

				if (statusCode == 403) {
					// Check the headers to see if it was an over QPS issue

					Header[] h = response.getHeaders("X-Mashery-Error-Code");

					if (h != null && h.length > 0) {

						for (int i = 0; i < h.length; i++) {
							if ("ERR_403_DEVELOPER_OVER_QPS".equals(h[i]
									.getValue())) {

								// Wait for 1 second and try the request again.
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									// Ended sleep. Continue.
								}

								response = client.execute(httpRequest);
								statusCode = response.getStatusLine()
										.getStatusCode();
							}
						}
					}
				}
			} finally {
				response.close();
			}

		} catch (IOException e) {
			System.err.println("Message Failed - Connection Error: " + e.getMessage());
		} finally {
			// Release the connection.
			try {
				client.close();
			} catch (IOException e) {
				throw new WhispirAPIException(e.getMessage());
			}
		}
		
		return statusCode;
	}

	private HttpRequestBase createPost(String workspaceId, String content)
			throws WhispirAPIException {
		// Create a method instance.
		String url = buildUrl(workspaceId);

		HttpPost httpPost = new HttpPost(url);

		httpPost.setHeader("Content-Type", WHISPIR_MESSAGE_HEADER_V1);
		httpPost.setHeader("Accept", WHISPIR_MESSAGE_HEADER_V1);
		
		try {
			StringEntity body = new StringEntity(content);
			httpPost.setEntity(body);
		} catch(UnsupportedEncodingException e) {
			throw new WhispirAPIException(e.getMessage());
		}
		
		return httpPost;
	}

	private String getHost() {
		if (debug) {	
			return this.debugHost;
		} else {
			return API_HOST;
		}
	}
	
	private String getScheme(String host) {
		if(host.indexOf("app") > -1) {
			return "http://";
		} else {
			return API_SCHEME;
		}
	}

	private String buildUrl(String workspaceId) {
		String url = "";
		// Set the host to either the debug host or the production host
		// depending on the debug setting
		String host = getHost();
		String scheme = getScheme(host);

		if (workspaceId != null && !"".equals(workspaceId)) {
			url = scheme + host + "/workspaces/" + workspaceId + "/messages"
					+ API_EXT + this.apikey;
		} else {
			url = scheme + host + "/messages" + API_EXT + this.apikey;
		}

		return url;
	}
}

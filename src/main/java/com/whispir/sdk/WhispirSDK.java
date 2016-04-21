package com.whispir.sdk;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.whispir.sdk.exceptions.WhispirSDKException;
import com.whispir.sdk.impl.MessageHelperImpl;
import com.whispir.sdk.impl.ScenarioHelperImpl;
import com.whispir.sdk.impl.WorkspaceHelperImpl;
import com.whispir.sdk.interfaces.MessageHelper;
import com.whispir.sdk.interfaces.ScenarioHelper;
import com.whispir.sdk.interfaces.WorkspaceHelper;

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

public class WhispirSDK implements MessageHelper, WorkspaceHelper,
		ScenarioHelper {

	private String apikey;
	private String username;
	private String password;

	// Used for debugging/testing purposes
	private String debugHost;
	private boolean debug;

	// Used for proxy purposes
	private RequestConfig proxy;
	private boolean proxyEnabled;
	private Credentials proxyCredentials;

	// Helpers for Modularisation of the code
	MessageHelper messageHelper;
	WorkspaceHelper workspaceHelper;
	ScenarioHelper scenarioHelper;

	@SuppressWarnings("unused")
	private WhispirSDK() {
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

	public WhispirSDK(String apikey, String username, String password)
			throws WhispirSDKException {
		this(apikey, username, password, "");
	}

	/**
	 * Instantiates the WhispirAPI object.
	 * 
	 * Requires the four parameters to be provided. DebugHost can be provided in
	 * the form xxxxxxx.whispir.net:8080 / xxxx.whispir.com
	 * 
	 * @param apikey
	 * @param username
	 * @param password
	 * @param debugHost
	 */

	public WhispirSDK(String apikey, String username, String password,
			String debugHost) throws WhispirSDKException {

		if (apikey.equals(null) || username.equals(null)
				|| password.equals(null)) {
			throw new WhispirSDKException(WhispirSDKConstants.NO_AUTH_ERROR);
		}

		if ("".equals(apikey) || "".equals(username) || "".equals(password)) {
			throw new WhispirSDKException(WhispirSDKConstants.NO_AUTH_ERROR);
		}

		this.apikey = apikey;
		this.username = username;
		this.password = password;
		this.proxyEnabled = false;
		this.proxyCredentials = null;

		if (debugHost != null && !"".equals(debugHost)) {
			this.setDebugHost(debugHost);
		}

		initHelpers();
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
			debugHost = debugHost.replaceAll("/api", "");
			this.debugHost = debugHost;
			this.debug = true;
		} else {
			this.debug = false;
		}
	}

	public void setProxy(String host, int port, boolean httpsEnabled) {
		this.setProxy(host, port, httpsEnabled, "", "");
	}

	public void setProxy(String host, int port, boolean httpsEnabled,
			String proxyUsername, String proxyPassword) {

		String scheme = "http";

		if (httpsEnabled) {
			scheme = "https";
		}

		this.proxy = RequestConfig.custom()
				.setProxy(new HttpHost(host, port, scheme)).build();
		this.proxyEnabled = true;

		if (!"".equals(proxyUsername) && !"".equals(proxyPassword)) {
			this.proxyCredentials = new UsernamePasswordCredentials(
					proxyUsername, proxyPassword);
		}
	}

	// ***************************************************
	// * Messages SDK Methods
	// ***************************************************

	public WhispirResponse sendMessage(String recipient, String subject, String content)
			throws WhispirSDKException {
		return this.messageHelper.sendMessage(recipient, subject, content);
	}

	public WhispirResponse sendMessage(String workspaceId, String recipient,
			String subject, String content) throws WhispirSDKException {
		return this.messageHelper.sendMessage(workspaceId, recipient, subject,
				content);
	}

	public WhispirResponse sendMessage(String workspaceId, String recipient,
			String subject, Map<String, String> content)
			throws WhispirSDKException {
		return this.messageHelper.sendMessage(workspaceId, recipient, subject,
				content);
	}

	public WhispirResponse sendMessage(String workspaceId, String recipient,
			String subject, Map<String, String> content,
			Map<String, String> options) throws WhispirSDKException {
		return this.messageHelper.sendMessage(workspaceId, recipient, subject,
				content, options);
	}
	
	public WhispirResponse getMessages() throws WhispirSDKException {
		return this.messageHelper.getMessages();
	}
	
	public WhispirResponse getMessage(String messageId) throws WhispirSDKException {
		return this.messageHelper.getMessage(messageId);
	}

	// ***************************************************
	// * Workspaces SDK Methods
	// ***************************************************

	public WhispirResponse getWorkspaces() throws WhispirSDKException {
		return this.workspaceHelper.getWorkspaces();
	}
	
	public WhispirResponse getWorkspace(String workspaceId) throws WhispirSDKException {
		return this.workspaceHelper.getWorkspace(workspaceId);
	}
	
	public WhispirResponse createWorkspace(Map<String, String> details) throws WhispirSDKException {
		return this.workspaceHelper.createWorkspace(details);
	}

	// ***************************************************
	// * Scenarios SDK Methods
	// ***************************************************

	public WhispirResponse getScenarios() throws WhispirSDKException {
		return this.scenarioHelper.getScenarios();
	}

	public WhispirResponse getScenarios(String workspaceId)
			throws WhispirSDKException {
		return this.scenarioHelper.getScenarios(workspaceId);
	}

	public WhispirResponse sendScenario(String workspaceId, String scenarioId)
			throws WhispirSDKException {
		return this.scenarioHelper.sendScenario(workspaceId, scenarioId);
	}
	
	public WhispirResponse createScenario(String workspaceId, String recipients, Map<String, String> details,
			Map<String, String> content) throws WhispirSDKException {
		return this.scenarioHelper.createScenario(workspaceId, recipients, details, content);
	}

	public WhispirResponse createScenario(String recipients, Map<String, String> details,
			Map<String, String> content) throws WhispirSDKException {
		return createScenario("", recipients, details, content);
	}
	
	// ***************************************************
	// * POST Methods
	// ***************************************************
	
	public WhispirResponse post(String resourceType, String workspaceId, String jsonContent)
			throws WhispirSDKException {
		return this.post(resourceType, "", workspaceId, jsonContent);
	}

	public WhispirResponse post(String resourceType, String resourceId, String workspaceId,
			String jsonContent) throws WhispirSDKException {
		HttpPost httpPost = (HttpPost) createPost(resourceType, resourceId,
				workspaceId, jsonContent);
		return executeRequest(httpPost);
	}
	
	private HttpRequestBase createPost(String resourceType, String resourceId,
			String workspaceId, String content) throws WhispirSDKException {

		String url = buildUrl(workspaceId, resourceType, resourceId);

		// Create a method instance.
		HttpPost httpPost = new HttpPost(url);

		setHeaders(httpPost, resourceType);

		try {
			StringEntity body = new StringEntity(content);
			httpPost.setEntity(body);
		} catch (UnsupportedEncodingException e) {
			throw new WhispirSDKException(e.getMessage());
		}

		return httpPost;
	}

	// ***************************************************
	// * GET Methods
	// ***************************************************

	public WhispirResponse get(String resourceType, String workspaceId, String resourceId)
			throws WhispirSDKException {
		HttpGet httpGet = (HttpGet) createGet(resourceType, workspaceId, resourceId);
		return executeRequest(httpGet);
	}
	
	public WhispirResponse get(String resourceType, String workspaceId)
			throws WhispirSDKException {
		return this.get(resourceType, workspaceId, ""); 
	}

	private HttpRequestBase createGet(String resourceType, String workspaceId, String resourceId)
			throws WhispirSDKException {
		// Create a method instance.
		String url = buildUrl(workspaceId, resourceType, resourceId);

		HttpGet httpGet = new HttpGet(url);

		setHeaders(httpGet, resourceType);

		return httpGet;
	}
	
	
	// ***************************************************
	// * DELETE Methods
	// ***************************************************

	public WhispirResponse delete(String resourceType, String workspaceId, String resourceId)
			throws WhispirSDKException {
		HttpDelete httpDelete = (HttpDelete) createDelete(resourceType, workspaceId, resourceId);
		return executeRequest(httpDelete);
	}

	private HttpRequestBase createDelete(String resourceType, String workspaceId, String resourceId)
			throws WhispirSDKException {
		// Create a method instance.
		String url = buildUrl(workspaceId, resourceType, resourceId);

		HttpDelete httpDelete = new HttpDelete(url);

		setHeaders(httpDelete, resourceType);

		return httpDelete;
	}

	
	// ***************************************************
	// * Private Methods
	// ***************************************************
	
	private void initHelpers() {
		this.messageHelper = new MessageHelperImpl(this);
		this.workspaceHelper = new WorkspaceHelperImpl(this);
		this.scenarioHelper = new ScenarioHelperImpl(this);
	}
	
	private void setHeaders(HttpRequestBase request, String resourceType)
			throws WhispirSDKException {

		String header = "";

		switch (resourceType) {
		case WhispirSDKConstants.MESSAGES_RESOURCE:
			header = WhispirSDKConstants.WHISPIR_MESSAGE_HEADER_V1;
			break;

		case WhispirSDKConstants.WORKSPACES_RESOURCE:
			header = WhispirSDKConstants.WHISPIR_WORKSPACE_HEADER_V1;
			break;

		case WhispirSDKConstants.SCENARIOS_RESOURCE:
			header = WhispirSDKConstants.WHISPIR_SCENARIO_HEADER_V1;
			break;

		default:
			throw new WhispirSDKException(
					"Resource specified was not found. Expecting Workspaces, Messages or Scenarios");
		}

		request.setHeader("Content-Type", header);
		request.setHeader("Accept", header);
	}

	public String getHost() {
		if (debug) {
			return this.debugHost;
		} else {
			return WhispirSDKConstants.API_HOST;
		}
	}

	private String getScheme(String host) {
		if (host.indexOf("app") > -1) {
			return "http://";
		} else {
			return WhispirSDKConstants.API_SCHEME;
		}
	}

	private String buildUrl(String workspaceId, String resourceType,
			String resourceId) {

		StringBuilder url = new StringBuilder();

		// Set the host to either the debug host or the production host
		// depending on the debug setting
		String host = getHost();
		String scheme = getScheme(host);

		url.append(scheme).append(host);
		
		if(this.debug) {
			url.append("/api");
		}

		if ((workspaceId != null && !"".equals(workspaceId)) || resourceType.equals(WhispirSDKConstants.WORKSPACES_RESOURCE)) {
			url.append("/workspaces/" + workspaceId);
		}

		//check that the resource type doesn't equal WORKSPACE as the URL will end up as:
		//workspaces/:id/workspaces.
		if (resourceType != null && !"".equals(resourceType) && !resourceType.equals(WhispirSDKConstants.WORKSPACES_RESOURCE)) {
			url.append("/" + resourceType);
		}

		if (resourceId != null && !"".equals(resourceId)) {
			url.append("/" + resourceId);
		}

		url.append(WhispirSDKConstants.API_EXT + this.apikey);

		System.out.println("Executing URL: " + url.toString());

		return url.toString();
	}

	private WhispirResponse executeRequest(HttpRequestBase httpRequest)
			throws WhispirSDKException {

		WhispirResponse wr = new WhispirResponse();
		int statusCode = 0;

		final HttpHost targetHost = new HttpHost(this.getHost(), 443, "https");
		CredentialsProvider credsProvider = new BasicCredentialsProvider();

		Credentials creds = new UsernamePasswordCredentials(this.username,
				this.password);
		
		if (debug) {
			credsProvider.setCredentials(AuthScope.ANY, creds);
		} else {
			credsProvider.setCredentials(new AuthScope(this.getHost(), -1),
					creds);
			if (this.proxyEnabled && this.proxyCredentials != null) {
				credsProvider.setCredentials(new AuthScope(this.proxy
						.getProxy().getHostName(), -1), this.proxyCredentials);
			}
		}

		// Create AuthCache Instance
		AuthCache authCache = new BasicAuthCache();
		// Generate BASIC scheme object and add it to the local auth cache
		authCache.put(targetHost, new BasicScheme());
		
		// Add AuthCache to the execution context
		final HttpClientContext context = HttpClientContext.create();
		context.setCredentialsProvider(credsProvider);
		context.setAuthCache(authCache);

		CloseableHttpClient client = HttpClients.custom()
				.setDefaultCredentialsProvider(credsProvider).build();

		try {

			if (proxyEnabled) {
				httpRequest.setConfig(this.proxy);
			}

			CloseableHttpResponse response = client.execute(targetHost, httpRequest, context);

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

								response = client.execute(targetHost, httpRequest, context);
								statusCode = response.getStatusLine()
										.getStatusCode();
							}
						}
					}
				}

				wr.setStatusCode(statusCode);
				
				//Check if this is a delete
				if(statusCode != 204) {
					wr.setRawResponse(EntityUtils.toString(response.getEntity()));
				}
				
				Map<String,String> headerMap = new HashMap<String,String>();
				
				HeaderIterator headers = response.headerIterator();
				
				while(headers.hasNext()) {
					Header h = headers.nextHeader();
					headerMap.put(h.getName(), h.getValue());
				}
				
				wr.setResponseHeaders(headerMap);

			} finally {
				EntityUtils.consume(response.getEntity());
				response.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Message Failed - Connection Error: "
					+ e.getMessage());
		} finally {
			// Release the connection.
			try {
				client.close();
			} catch (IOException e) {
				throw new WhispirSDKException(e.getMessage());
			}
		}

		return wr;
	}

}

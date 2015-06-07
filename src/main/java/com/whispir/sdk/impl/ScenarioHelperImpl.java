package com.whispir.sdk.impl;

import java.util.Map;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.whispir.sdk.WhispirResponse;
import com.whispir.sdk.WhispirSDK;
import com.whispir.sdk.WhispirSDKConstants;
import com.whispir.sdk.exceptions.WhispirSDKException;
import com.whispir.sdk.interfaces.ScenarioHelper;

public class ScenarioHelperImpl extends BaseHelperImpl implements
		ScenarioHelper {

	public ScenarioHelperImpl(WhispirSDK sdk) {
		super(sdk);
	}

	@Override
	public WhispirResponse getScenarios() throws WhispirSDKException {
		return this.getScenarios("");
	}

	@Override
	public WhispirResponse getScenarios(String workspaceId)
			throws WhispirSDKException {
		WhispirResponse response = sdk.get(
				WhispirSDKConstants.SCENARIOS_RESOURCE, workspaceId);

		Map<String, String> map = new TreeMap<String, String>();

		try {
			JSONObject obj = new JSONObject(response.getRawResponse());

			JSONArray scenarios = obj.getJSONArray("scenarios");
			int scenarioLength = scenarios.length();

			for (int i = 0; i < scenarioLength; i++) {
				String scenarioName = (String) scenarios.getJSONObject(i).get(
						"title");
				String fullUrl = (String) scenarios.getJSONObject(i)
						.getJSONArray("link").getJSONObject(0).get("uri");

				String id = fullUrl.substring(fullUrl.lastIndexOf("/") + 1,
						fullUrl.lastIndexOf("?"));

				System.out.println(scenarioName + " " + id);

				map.put(scenarioName, id);
			}

		} catch (JSONException e) {
			throw new WhispirSDKException(e.getMessage());
		}

		response.setResponse(map);

		return response;
	}

	@Override
	public WhispirResponse sendScenario(String workspaceId, String scenarioId)
			throws WhispirSDKException {
		return sdk.post(WhispirSDKConstants.SCENARIOS_RESOURCE, scenarioId, workspaceId, "");
	}
	
	@Override
	public WhispirResponse createScenario(String recipients,
			Map<String, String> details, Map<String, String> content) throws WhispirSDKException {
		
		return createScenario("", recipients, details, content);
	}

	@Override
	public WhispirResponse createScenario(String workspaceId, String recipients,
			Map<String, String> details, Map<String, String> content) throws WhispirSDKException {
		
		WhispirResponse response = new WhispirResponse();
		
		if (recipients == null || recipients.length() < 8) {
			// error with the recipient information, returning HTTP 422.
			response.setStatusCode(422);
			return response;
		}
		
		JSONObject scenario = new JSONObject();
		JSONObject message = new JSONObject();
		
		try {
			scenario.put("title", details.get("title"));
			scenario.put("description", details.get("description"));
			scenario.put("allowedUsers", details.get("allowedUsers"));
			
			message.put("to", recipients);
			message.put("subject", content.get("subject"));
			message.put("body", content.get("body"));
			
			scenario.put("message", message);
			
			response = sdk.post(WhispirSDKConstants.SCENARIOS_RESOURCE, workspaceId, scenario.toString());
			
			return response;
			
		} catch (JSONException e) {
			throw new WhispirSDKException(
					"Error occurred parsing the object with the content provided."
							+ e.getMessage());
		}
	}
}

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
		WhispirResponse response = sdk.get(
				WhispirSDKConstants.SCENARIOS_RESOURCE, "");

		Map<String, String> map = new TreeMap<String, String>();

		try {
			JSONObject obj = new JSONObject(response.getRawResponse());

			JSONArray scenarios = obj.getJSONArray("scenarios");
			int scenarioLength = scenarios.length();

			for (int i = 0; i < scenarioLength; i++) {
				String scenarioName = (String) scenarios.getJSONObject(i)
						.get("title");
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
	public int sendScenario(String scenarioId) throws WhispirSDKException {
		// TODO Auto-generated method stub
		return 0;
	}
}

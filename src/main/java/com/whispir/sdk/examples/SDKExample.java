package com.whispir.sdk.examples;

import java.util.Map;
import java.util.TreeMap;

import com.whispir.sdk.WhispirResponse;
import com.whispir.sdk.WhispirSDK;
import com.whispir.sdk.exceptions.WhispirSDKException;

public class SDKExample {

	public static final String API_KEY = "...";
	public static final String USERNAME = "...";
	public static final String PASSWORD = "...";

	public static void main(String[] args) {
		try {

			// INIT API object
			WhispirSDK sdk = new WhispirSDK(API_KEY, USERNAME, PASSWORD);

			// Send the message
			WhispirResponse response = sdk.sendMessage("61400000000",
					"This is the subject of my SMS",
					"This is the content of my SMS");

			// Status should be 202 Accepted
			System.out.println("Status: " + response.getStatusCode());

			//------------------------------------------------------
			
			// Retrieve a list of workspaces
			WhispirResponse workspaceResponse = sdk.getWorkspaces();

			TreeMap<String, String> workspaces = (TreeMap<String, String>) workspaceResponse
					.getResponse();

			// Iterate through the workspaces, printing the names and IDs out
			for (Map.Entry<String, String> entry : workspaces.entrySet()) {
				String workspaceName = entry.getKey();
				String workspaceId = entry.getValue();

				System.out.println(workspaceName + " => " + workspaceId);
			}
			
			//------------------------------------------------------
			
			//Retrieve a list of scenarios
			
			WhispirResponse scenariosResponse = sdk.getScenarios();
			
			TreeMap<String, String> scenarios = (TreeMap<String, String>) scenariosResponse
					.getResponse();

			// Iterate through the workspaces, printing the names and IDs out
			for (Map.Entry<String, String> entry : scenarios.entrySet()) {
				String scenarioName = entry.getKey();
				String scenarioId = entry.getValue();

				System.out.println(scenarioName + " => " + scenarioId);
			}
			
			//execute the scenario
			
			String scenarioId = "...";
			String workspaceId = "...";
			
			response = sdk.sendScenario(workspaceId, scenarioId);
			
			// Status should be 202 Accepted
			System.out.println("Status: " + response.getStatusCode());

		} catch (WhispirSDKException e) {
			e.printStackTrace();
		}
	}
}

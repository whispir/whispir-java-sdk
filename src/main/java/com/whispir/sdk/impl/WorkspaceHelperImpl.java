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
import com.whispir.sdk.interfaces.WorkspaceHelper;

public class WorkspaceHelperImpl extends BaseHelperImpl implements WorkspaceHelper {

	public WorkspaceHelperImpl(WhispirSDK sdk) {
		super(sdk);
	}

	/**
	 * <p>
	 * Allows a user to retrieve a map of workspaces and their corresponding IDs
	 * @return map - the map of names and ids for each workspace
	 */
	@Override
	public WhispirResponse getWorkspaces() throws WhispirSDKException {
		WhispirResponse response = this.getWorkspace("");
		
		Map<String, String> map = new TreeMap<String,String>();
		
		try {
			
			if(response.getStatusCode() != 200) {
				return response;
			}
			
			JSONObject obj = new JSONObject(response.getRawResponse());
			
			JSONArray workspaces = obj.getJSONArray("workspaces");
			int workspacesLength = workspaces.length();
			
			for (int i = 0; i < workspacesLength; i++) {
				String workspaceName = (String) workspaces.getJSONObject(i).get("projectName");
				String fullUrl = (String)workspaces.getJSONObject(i).getJSONArray("link").getJSONObject(0).get("uri");
				
				String id = fullUrl.substring(fullUrl.lastIndexOf("/") + 1, fullUrl.lastIndexOf("?"));
				
				map.put(workspaceName, id);
			}
			
		} catch (JSONException e) {
			throw new WhispirSDKException(e.getMessage());
		}
		
		response.setResponse(map);
		
		return response;
	}
	
	/**
	 * <p>
	 * Allows a user to search for a specific Workspace by ID
	 * @return WhispirResponse the workspace that was searched for
	 */
	@Override
	public WhispirResponse getWorkspace(String workspaceId) throws WhispirSDKException {
		WhispirResponse response = sdk.get(WhispirSDKConstants.WORKSPACES_RESOURCE, workspaceId);
		Map<String, String> map = new TreeMap<String,String>();
		
		if("".equals(workspaceId) || workspaceId == null){
			return response;
		}
		
		try {
			JSONObject obj = new JSONObject(response.getRawResponse());
			
			map.put("name", obj.getString("projectName"));
			map.put("number", obj.getString("projectNumber"));
			map.put("billingcostcentre", obj.getString("billingcostcentre"));
			map.put("id", workspaceId);
			
			response.setResponse(map);
			
		} catch (JSONException e) {
			throw new WhispirSDKException(e.getMessage());
		}
		
		return response;
	}
	
	@Override
	public WhispirResponse createWorkspace(Map<String, String> details) throws WhispirSDKException {
		
		WhispirResponse response = new WhispirResponse();
		
		if (details == null) {
			return response;
		}
		
		JSONObject workspace = new JSONObject();
		
		try {
			workspace.put("projectName", details.get("name"));
			workspace.put("projectNumber", details.get("number"));
			workspace.put("status", "A");
			workspace.put("billingcostcentre", details.get("billingcostcentre"));
			
			response = sdk.post(WhispirSDKConstants.WORKSPACES_RESOURCE, "", workspace.toString());
			
		} catch (JSONException e) {
			throw new WhispirSDKException(
					"Error occurred parsing the object with the content provided."
							+ e.getMessage());
		}
		
		return response;
	}
}

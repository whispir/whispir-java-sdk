package com.whispir.sdk.impl;

import java.util.Map;

import com.whispir.sdk.WhispirResponse;
import com.whispir.sdk.WhispirSDK;
import com.whispir.sdk.WhispirSDKConstants;
import com.whispir.sdk.exceptions.WhispirSDKException;
import com.whispir.sdk.interfaces.WorkspaceHelper;

public class WorkspaceHelperImpl implements WorkspaceHelper {

	private WhispirSDK sdk;

	@SuppressWarnings("unused")
	private WorkspaceHelperImpl() {
	}

	public WorkspaceHelperImpl(WhispirSDK sdk) {
		this.sdk = sdk;
	}

	/**
	 * <p>
	 * Allows a user to retrieve a map of workspaces and their corresponding IDs
	 * @return map - the map of names and ids for each workspace
	 */
	@Override
	public Map<String, String> getWorkspaces() throws WhispirSDKException {
		WhispirResponse response = sdk.get(WhispirSDKConstants.WORKSPACES_RESOURCE, "");
		return response.getResponse();
		
	}

}

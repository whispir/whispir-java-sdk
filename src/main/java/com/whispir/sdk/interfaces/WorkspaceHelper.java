package com.whispir.sdk.interfaces;

import java.util.Map;

import com.whispir.sdk.exceptions.WhispirSDKException;

public interface WorkspaceHelper {
	public Map<String,String> getWorkspaces() throws WhispirSDKException;
}

package com.whispir.sdk.interfaces;

import java.util.Map;

import com.whispir.sdk.WhispirResponse;
import com.whispir.sdk.exceptions.WhispirSDKException;
/**
 * @author jordan
 * @version 1.0
 * @since 2.0
 * 
 * <p>Interface that must be implemented by both the WhispirSDK object and the implementing object.</p>
 * <p>This ensures a contract is upheld between the SDK's exposed methods, and the implementation classes in the background</p>
 */
public interface ScenarioHelper {
	public WhispirResponse getScenarios() throws WhispirSDKException;
	public WhispirResponse getScenarios(String workspaceId) throws WhispirSDKException;
	public WhispirResponse createScenario(String recipients, Map<String, String> details, Map<String,String> content) throws WhispirSDKException;
	public WhispirResponse createScenario(String workspaceId, String recipients, Map<String, String> details, Map<String,String> content) throws WhispirSDKException;	
	public WhispirResponse sendScenario(String workspaceId, String scenarioId) throws WhispirSDKException;
}

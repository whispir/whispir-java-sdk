package com.whispir.sdk.interfaces;

import java.util.Map;

import com.whispir.sdk.WhispirResponse;
import com.whispir.sdk.exceptions.WhispirSDKException;
/**
 * @author jordan
 * @version 2.1
 * @since 2.0
 * 
 * <p>Interface that must be implemented by both the WhispirSDK object and the implementing object.</p>
 * <p>This ensures a contract is upheld between the SDK's exposed methods, and the implementation classes in the background</p>
 */


public interface MessageHelper {

	public WhispirResponse sendMessage(String recipient, String subject, String content)
			throws WhispirSDKException;

	public WhispirResponse sendMessage(String workspaceId, String recipient,
			String subject, String content) throws WhispirSDKException;

	public WhispirResponse sendMessage(String workspaceId, String recipient,
			String subject, Map<String, String> content)
			throws WhispirSDKException;
	
	public WhispirResponse sendMessage(String workspaceId, String recipient,
			String subject, Map<String, String> content,
			Map<String, String> options) throws WhispirSDKException;
	
	public WhispirResponse getMessages() throws WhispirSDKException;
	public WhispirResponse getMessage(String messageId) throws WhispirSDKException;
}

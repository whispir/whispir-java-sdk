package com.whispir.sdk.tests;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.whispir.sdk.WhispirSDK;
import com.whispir.sdk.exceptions.WhispirSDKException;

public class WhispirSDKTest {
	
	protected WhispirSDK whispirSDK;

	// POPULATE THESE OR ALL THE TESTS WILL FAIL!!!

	protected static final String TEST_API_KEY = "";
	protected static final String TEST_USERNAME = "";
	protected static final String TEST_PASSWORD = "";

	// Message content variables for the tests
	protected static final String TEST_RECIPIENT = "";
	protected static final String TEST_WORKSPACE_ID = "";
	protected static final String TEST_MESSAGE_SUBJECT = "";
	protected static final String TEST_MESSAGE_BODY = "";

	// Debugging
	protected static final String DEBUG_HOST = "";
	
	// Proxy Debugging (I installed Squid on my Mac from here http://squidman.net/squidman/index.html)
	protected static final String PROXY_HOST = "";
	protected static final int PROXY_PORT = 0;
	protected static final boolean PROXY_HTTPS_ENABLED = false;

	@Before
	public void setUp() throws Exception {
		if (!"".equals(DEBUG_HOST)) {
			whispirSDK = new WhispirSDK(TEST_API_KEY, TEST_USERNAME,
					TEST_PASSWORD, DEBUG_HOST);
		} else {
			whispirSDK = new WhispirSDK(TEST_API_KEY, TEST_USERNAME,
					TEST_PASSWORD);
		}
		
		if (!"".equals(PROXY_HOST)) {
			whispirSDK.setProxy(PROXY_HOST, PROXY_PORT, PROXY_HTTPS_ENABLED);
		}
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBadAPIKey() throws WhispirSDKException {
		whispirSDK.setApikey("1234");
		whispirSDK.setUsername(TEST_USERNAME);
		whispirSDK.setPassword(TEST_PASSWORD);
		
		int response = whispirSDK.sendMessage(TEST_RECIPIENT, TEST_MESSAGE_SUBJECT, TEST_MESSAGE_BODY);

		if(!"".equals(DEBUG_HOST)) {
			assertTrue(response == 202);
		} else {
			// HTTP403 Permission Denied / Forbidden
			assertTrue(response == 403);
		}
	}

	@Test
	public void testBadUsername() throws WhispirSDKException {
		whispirSDK.setApikey(TEST_API_KEY);
		whispirSDK.setUsername("blahblahblah");
		whispirSDK.setPassword(TEST_PASSWORD);
		
		int response = whispirSDK.sendMessage(TEST_RECIPIENT,
				TEST_MESSAGE_SUBJECT, TEST_MESSAGE_BODY);

		// HTTP401 Unauthorized Access
		assertTrue(response == 401);
	}

	@Test
	public void testBadPassword() throws WhispirSDKException {
		whispirSDK.setApikey(TEST_API_KEY);
		whispirSDK.setUsername(TEST_USERNAME);
		whispirSDK.setPassword("blahblahblah");
		
		int response = whispirSDK.sendMessage(TEST_RECIPIENT,
				TEST_MESSAGE_SUBJECT, TEST_MESSAGE_BODY);

		// HTTP401 Unauthorized Access
		assertTrue(response == 401);
	}
}

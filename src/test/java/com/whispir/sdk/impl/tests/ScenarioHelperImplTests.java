package com.whispir.sdk.impl.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.whispir.sdk.WhispirResponse;
import com.whispir.sdk.exceptions.WhispirSDKException;
import com.whispir.sdk.tests.WhispirSDKTest;

public class ScenarioHelperImplTests extends WhispirSDKTest {

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void testGetScenarios() throws WhispirSDKException {
		whispirSDK.setApikey(TEST_API_KEY);
		whispirSDK.setUsername(TEST_USERNAME);
		whispirSDK.setPassword(TEST_PASSWORD);

		WhispirResponse response = whispirSDK.getScenarios();

		assertEquals(response.getStatusCode(), 200);
		assertTrue(response.getResponse().size() > 0);
	}

	@Test
	public void testGetWorkspaceScenarios() throws WhispirSDKException {
		whispirSDK.setApikey(TEST_API_KEY);
		whispirSDK.setUsername(TEST_USERNAME);
		whispirSDK.setPassword(TEST_PASSWORD);

		WhispirResponse response = whispirSDK.getScenarios(TEST_WORKSPACE_ID);

		assertEquals(response.getStatusCode(), 200);
		assertTrue(response.getResponse().size() > 0);
	}
}

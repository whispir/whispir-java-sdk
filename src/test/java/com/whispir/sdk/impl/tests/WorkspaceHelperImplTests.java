package com.whispir.sdk.impl.tests;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.whispir.sdk.exceptions.WhispirSDKException;
import com.whispir.sdk.tests.WhispirSDKTest;

public class WorkspaceHelperImplTests extends WhispirSDKTest {

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void testGetWorkspaces() throws WhispirSDKException {
		whispirSDK.setApikey(TEST_API_KEY);
		whispirSDK.setUsername(TEST_USERNAME);
		whispirSDK.setPassword(TEST_PASSWORD);
		
		HashMap<String,String> response = (HashMap<String, String>) whispirSDK.getWorkspaces();

		// HTTP422 Unprocessable Entity
		assertTrue(response.size() > 0);
	}

}

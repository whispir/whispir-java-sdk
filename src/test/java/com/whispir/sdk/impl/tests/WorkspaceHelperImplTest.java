package com.whispir.sdk.impl.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.whispir.sdk.WhispirResponse;
import com.whispir.sdk.exceptions.WhispirSDKException;
import com.whispir.sdk.tests.WhispirSDKTest;

public class WorkspaceHelperImplTest extends WhispirSDKTest {

	@Before
	public void setUp() throws Exception {
		super.setUp();
		whispirSDK.setApikey(TEST_API_KEY);
		whispirSDK.setUsername(TEST_USERNAME);
		whispirSDK.setPassword(TEST_PASSWORD);
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void testGetWorkspaces() throws WhispirSDKException {
		if(!"".equals(TEST_WORKSPACE_ID)) {
			WhispirResponse response = whispirSDK.getWorkspaces();
	
			assertEquals(response.getStatusCode(), 200);
			assertTrue(response.getResponse().size() > 0);
		} else {
			assertTrue(true);
		}
	}
	
	@Test
	public void testCreateWorkspace() throws WhispirSDKException {
		
		//Scenarios need content, and they need recipients
		Map<String, String> details = new HashMap<String, String>();
		
		Random randomGenerator = new Random();
		
		String rand = Integer.toString(randomGenerator.nextInt(100000));
		
		details.put("name", "Test Workspace From SDK " + rand);
		details.put("number", rand);
		details.put("billingcostcentre", rand);
		
		WhispirResponse response = whispirSDK.createWorkspace(details);
		
		assertEquals(response.getStatusCode(), 201);
	}
	
	@Test
	public void testGetSingleWorkspace() throws WhispirSDKException {
		
		if(!"".equals(TEST_WORKSPACE_ID)) {
			WhispirResponse workspace = whispirSDK.getWorkspace(TEST_WORKSPACE_ID);
			assertTrue(workspace.getResponse() != null);
			
			if(!"".equals(TEST_WORKSPACE_NAME)){
				assertTrue(TEST_WORKSPACE_NAME.equals(workspace.getResponse().get("name")));
			}
		}
	}

}

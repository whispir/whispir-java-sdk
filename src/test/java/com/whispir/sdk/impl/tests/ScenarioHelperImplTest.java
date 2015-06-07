package com.whispir.sdk.impl.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.whispir.sdk.WhispirResponse;
import com.whispir.sdk.WhispirSDKConstants;
import com.whispir.sdk.exceptions.WhispirSDKException;
import com.whispir.sdk.tests.WhispirSDKTest;

public class ScenarioHelperImplTest extends WhispirSDKTest {
	
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
	public void testGetScenarios() throws WhispirSDKException {
		WhispirResponse response = whispirSDK.getScenarios();

		assertEquals(response.getStatusCode(), 200);
		assertTrue(response.getResponse().size() > 0);
	}

	@Test
	public void testGetWorkspaceScenarios() throws WhispirSDKException {
		WhispirResponse response = whispirSDK.getScenarios(TEST_WORKSPACE_ID);

		assertEquals(response.getStatusCode(), 200);
		assertTrue(response.getResponse().size() > 0);
	}
	
	@Test
	public void testCreateScenarioWithNoRecipients() throws WhispirSDKException {
		
		//Scenarios need content, and they need recipients
		Map<String, String> details = new HashMap<String, String>();

		details.put("title", "Test Scenario");
		details.put("description", "Test Scenario Description");
		details.put("allowedUsers", "EVERYONE");
		
		Map<String, String> content = new HashMap<String, String>();
		content.put("subject", "This is the subject");
		content.put("body", "This is the body");
		
		WhispirResponse response = whispirSDK.createScenario("", details, content);

		assertEquals(response.getStatusCode(), 422);
	}
	
	@Test
	public void testCreateCompanyScenario() throws WhispirSDKException {
		
		//Scenarios need content, and they need recipients
		Map<String, String> details = new HashMap<String, String>();

		details.put("title", "Test Scenario");
		details.put("description", "Test Scenario Description");
		details.put("allowedUsers", "EVERYONE");
		
		Map<String, String> content = new HashMap<String, String>();
		content.put("subject", "This is the subject");
		content.put("body", "This is the body");
		
		WhispirResponse response = whispirSDK.createScenario(TEST_RECIPIENT, details, content);
	
		assertEquals(response.getStatusCode(), 201);
		
		String[] url = response.getResponseHeaders().get("Location").split("/");
		String id = "";
		
		for(String part : url) {
			if(part.contains("?")) {
				id = part.split("\\?")[0];
			}
		}
		
		assertTrue(!"".equals(id));
		
		System.out.println("Scenario ID: " + id);
		
		//Send the Scenario.  Expected statuscode is 202
		//JWalsh 7/6/15 - There is currently a limitation where you can only invoke scenarios from within workspaces, 
		//not the default workspace. This will be fixed in a subsequent release of Whispir.
		
		/*response = whispirSDK.sendScenario("", id);
		System.out.println(response.getStatusCode());
		assertTrue(response.getStatusCode() == 202);*/
		
		response = whispirSDK.delete(WhispirSDKConstants.SCENARIOS_RESOURCE, "", id);
		System.out.println(response.getStatusCode());
		assertTrue(response.getStatusCode() == 204);
		
	}
	
	@Test
	public void testCreateAndSendWorkspaceScenario() throws WhispirSDKException {
		
		//Scenarios need content, and they need recipients
		Map<String, String> details = new HashMap<String, String>();

		details.put("title", "Test Scenario");
		details.put("description", "Test Scenario Description");
		details.put("allowedUsers", "EVERYONE");
		
		Map<String, String> content = new HashMap<String, String>();
		content.put("subject", "This is the subject");
		content.put("body", "This is the body");
		
		WhispirResponse response = whispirSDK.createScenario(TEST_WORKSPACE_ID, TEST_RECIPIENT, details, content);
		
		assertEquals(response.getStatusCode(), 201);
		
		String[] url = response.getResponseHeaders().get("Location").split("/");
		String id = "";
		
		for(String part : url) {
			if(part.contains("?")) {
				id = part.split("\\?")[0];
			}
		}
		
		//Ensure that we've got an ID for the scenario out of the API
		assertTrue(!"".equals(id));
		
		System.out.println("Scenario ID: " + id);
		
		//Send the Scenario.  Expected statuscode is 202
		response = whispirSDK.sendScenario(TEST_WORKSPACE_ID, id);
		System.out.println(response.getStatusCode());
		assertTrue(response.getStatusCode() == 202);
		
		//Delete the Scenario.  Expected statuscode is 204
		response = whispirSDK.delete(WhispirSDKConstants.SCENARIOS_RESOURCE, TEST_WORKSPACE_ID, id);
		System.out.println(response.getStatusCode());
		assertTrue(response.getStatusCode() == 204);
		
	}
}

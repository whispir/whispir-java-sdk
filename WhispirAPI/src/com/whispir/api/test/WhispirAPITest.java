package com.whispir.api.test;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.whispir.api.WhispirAPI;
import com.whispir.api.exceptions.WhispirAPIException;

public class WhispirAPITest {
	
	private WhispirAPI whispirAPI;
	private static final String TEST_API_KEY = "4fcn8xkeherbdm5y5fpnat8g";
	private static final String TEST_USERNAME = "jordan.walsh";
	private static final String TEST_PASSWORD = "12345678";
	
	
	@Before
	public void setUp() throws Exception {
		whispirAPI = new WhispirAPI(TEST_API_KEY, TEST_USERNAME, TEST_PASSWORD);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testBadAPIKey() throws WhispirAPIException {
		whispirAPI.setApikey("1234");
		whispirAPI.setUsername(TEST_USERNAME);
		whispirAPI.setPassword(TEST_PASSWORD);
		
		int response = whispirAPI.sendMessage("61423556682", "subject", "content");
		
		//HTTP403 Permission Denied / Forbidden
		assertTrue(response == 403);
	}
	
	@Test
	public void testBadUsername() throws WhispirAPIException {		
		whispirAPI.setApikey(TEST_API_KEY);
		whispirAPI.setUsername("blahblahblah");
		whispirAPI.setPassword(TEST_PASSWORD);
		
		int response = whispirAPI.sendMessage("61423556682", "subject", "content");
		
		//HTTP401 Unauthorized Access
		assertTrue(response == 401);
	}
	
	@Test
	public void testBadPassword() throws WhispirAPIException {
		whispirAPI.setApikey(TEST_API_KEY);
		whispirAPI.setUsername(TEST_USERNAME);
		whispirAPI.setPassword("blahblahblah");
		
		int response = whispirAPI.sendMessage("61423556682", "subject", "content");
		
		//HTTP401 Unauthorized Access
		assertTrue(response == 401);
	}
	
	@Test
	public void testBadRecipient() throws WhispirAPIException {
		whispirAPI.setApikey(TEST_API_KEY);
		whispirAPI.setUsername(TEST_USERNAME);
		whispirAPI.setPassword(TEST_PASSWORD);
		
		int response = whispirAPI.sendMessage("1", "Incident Notification Test.", "This is the content of the SMS message.");
		
		//HTTP422 Unprocessable Entity
		assertTrue(response == 422);
	}
	
	@Test
	public void testBadSubject() throws WhispirAPIException {
		whispirAPI.setApikey(TEST_API_KEY);
		whispirAPI.setUsername(TEST_USERNAME);
		whispirAPI.setPassword(TEST_PASSWORD);
		
		int response = whispirAPI.sendMessage("61423556682", "", "This is the content of the SMS message.");
		
		//HTTP422 Unprocessable Entity
		assertTrue(response == 422);
	}
	
	@Test
	public void testBadContent() throws WhispirAPIException {
		whispirAPI.setApikey(TEST_API_KEY);
		whispirAPI.setUsername(TEST_USERNAME);
		whispirAPI.setPassword(TEST_PASSWORD);
		
		int response = whispirAPI.sendMessage("61423556682", "Incident Notification Test.", "");
		
		//HTTP422 Unprocessable Entity
		assertTrue(response == 422);
	}
	
	@Test
	public void testCompanyMessage() throws WhispirAPIException {
		whispirAPI.setApikey(TEST_API_KEY);
		whispirAPI.setUsername(TEST_USERNAME);
		whispirAPI.setPassword(TEST_PASSWORD);
		
		int response = whispirAPI.sendMessage("61423556682", "Incident Notification Test.", "This is the content of the SMS message.");
		
		//HTTP202 Accepted
		assertTrue(response == 202);
	}
	
	@Test
	public void testWorkspaceMessage() throws WhispirAPIException {
		whispirAPI.setApikey(TEST_API_KEY);
		whispirAPI.setUsername(TEST_USERNAME);
		whispirAPI.setPassword(TEST_PASSWORD);
		
		int response = whispirAPI.sendMessage("F3460C2D9E5E2673", "61423556682", "Incident Notification Test.", "This is the content of the SMS message.");
		
		//HTTP202 Accepted
		assertTrue(response == 202);
	}
	
	@Test
	public void testV2Schema() throws WhispirAPIException {
		whispirAPI = new WhispirAPI(TEST_API_KEY, TEST_USERNAME, TEST_PASSWORD, "v2");

		int response = whispirAPI.sendMessage("F3460C2D9E5E2673", "61423556682", "Incident Notification Test.", "This is the content of the SMS message.");
		
		//HTTP415 Unsupported Media Type (it's not implemented yet)
		assertTrue(response == 415);
	}

}

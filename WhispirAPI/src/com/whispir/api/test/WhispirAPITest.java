package com.whispir.api.test;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.whispir.api.WhispirAPI;

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
	public void testBadAPIKey() {
		whispirAPI.setApikey("1234");
		whispirAPI.setUsername(TEST_USERNAME);
		whispirAPI.setPassword(TEST_PASSWORD);
		
		int response = whispirAPI.sendMessage("61423556682", "subject", "content");
		
		assertTrue(response == 403);
	}
	
	@Test
	public void testBadUsername() {		
		whispirAPI.setApikey(TEST_API_KEY);
		whispirAPI.setUsername("blahblahblah");
		whispirAPI.setPassword(TEST_PASSWORD);
		
		int response = whispirAPI.sendMessage("61423556682", "subject", "content");
		
		assertTrue(response == 401);
	}
	
	@Test
	public void testBadPassword() {
		whispirAPI.setApikey(TEST_API_KEY);
		whispirAPI.setUsername(TEST_USERNAME);
		whispirAPI.setPassword("blahblahblah");
		
		int response = whispirAPI.sendMessage("61423556682", "subject", "content");
		
		assertTrue(response == 401);
	}
	
	@Test
	public void testBadRecipient() {
		whispirAPI.setApikey(TEST_API_KEY);
		whispirAPI.setUsername(TEST_USERNAME);
		whispirAPI.setPassword(TEST_PASSWORD);
		
		int response = whispirAPI.sendMessage("1", "Incident Notification Test.", "This is the content of the SMS message.");
		assertTrue(response == 422);
	}
	
	@Test
	public void testBadSubject() {
		whispirAPI.setApikey(TEST_API_KEY);
		whispirAPI.setUsername(TEST_USERNAME);
		whispirAPI.setPassword(TEST_PASSWORD);
		
		int response = whispirAPI.sendMessage("61423556682", "", "This is the content of the SMS message.");
		assertTrue(response == 422);
	}
	
	@Test
	public void testBadContent() {
		whispirAPI.setApikey(TEST_API_KEY);
		whispirAPI.setUsername(TEST_USERNAME);
		whispirAPI.setPassword(TEST_PASSWORD);
		
		int response = whispirAPI.sendMessage("61423556682", "Incident Notification Test.", "");
		assertTrue(response == 422);
	}
	
	@Test
	public void testCompanyMessage() {
		whispirAPI.setApikey(TEST_API_KEY);
		whispirAPI.setUsername(TEST_USERNAME);
		whispirAPI.setPassword(TEST_PASSWORD);
		
		int response = whispirAPI.sendMessage("61423556682", "Incident Notification Test.", "This is the content of the SMS message.");
		assertTrue(response == 202);
	}
	
	@Test
	public void testWorkspaceMessage() {
		whispirAPI.setApikey(TEST_API_KEY);
		whispirAPI.setUsername(TEST_USERNAME);
		whispirAPI.setPassword(TEST_PASSWORD);
		
		int response = whispirAPI.sendMessage("F3460C2D9E5E2673", "61423556682", "Incident Notification Test.", "This is the content of the SMS message.");
		assertTrue(response == 202);
	}

}

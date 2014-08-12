package com.whispir.api.tests;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.whispir.api.WhispirAPI;
import com.whispir.api.exceptions.WhispirAPIException;

public class WhispirAPITest {

	private WhispirAPI whispirAPI;

	// POPULATE THESE OR ALL THE TESTS WILL FAIL!!!

	private static final String TEST_API_KEY = "4fcn8xkeherbdm5y5fpnat8g";
	private static final String TEST_USERNAME = "jordan.walsh";
	private static final String TEST_PASSWORD = "1234";

	// Message content variables for the tests

	private static final String TEST_RECIPIENT = "61423556682";
	private static final String TEST_WORKSPACE_ID = "F3460C2D9E5E2673";
	private static final String TEST_MESSAGE_SUBJECT = "Incident Notification Test.";
	private static final String TEST_MESSAGE_BODY = "This is the content of the SMS message.";
	
	//Debugging
	private static final String DEBUG_HOST = "app19.dev1.whispir.net:8080";

	@Before
	public void setUp() throws Exception {
		if(!"".equals(DEBUG_HOST)) {
			whispirAPI = new WhispirAPI(TEST_API_KEY, TEST_USERNAME, TEST_PASSWORD, "v1", DEBUG_HOST);
		} else {
			whispirAPI = new WhispirAPI(TEST_API_KEY, TEST_USERNAME, TEST_PASSWORD);
		}
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBadAPIKey() throws WhispirAPIException {
		whispirAPI.setApikey("1234");
		whispirAPI.setUsername(TEST_USERNAME);
		whispirAPI.setPassword(TEST_PASSWORD);
		
		int response = whispirAPI.sendMessage(TEST_RECIPIENT, TEST_MESSAGE_SUBJECT, TEST_MESSAGE_BODY);

		
		if(!"".equals(DEBUG_HOST)) {
			assertTrue(response == 202);
		} else {
			// HTTP403 Permission Denied / Forbidden
			assertTrue(response == 403);
		}
		
		
	}

	@Test
	public void testBadUsername() throws WhispirAPIException {
		whispirAPI.setApikey(TEST_API_KEY);
		whispirAPI.setUsername("blahblahblah");
		whispirAPI.setPassword(TEST_PASSWORD);
		
		int response = whispirAPI.sendMessage(TEST_RECIPIENT,
				TEST_MESSAGE_SUBJECT, TEST_MESSAGE_BODY);

		// HTTP401 Unauthorized Access
		assertTrue(response == 401);
	}

	@Test
	public void testBadPassword() throws WhispirAPIException {
		whispirAPI.setApikey(TEST_API_KEY);
		whispirAPI.setUsername(TEST_USERNAME);
		whispirAPI.setPassword("blahblahblah");
		
		int response = whispirAPI.sendMessage(TEST_RECIPIENT,
				TEST_MESSAGE_SUBJECT, TEST_MESSAGE_BODY);

		// HTTP401 Unauthorized Access
		assertTrue(response == 401);
	}

	@Test
	public void testBadRecipient() throws WhispirAPIException {
		whispirAPI.setApikey(TEST_API_KEY);
		whispirAPI.setUsername(TEST_USERNAME);
		whispirAPI.setPassword(TEST_PASSWORD);
		
		int response = whispirAPI.sendMessage("1", TEST_MESSAGE_SUBJECT,
				TEST_MESSAGE_BODY);

		// HTTP422 Unprocessable Entity
		assertTrue(response == 422);
	}

	@Test
	public void testBadSubject() throws WhispirAPIException {
		whispirAPI.setApikey(TEST_API_KEY);
		whispirAPI.setUsername(TEST_USERNAME);
		whispirAPI.setPassword(TEST_PASSWORD);
		
		int response = whispirAPI.sendMessage(TEST_RECIPIENT, "",
				TEST_MESSAGE_BODY);

		// HTTP422 Unprocessable Entity
		assertTrue(response == 422);
	}

	@Test
	public void testBadContent() throws WhispirAPIException {
		whispirAPI.setApikey(TEST_API_KEY);
		whispirAPI.setUsername(TEST_USERNAME);
		whispirAPI.setPassword(TEST_PASSWORD);
		
		int response = whispirAPI.sendMessage(TEST_RECIPIENT,
				TEST_MESSAGE_SUBJECT, "");

		// HTTP422 Unprocessable Entity
		assertTrue(response == 422);
	}

	@Test
	public void testCompanyMessage() throws WhispirAPIException {
		whispirAPI.setApikey(TEST_API_KEY);
		whispirAPI.setUsername(TEST_USERNAME);
		whispirAPI.setPassword(TEST_PASSWORD);

		int response = whispirAPI.sendMessage(TEST_RECIPIENT,
				TEST_MESSAGE_SUBJECT, TEST_MESSAGE_BODY);

		// HTTP202 Accepted
		assertTrue(response == 202);
	}

	@Test
	public void testWorkspaceMessage() throws WhispirAPIException {
		whispirAPI.setApikey(TEST_API_KEY);
		whispirAPI.setUsername(TEST_USERNAME);
		whispirAPI.setPassword(TEST_PASSWORD);
		
		int response = whispirAPI.sendMessage(TEST_WORKSPACE_ID,
				TEST_RECIPIENT, TEST_MESSAGE_SUBJECT, TEST_MESSAGE_BODY);

		// HTTP202 Accepted
		assertTrue(response == 202);
	}

	@Test
	public void testV2Schema() throws WhispirAPIException {
		whispirAPI = new WhispirAPI(TEST_API_KEY, TEST_USERNAME, TEST_PASSWORD, "v2", DEBUG_HOST);
		
		int response = whispirAPI.sendMessage(TEST_WORKSPACE_ID,
				TEST_RECIPIENT, TEST_MESSAGE_SUBJECT, TEST_MESSAGE_BODY);

		// HTTP415 Unsupported Media Type (it's not implemented yet)
		assertTrue(response == 415);
	}

	@Test
	public void testMessageWithOptions() throws WhispirAPIException {		
		Map<String, String> content = new HashMap<String, String>();
		Map<String, String> options = new HashMap<String, String>();

		content.put("body", TEST_MESSAGE_BODY);
		options.put("type", "defaultNoReply");

		int response = whispirAPI.sendMessage(TEST_WORKSPACE_ID,
				TEST_RECIPIENT, TEST_MESSAGE_SUBJECT, content, options);

		// HTTP202 Accepted
		assertTrue(response == 202);
	}

	@Test
	public void testEmailMessage() throws WhispirAPIException {
		Map<String, String> content = new HashMap<String, String>();

		content.put("email", "This is the content of the Email message.");
		content.put("emailType", "text/plain");

		int response = whispirAPI.sendMessage(TEST_WORKSPACE_ID,
				"jwalsh@whispir.com", TEST_MESSAGE_SUBJECT, content);

		// HTTP202 Accepted
		assertTrue(response == 202);
	}

	@Test
	public void testPlainRPMMessage() throws WhispirAPIException {
		Map<String, String> content = new HashMap<String, String>();

		content.put("body",
				"This is the content of the SMS message. @@web_link@@");
		content.put("web", "This is the content of the Web message.");
		content.put("webType", "text/plain");

		int response = whispirAPI.sendMessage(TEST_WORKSPACE_ID,
				TEST_RECIPIENT, TEST_MESSAGE_SUBJECT, content);

		// HTTP202 Accepted
		assertTrue(response == 202);
	}

	@Test
	public void testRichRPMMessage() throws WhispirAPIException {		
		Map<String, String> content = new HashMap<String, String>();

		content.put("body",
				"This is the content of the SMS message. @@web_link@@");
		content.put("web", "<b>This is the content of the Web message.</b>");
		content.put("webType", "text/html");

		int response = whispirAPI.sendMessage(TEST_WORKSPACE_ID,
				TEST_RECIPIENT, TEST_MESSAGE_SUBJECT, content);

		// HTTP202 Accepted
		assertTrue(response == 202);
	}

	@Test
	public void testVoiceCall() throws WhispirAPIException {
		Map<String, String> content = new HashMap<String, String>();

		content.put("voiceIntro", "Welcome");
		content.put("voice", "This is the content of the voice call");

		int response = whispirAPI.sendMessage(TEST_WORKSPACE_ID,
				TEST_RECIPIENT, TEST_MESSAGE_SUBJECT, content);

		// HTTP202 Accepted
		assertTrue(response == 202);
	}

}

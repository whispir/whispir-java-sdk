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
	
	protected WhispirAPI whispirAPI;

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
			whispirAPI = new WhispirAPI(TEST_API_KEY, TEST_USERNAME,
					TEST_PASSWORD, DEBUG_HOST);
		} else {
			whispirAPI = new WhispirAPI(TEST_API_KEY, TEST_USERNAME,
					TEST_PASSWORD);
		}
		
		if (!"".equals(PROXY_HOST)) {
			whispirAPI.setProxy(PROXY_HOST, PROXY_PORT, PROXY_HTTPS_ENABLED);
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
		
		if(!"".equals(TEST_WORKSPACE_ID)) {
			whispirAPI.setApikey(TEST_API_KEY);
			whispirAPI.setUsername(TEST_USERNAME);
			whispirAPI.setPassword(TEST_PASSWORD);
			
			int response = whispirAPI.sendMessage(TEST_WORKSPACE_ID,
					TEST_RECIPIENT, TEST_MESSAGE_SUBJECT, TEST_MESSAGE_BODY);

			// HTTP202 Accepted
			assertTrue(response == 202);
		} else{
			assertTrue(true); // NO WORKSPACE PROVIDED SO JUST PASS THE TEST
		}
		
		
	}

	@Test
	public void testMessageWithOptions() throws WhispirAPIException {		
		Map<String, String> content = new HashMap<String, String>();
		Map<String, String> options = new HashMap<String, String>();

		content.put("body", TEST_MESSAGE_BODY);
		options.put("type", "defaultNoReply");
		options.put("pushNotifications", "enabled");
		options.put("pushEscalationMins", "3");
		
		int response = whispirAPI.sendMessage("",
				TEST_RECIPIENT, TEST_MESSAGE_SUBJECT, content, options);

		// HTTP202 Accepted
		assertTrue(response == 202);
	}
	
	@Test
	public void testMessageWithFormatting() throws WhispirAPIException {		
		Map<String, String> content = new HashMap<String, String>();

		content.put("body", "This message has\nsome new line\ncharacters");
		
		int response = whispirAPI.sendMessage("",
				TEST_RECIPIENT, TEST_MESSAGE_SUBJECT, content);

		// HTTP202 Accepted
		assertTrue(response == 202);
	}

	@Test
	public void testEmailMessage() throws WhispirAPIException {
		Map<String, String> content = new HashMap<String, String>();

		content.put("email", "This is the content of the Email message.");
		content.put("emailType", "text/plain");

		int response = whispirAPI.sendMessage("",
				TEST_RECIPIENT, TEST_MESSAGE_SUBJECT, content);

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

		int response = whispirAPI.sendMessage("",
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

		int response = whispirAPI.sendMessage("",
				TEST_RECIPIENT, TEST_MESSAGE_SUBJECT, content);

		// HTTP202 Accepted
		assertTrue(response == 202);
	}

	@Test
	public void testVoiceCall() throws WhispirAPIException {
		Map<String, String> content = new HashMap<String, String>();

		content.put("voiceIntro", "Welcome");
		content.put("voice", "This is the content of the voice call");

		int response = whispirAPI.sendMessage("",
				TEST_RECIPIENT, TEST_MESSAGE_SUBJECT, content);

		// HTTP202 Accepted
		assertTrue(response == 202);
	}
}

package com.whispir.sdk.impl.tests;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.whispir.sdk.WhispirResponse;
import com.whispir.sdk.exceptions.WhispirSDKException;
import com.whispir.sdk.tests.WhispirSDKTest;

public class MessageHelperImplTest extends WhispirSDKTest {

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
	public void testBadRecipient() throws WhispirSDKException {
		WhispirResponse response = whispirSDK.sendMessage("1", TEST_MESSAGE_SUBJECT,
				TEST_MESSAGE_BODY);

		// HTTP422 Unprocessable Entity
		assertTrue(response.getStatusCode() == 422);
	}

	@Test
	public void testBadSubject() throws WhispirSDKException {
		WhispirResponse response = whispirSDK.sendMessage(TEST_RECIPIENT, "",
				TEST_MESSAGE_BODY);

		// HTTP422 Unprocessable Entity
		assertTrue(response.getStatusCode() == 422);
	}

	@Test
	public void testBadContent() throws WhispirSDKException {
		WhispirResponse response = whispirSDK.sendMessage(TEST_RECIPIENT,
				TEST_MESSAGE_SUBJECT, "");

		// HTTP422 Unprocessable Entity
		assertTrue(response.getStatusCode() == 422);
	}

	@Test
	public void testCompanyMessage() throws WhispirSDKException {
		WhispirResponse response = whispirSDK.sendMessage(TEST_RECIPIENT,
				TEST_MESSAGE_SUBJECT, TEST_MESSAGE_BODY);

		// HTTP202 Accepted
		assertTrue(response.getStatusCode() == 202);
	}

	@Test
	public void testWorkspaceMessage() throws WhispirSDKException {
		
		if(!"".equals(TEST_WORKSPACE_ID)) {
			WhispirResponse response = whispirSDK.sendMessage(TEST_WORKSPACE_ID,
					TEST_RECIPIENT, TEST_MESSAGE_SUBJECT, TEST_MESSAGE_BODY);

			// HTTP202 Accepted
			assertTrue(response.getStatusCode() == 202);
		} else{
			assertTrue(true); // NO WORKSPACE PROVIDED SO JUST PASS THE TEST
		}
	}

	@Test
	public void testMessageWithOptions() throws WhispirSDKException {		
		Map<String, String> content = new HashMap<String, String>();
		Map<String, String> options = new HashMap<String, String>();

		content.put("body", TEST_MESSAGE_BODY);
		options.put("type", "defaultNoReply");
		options.put("pushNotifications", "enabled");
		options.put("pushEscalationMins", "3");
		
		WhispirResponse response = whispirSDK.sendMessage("",
				TEST_RECIPIENT, TEST_MESSAGE_SUBJECT, content, options);

		// HTTP202 Accepted
		assertTrue(response.getStatusCode() == 202);
	}
	
	@Test
	public void testMessageWithFormatting() throws WhispirSDKException {		
		Map<String, String> content = new HashMap<String, String>();

		content.put("body", "This message has\nsome new line\ncharacters");
		
		WhispirResponse response = whispirSDK.sendMessage("",
				TEST_RECIPIENT, TEST_MESSAGE_SUBJECT, content);

		// HTTP202 Accepted
		assertTrue(response.getStatusCode() == 202);
	}

	@Test
	public void testEmailMessage() throws WhispirSDKException {
		Map<String, String> content = new HashMap<String, String>();

		content.put("email", "This is the content of the Email message.");
		content.put("emailType", "text/plain");

		WhispirResponse response = whispirSDK.sendMessage("",
				TEST_RECIPIENT, TEST_MESSAGE_SUBJECT, content);

		// HTTP202 Accepted
		assertTrue(response.getStatusCode() == 202);
	}

	@Test
	public void testPlainRPMMessage() throws WhispirSDKException {
		Map<String, String> content = new HashMap<String, String>();

		content.put("body",
				"This is the content of the SMS message. @@web_link@@");
		content.put("web", "This is the content of the Web message.");
		content.put("webType", "text/plain");

		WhispirResponse response = whispirSDK.sendMessage("",
				TEST_RECIPIENT, TEST_MESSAGE_SUBJECT, content);

		// HTTP202 Accepted
		assertTrue(response.getStatusCode() == 202);
	}

	@Test
	public void testRichRPMMessage() throws WhispirSDKException {		
		Map<String, String> content = new HashMap<String, String>();

		content.put("body",
				"This is the content of the SMS message. @@web_link@@");
		content.put("web", "<b>This is the content of the Web message.</b>");
		content.put("webType", "text/html");

		WhispirResponse response = whispirSDK.sendMessage("",
				TEST_RECIPIENT, TEST_MESSAGE_SUBJECT, content);

		// HTTP202 Accepted
		assertTrue(response.getStatusCode() == 202);
	}

	@Test
	public void testVoiceCall() throws WhispirSDKException {
		Map<String, String> content = new HashMap<String, String>();

		content.put("voiceIntro", "Welcome");
		content.put("voice", "This is the content of the voice call");

		WhispirResponse response = whispirSDK.sendMessage("",
				TEST_RECIPIENT, TEST_MESSAGE_SUBJECT, content);

		// HTTP202 Accepted
		assertTrue(response.getStatusCode() == 202);
	}

	@Test
	public void testGetMessages() throws WhispirSDKException {
		WhispirResponse response = whispirSDK.getMessages();

		//System.out.println(response.getResponse());
		
		// HTTP200 Accepted
		assertTrue(response.getStatusCode() == 200);
	}
	
	@Test
	public void testGetSingleMessage() throws WhispirSDKException {
		
		WhispirResponse messages = whispirSDK.getMessages();
		WhispirResponse message = null;
		
		Iterator<String> keys = messages.getResponse().keySet().iterator();
		
		while(keys.hasNext()) {
			String id = messages.getResponse().get(keys.next());
			
			if(!"".equals(id)) {
				message = whispirSDK.getMessage(id);
			}
		}
		
		assertTrue(message != null);
		
		// HTTP200 Accepted
		assertTrue(message.getStatusCode() == 200);
	}
	
}

package com.whispir.sdk.tests;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.whispir.sdk.WhispirEndPoints;
import com.whispir.sdk.WhispirResponse;
import com.whispir.sdk.WhispirSDK;
import com.whispir.sdk.exceptions.WhispirSDKException;

public class WhispirSDKTest {

  protected WhispirSDK whispirSDK;

  // POPULATE THESE OR ALL THE TESTS WILL FAIL!!!

  protected static final String TEST_API_KEY = "";
  protected static final String TEST_USERNAME = "";
  protected static final String TEST_PASSWORD = "";
  protected static final WhispirEndPoints APIENDPOINT = null;
  
  // Message content variables for the tests
  protected static final String TEST_RECIPIENT = "";
  protected static final String TEST_WORKSPACE_ID = "";
  protected static final String TEST_WORKSPACE_NAME = "";
  protected static final String TEST_MESSAGE_SUBJECT = "";
  protected static final String TEST_MESSAGE_BODY = "";

  // Debugging
  protected static final String DEBUG_HOST = "";

  // Proxy Debugging (I installed Squid on my Mac from here http://squidman.net/squidman/index.html)
  protected static final String PROXY_HOST = "";
  protected static final int PROXY_PORT = 0;
  protected static final boolean PROXY_HTTPS_ENABLED = false;
  protected static final String PROXY_USERNAME = "";
  protected static final String PROXY_PASSWORD = "";
	
  //Squid Proxy Error Code is 407 (Proxy Authentication Required)
  protected static final int PROXY_ERROR_CODE = 407;

  @Before
  public void setUp() throws Exception {
    if (!"".equals(DEBUG_HOST)) {
      this.whispirSDK = new WhispirSDK(TEST_API_KEY, TEST_USERNAME,
          TEST_PASSWORD, DEBUG_HOST);
    } else {
      this.whispirSDK = new WhispirSDK(TEST_API_KEY, TEST_USERNAME,
          TEST_PASSWORD, APIENDPOINT);
    }

    if (!"".equals(PROXY_HOST)) {

      if(!"".equals(PROXY_USERNAME) && !"".equals(PROXY_PASSWORD)) {
        this.whispirSDK.setProxy(PROXY_HOST, PROXY_PORT, PROXY_HTTPS_ENABLED, PROXY_USERNAME, PROXY_PASSWORD);
      } else {
        this.whispirSDK.setProxy(PROXY_HOST, PROXY_PORT, PROXY_HTTPS_ENABLED);
      }
    }
  }

  @After
  public void tearDown() throws Exception {
    this.whispirSDK = null;
  }

  @Test
  public void testBadProxyUsername() throws WhispirSDKException {

    if(!"".equals(PROXY_HOST)) {
      this.whispirSDK.setProxy(PROXY_HOST, PROXY_PORT, PROXY_HTTPS_ENABLED, "bad", PROXY_PASSWORD);

      this.whispirSDK.setApikey(TEST_API_KEY);
      this.whispirSDK.setUsername(TEST_USERNAME);
      this.whispirSDK.setPassword(TEST_PASSWORD);

      WhispirResponse response = this.whispirSDK.getWorkspaces();

      assertTrue(response.getStatusCode() == PROXY_ERROR_CODE);
    } else {
      assertTrue(true);
    }
  }

  @Test
  public void testBadProxyPassword() throws WhispirSDKException {

    if(!"".equals(PROXY_HOST)) {
      this.whispirSDK.setProxy(PROXY_HOST, PROXY_PORT, PROXY_HTTPS_ENABLED, PROXY_USERNAME, "bad");

      this.whispirSDK.setApikey(TEST_API_KEY);
      this.whispirSDK.setUsername(TEST_USERNAME);
      this.whispirSDK.setPassword(TEST_PASSWORD);

      WhispirResponse response = this.whispirSDK.getWorkspaces();


      assertTrue(response.getStatusCode() == PROXY_ERROR_CODE);
    }

    assertTrue(true);
  }

  @Test
  public void testBadAPIKey() throws WhispirSDKException {
    this.whispirSDK.setApikey("1234");
    this.whispirSDK.setUsername(TEST_USERNAME);
    this.whispirSDK.setPassword(TEST_PASSWORD);

    WhispirResponse response = this.whispirSDK.sendMessage(TEST_RECIPIENT, TEST_MESSAGE_SUBJECT, TEST_MESSAGE_BODY);

    if(!"".equals(DEBUG_HOST)) {
      assertTrue(response.getStatusCode() == 202);
    } else {
      // HTTP403 Permission Denied / Forbidden
      assertTrue(response.getStatusCode() == 403);
    }
  }

  @Test
  public void testBadUsername() throws WhispirSDKException {
    this.whispirSDK.setApikey(TEST_API_KEY);
    this.whispirSDK.setUsername("blahblahblah");
    this.whispirSDK.setPassword(TEST_PASSWORD);

    WhispirResponse response = this.whispirSDK.sendMessage(TEST_RECIPIENT,
        TEST_MESSAGE_SUBJECT, TEST_MESSAGE_BODY);

    // HTTP401 Unauthorized Access
    assertTrue(response.getStatusCode() == 401);
  }

  @Test
  public void testBadPassword() throws WhispirSDKException {
    this.whispirSDK.setApikey(TEST_API_KEY);
    this.whispirSDK.setUsername(TEST_USERNAME);
    this.whispirSDK.setPassword("blahblahblah");

    WhispirResponse response = this.whispirSDK.sendMessage(TEST_RECIPIENT,
        TEST_MESSAGE_SUBJECT, TEST_MESSAGE_BODY);

    // HTTP401 Unauthorized Access
    assertTrue(response.getStatusCode() == 401);
  }
  
  @Test
  public void testDebugHost() throws WhispirSDKException {
    this.whispirSDK.setApikey(TEST_API_KEY);
    this.whispirSDK.setUsername(TEST_USERNAME);
    this.whispirSDK.setPassword(TEST_PASSWORD);
    this.whispirSDK.setDebugHost("sandbox.whispir.com/api");

    // HTTP401 Unauthorized Access
    assertTrue(this.whispirSDK.getHost().equals("sandbox.whispir.com"));
  }
}


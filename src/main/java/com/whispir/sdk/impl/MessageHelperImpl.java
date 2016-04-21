package com.whispir.sdk.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.whispir.sdk.WhispirResponse;
import com.whispir.sdk.WhispirSDK;
import com.whispir.sdk.WhispirSDKConstants;
import com.whispir.sdk.exceptions.WhispirSDKException;
import com.whispir.sdk.interfaces.MessageHelper;

public class MessageHelperImpl extends BaseHelperImpl implements MessageHelper {

  public MessageHelperImpl(WhispirSDK sdk) {
    super(sdk);
  }

  /**
   * <p>
   * Allows a user to send a message in the default My Company workspace.
   * </p>
   * <p>
   * For more complex content, the user should use the Map content overloaded
   * function.
   * </p>
   *
   * @param recipient
   *            - the mobile number or email address of the recipient of the
   *            message
   * @param subject
   *            - the textual subject of the message
   * @param content
   *            - the textual content of the Push/SMS message.
   * @return response - the HTTP response code of the performed action.
   */
  public WhispirResponse sendMessage(String recipient, String subject, String content)
      throws WhispirSDKException {
    return sendMessage("", recipient, subject, content);
  }

  /**
   * <p>
   * Allows a user to send a message in the specified Workspace ID.
   * </p>
   * <p>
   * For more complex content, the user should use the Map content overloaded
   * function.
   * </p>
   *
   * @param recipient
   *            - the mobile number or email address of the recipient of the
   *            message
   * @param subject
   *            - the textual subject of the message
   * @param content
   *            - the textual content of the Push/SMS message.
   * @return response - the HTTP response code of the performed action.
   */
  public WhispirResponse sendMessage(String workspaceId, String recipient,
      String subject, String content) throws WhispirSDKException {
    Map<String, String> smsContent = new HashMap<String, String>();
    smsContent.put("body", content);
    return sendMessage(workspaceId, recipient, subject, smsContent);
  }

  /**
   * <p>
   * Allows a user to send a message in any workspace, with any combination of
   * content within the content map.
   * </p>
   * <p>
   * The content Map is expected to provide the following information.
   * </p>
   * <p>
   * For SMS/Push
   * </p>
   * <p>
   * - body - The content for the Push/SMS message.
   * </p>
   *
   * @param recipient
   *            - the mobile number or email address of the recipient of the
   *            message
   * @param subject
   *            - the textual subject of the message
   * @param content
   *            - the Map of content for the Whispir Message
   * @return response - the HTTP response code of the performed action.
   */
  public WhispirResponse sendMessage(String workspaceId, String recipient,
      String subject, Map<String, String> content)
      throws WhispirSDKException {
    Map<String, String> options = new HashMap<String, String>();
    return sendMessage(workspaceId, recipient, subject, content, options);
  }

  /**
   * Allows a user to send a message in any workspace, with any combination of
   * content within the content map
   * <p>
   * The content Map is expected to provide the following information:
   * </p>
   * <p>
   * For SMS/Push
   * </p>
   * <p>
   * - body - The content of the SMS/Push Message
   * </p>
   * <p>
   * </p>
   * <p>
   * The options Map is expected to provide the following information:
   * </p>
   * <p>
   * - type - defaultNoReply (specifies if the message cannot be replied to)
   * </p>
   * <p>
   * - pushEscalation - true/false (string)
   * </p>
   * <p>
   * - escalationMins - 3,4,5,10 (string)
   * </p>
   * <p>
   * </p>
   *
   * @param recipient
   *            - the mobile number or email address of the recipient of the
   *            message
   * @param subject
   *            - the textual subject of the message
   * @param content
   *            - the Map of content for the Whispir Message
   * @param options
   *            - the Map of options for the Whispir Message
   * @return response - the HTTP response code of the performed action.
   */
  public WhispirResponse sendMessage(String workspaceId, String recipient,
      String subject, Map<String, String> content,
      Map<String, String> options) throws WhispirSDKException {
    WhispirResponse response = new WhispirResponse();

    if (recipient == null || recipient.length() < 8) {
      // error with the recipient information, returning HTTP 422.
      response.setStatusCode(422);
      return response;
    }

    try {
      JSONObject request = new JSONObject();

      request.put("to", recipient);
      request.put("subject", subject);

      // Check for the body in the map
      if (content.containsKey("body") && !"".equals(content.get("body"))) {
        request.put("body", content.get("body"));
      }

      // Check for the email in the map
      if (content.containsKey("email")
          && !"".equals(content.get("email"))) {
        JSONObject email = new JSONObject();
        email.put("body", content.get("email"));

        if (content.containsKey("emailType")
            && !"".equals(content.get("emailType"))) {
          email.put("type", content.get("emailType"));
        }

        request.put("email", email);
      }

      // Check for the voice content in the map
      if (content.containsKey("voice")
          && !"".equals(content.get("voice"))) {
        JSONObject voice = new JSONObject();
        voice.put("body", content.get("voice"));
        voice.put("type",
            "ConfCall:,ConfAccountNo:,ConfPinNo:,ConfModPinNo:,Pin:");

        if (content.containsKey("voiceIntro")
            && !"".equals(content.get("voiceIntro"))) {
          voice.put("header", content.get("voiceIntro"));
        }

        request.put("voice", voice);
      }

      // Check for the web content in the map
      if (content.containsKey("web") && !"".equals(content.get("web"))) {
        JSONObject web = new JSONObject();
        web.put("body", content.get("web"));

        if (content.containsKey("webType")
            && !"".equals(content.get("webType"))) {
          web.put("type", content.get("webType"));
        }

        request.put("web", web);
      }

      // Check for the noreply options in the map
      if (options.containsKey("type")) {
        request.put("type", options.get("type"));
      }

      // Check for the push to SMS escalation options in the map
      if (options.containsKey("pushNotifications")
          && "enabled".equalsIgnoreCase(options
              .get("pushNotifications"))) {

        JSONObject pushOptions = new JSONObject();
        pushOptions.put("notifications", "enabled");

        if (options.containsKey("pushEscalationMins")) {
          pushOptions.put("escalationMins",
              options.get("pushEscalationMins"));
        }

        JSONObject features = new JSONObject();
        features.put("pushOptions", pushOptions);

        request.put("features", features);
      }

      // System.out.println("Request: " + request.toString());

      // Execute the request
      return sdk.post(WhispirSDKConstants.MESSAGES_RESOURCE, workspaceId, request.toString());

    } catch (JSONException e) {
      throw new WhispirSDKException(
          "Error occurred parsing the object with the content provided."
              + e.getMessage());
    }
  }

  /**
   * <p>
   * Allows a user to retrieve a list of messages.
   * </p>
   *
   * @return response - the WhispirResponse object of the performed action.
   */
  public WhispirResponse getMessages() throws WhispirSDKException {
    WhispirResponse response = new WhispirResponse();

    response = sdk.get(WhispirSDKConstants.MESSAGES_RESOURCE, "");

    Map<String, String> map = new TreeMap<String, String>();

    try {
      JSONObject obj = new JSONObject(response.getRawResponse());

      JSONArray messages = obj.getJSONArray("messages");
      int messagesLength = messages.length();

      for (int i = 0; i < messagesLength; i++) {
        String subject = (String) messages.getJSONObject(i).get(
            "subject");
        String fullUrl = (String) messages.getJSONObject(i)
            .getJSONArray("link").getJSONObject(0).get("uri");

        String id = fullUrl.substring(fullUrl.lastIndexOf("/") + 1,
            fullUrl.lastIndexOf("?"));

        System.out.println(subject + " " + id);

        map.put(subject, id);
      }

    } catch (JSONException e) {
      throw new WhispirSDKException(e.getMessage());
    }

    response.setResponse(map);

    return response;
  }

  /**
   * <p>
   * Allows a user to retrieve a single message from the API.
   * </p>
   * @param messageId - the ID of the message to retrieve
   * @return response - the WhispirResponse object of the performed action.
   */
  public WhispirResponse getMessage(String messageId) throws WhispirSDKException {
    WhispirResponse response = new WhispirResponse();

    response = sdk.get(WhispirSDKConstants.MESSAGES_RESOURCE, "", messageId);

    Map<String, String> map = new TreeMap<String, String>();

    response.setResponse(map);

    return response;
  }
}

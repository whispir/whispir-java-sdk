package com.whispir.sdk.examples;

import java.util.HashMap;
import java.util.Map;

import com.whispir.sdk.WhispirResponse;
import com.whispir.sdk.WhispirSDK;
import com.whispir.sdk.exceptions.WhispirSDKException;

public class Launcher {
	
	private static WhispirSDK sdk;

	/**
	 * 
	 * @param args
	 *            [0] String - The recipient contact information
	 * @param args
	 *            [1] String - The Subject of the message, or first line of the
	 *            SMS
	 * @param args
	 *            [2] String - The Content of the message, or remainder of the
	 *            SMS
	 *            
	 * @param args
	 *            [3] String - The workspace id to be used to send the message
	 */
	public static void main(String[] args) {
		
		if (args.length == 0) {
			System.out.println("Whispir SDK Test Bed v1.0");
			System.out.println("");
			System.out.println("Nine arguments are required to run this test bed.");
			System.out.println("1 - debug mode (Whispir internal only): true/false");
			System.out.println("2 - debug server url (enter a - if none)");
			System.out.println("3 - API key");
			System.out.println("4 - Username");
			System.out.println("5 - password");
			System.out.println("6 - 1=Simple SMS/Push in My Company, 2=Simple SMS/Push in a Workspace, 3=RPM in a Workspace, 4=RPM with Push Escalation");
			System.out.println("7 - Workspace ID (enter a - if none)");
			System.out.println("8 - Recipient Details");
			System.out.println("9 - Subject");
			System.out.println("10 - Content");
			System.out.println("11 - Use Proxy (true/false)");
		} else {
			
			try {
				final String debug = args[0];
				final String server = args[1];
				final String apikey = args[2];								
				final String username = args[3];				
				final String password = args[4];
				final int test = new Integer(args[5]);
				final String workspaceId = args[6];
				final String recipient = args[7];
				final String subject = args[8];
				final String messageContent = args[9];
				final String proxyEnabled = args[10];
				
				if("true".equals(debug)) {
					sdk = new WhispirSDK("abc", username, password, server);
				} else {
					sdk = new WhispirSDK(apikey, username, password, "");
				}
				
				if("true".equals(proxyEnabled)) {
					sdk.setProxy("localhost", 9080, false);
				}
				
				WhispirResponse response = new WhispirResponse();
				
				Map<String,String> content = new HashMap<String,String>();
				Map<String,String> options = new HashMap<String,String>();
				
				content.put("body", messageContent);
				//options.put("pushNotifications", "enabled");
				
				switch (test) {
				case 1:
					response = sdk.sendMessage("", recipient, subject, content, options);
					break;
				case 2:
					response = sdk.sendMessage(workspaceId, recipient, subject, content, options);
					break;
				case 3:
					content.put("web", "<b>This is the content of the Web message.</b>");
					content.put("webType", "text/html");
					response = sdk.sendMessage(workspaceId, recipient, subject, content, options);
					break;
				case 4:
					options.put("pushEscalationMins", "3");
					response = sdk.sendMessage(workspaceId, recipient, subject, content, options);
					break;
				default:
					System.out.println("No valid Test Option Provided.");
					break;
				}
				
				System.out.println("Message Result: " + response);
				
			} catch (WhispirSDKException e) {
				e.printStackTrace();
			}
		}
	}
}

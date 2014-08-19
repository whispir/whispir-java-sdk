package com.whispir.api;

import java.util.HashMap;
import java.util.Map;

import com.whispir.api.exceptions.WhispirAPIException;

public class APILauncher {
	
	private static WhispirAPI api;

	/**
	 * 
	 * @param args
	 *            [0] String - The recipient contact information e.g. 0423556682
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
			System.out.println("Whispir API Test Bed v1.0");
			System.out.println("This will prompt you to enter details for a message then send it.");
			System.out.println("");
			System.out.println("Nine arguments are required to run this test bed.");
			System.out.println("1 - debug mode: true/false");
			System.out.println("2 - server: app19.dev1.whispir.net:8080 (enter a - if none)");
			System.out.println("3 - API key e.g. 4fcn8xkeherbdm5y5fpnat8g");
			System.out.println("4 - Username e.g. john.smith");
			System.out.println("5 - password e.g. myPassword");
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
					api = new WhispirAPI("abc", username, password, server);
				} else {
					api = new WhispirAPI(apikey, username, password, "");
				}
				
				if("true".equals(proxyEnabled)) {
					api.setProxy("localhost", 9080, false);
				}
				
				int response = 0;
				
				Map<String,String> content = new HashMap<String,String>();
				Map<String,String> options = new HashMap<String,String>();
				
				content.put("body", messageContent);
				options.put("pushNotifications", "enabled");
				
				switch (test) {
				case 1:
					response = api.sendMessage("", recipient, subject, content, options);
					break;
				case 2:
					response = api.sendMessage(workspaceId, recipient, subject, content, options);
					break;
				case 3:
					content.put("web", "<b>This is the content of the Web message.</b>");
					content.put("webType", "text/html");
					response = api.sendMessage(workspaceId, recipient, subject, content, options);
					break;
				case 4:
					options.put("pushEscalationMins", "3");
					response = api.sendMessage(workspaceId, recipient, subject, content, options);
					break;
				default:
					System.out.println("No valid Test Option Provided.");
					break;
				}
				
				System.out.println("Message Result: " + response);
				
			} catch (WhispirAPIException e) {
				e.printStackTrace();
			}
		}
	}
}

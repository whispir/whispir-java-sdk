package com.whispir.api;

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
		
		if (args.length < 3) {
			System.out.println("Four String arguments are required.");
			System.out.println("1 - Phone Number. e.g. 0402859178");
			System.out.println("2 - Subject. e.g. Hello World");
			System.out.println("3 - Content. e.g. SMS is fun");
			System.out.println("4 - Workspace ID. e.g. F3460C2D9E5E2673");
		} else {
			
			try {
				api = new WhispirAPI("4fcn8xkeherbdm5y5fpnat8g", "jordan.walsh", "12345678");
				
				final String recipient = args[0];
				final String subject = args[1];
				final String content = args[2];
				String workspace = "";
				
				if(args.length > 3) {
					 workspace = args[3];
				}
				
				int response = api.sendMessage(workspace, recipient, subject, content);

				System.out.println("Response: " + response);
				
			} catch (WhispirAPIException e) {
				e.printStackTrace();
			}
		}
	}
}

package com.whispir.api;

public class APILauncher {

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
	 */
	public static void main(String[] args) {
		
		WhispirAPI whispirAPI = new WhispirAPI("4fcn8xkeherbdm5y5fpnat8g", "jordan.walsh", "12345678");

		if (args.length < 3) {
			System.out.println("Four String arguments are required.");
			System.out.println("1 - Phone Number. e.g. 0402859178");
			System.out.println("2 - Subject. e.g. Hello World");
			System.out.println("3 - Content. e.g. SMS is fun");
			System.out.println("4 - Workspace ID. e.g. F3460C2D9E5E2673");
		} else {
			final String recipient = args[0];
			final String subject = args[1];
			final String content = args[2];
			final String workspace = args[3];

			int response = whispirAPI.sendMessage(workspace, recipient, subject, content);

			System.out.println("Response: " + response);
		}

	}
	
	
}

package com.whispir.api;

import java.util.Scanner;

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
			System.out.println("Whispir API Test Bed v1.0");
			System.out.println("This will prompt you to enter details for a message then send it.");
			System.out.println("");
			System.out.println("Three arguments are required to run this test bed.");
			System.out.println("1 - API KEY e.g. abcdyecbbuc838c893b0");
			System.out.println("2 - Username e.g. john.smith");
			System.out.println("3 - password e.g. myPassword");
		} else {
			
			Scanner input = new Scanner(System.in);
			
			try {
				final String apikey = args[0];
				final String username = args[1];
				final String password = args[2];
				
				api = new WhispirAPI(apikey, username, password);
				
				System.out.println("Please enter the phone number to send the SMS to:");
				String recipient = input.nextLine();
				
				System.out.println("Please enter the Subject line of your message:");
				String subject = input.nextLine();
				
				System.out.println("Please enter the remainder of your message:");
				String content = input.nextLine();
				
				int response = api.sendMessage(recipient, subject, content);

				System.out.println("Message Result: " + response);
				
			} catch (WhispirAPIException e) {
				e.printStackTrace();
			}
		}
	}
}

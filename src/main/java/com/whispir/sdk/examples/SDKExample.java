package com.whispir.sdk.examples;

import com.whispir.sdk.WhispirSDK;
import com.whispir.sdk.exceptions.WhispirSDKException;

public class SDKExample {
	
	public static final String API_KEY = "...";
	public static final String USERNAME = "...";
	public static final String PASSWORD = "...";

	public static void main(String[] args) {
		try {
			
			//INIT API object
			WhispirSDK sdk = new WhispirSDK(API_KEY, USERNAME, PASSWORD);
			
			//Send the message
			int status = sdk.sendMessage("61400000000", "This is the subject of my SMS", "This is the content of my SMS");
			
			//Status should be 202 Accepted
			System.out.println("Status: " + status);
			
		} catch (WhispirSDKException e) {
			e.printStackTrace();
		}
	}
}

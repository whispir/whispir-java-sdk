package com.whispir.api.exceptions;

public class WhispirAPIException extends Exception {

	/**
	 * serialVersionUID 
	 */
	private static final long serialVersionUID = 590158798211928997L;
    
    public WhispirAPIException(String message){
        super(message);
    }
}

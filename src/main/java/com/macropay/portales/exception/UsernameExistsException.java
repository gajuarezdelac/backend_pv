package com.macropay.portales.exception;

public class UsernameExistsException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public UsernameExistsException(String msg) {
		super(msg);
	}
}

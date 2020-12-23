package com.lupcode.JSON.exceptions;

/** General JSON exception, parent class for every specific JSON exception
 * @author LupCode.com (Luca Vogels)
 * @since 2020-12-23
 */
public class JsonException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private static final String DEFAULT_MESSAGE = "An unknown JSON error occurred";

	
	public JsonException(){
		super(DEFAULT_MESSAGE);
	}
	
	public JsonException(String message){
		super(message);
	}
	
	public JsonException(Throwable throwable){
		super(DEFAULT_MESSAGE, throwable);
	}
	
	public JsonException(String message, Throwable throwable){
		super(message, throwable);
	}
}

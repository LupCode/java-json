package com.lupcode.JSON.exceptions;

import com.lupcode.JSON.utils.LineColumnTracker;

/** General JSON exception, parent class for every specific JSON exception
 * @author LupCode.com (Luca Vogels)
 * @since 2020-12-23
 */
public class JsonParseUnfinishedException extends JsonParseException {

	private static final long serialVersionUID = 1L;
	
	private static final String DEFAULT_MESSAGE = "Json data ends unexpectedly";

	private char[] expected = null;
	private LineColumnTracker lct = null;
	
	public JsonParseUnfinishedException(){
		super(DEFAULT_MESSAGE);
	}
	
	public JsonParseUnfinishedException(String message){
		super(message);
	}
	
	public JsonParseUnfinishedException(Throwable throwable){
		super(DEFAULT_MESSAGE, throwable);
	}
	
	public JsonParseUnfinishedException(String message, Throwable throwable){
		super(message, throwable);
	}
	
	public JsonParseUnfinishedException(LineColumnTracker lct){
		super(DEFAULT_MESSAGE+(lct!=null ? " at "+lct.toString() : ""));
		this.lct = lct;
	}
	
	public JsonParseUnfinishedException(char expected, LineColumnTracker lct){
		super(DEFAULT_MESSAGE+", expected '"+expected+"' at "+(lct!=null?lct.toString():"unknown"));
		this.expected = new char[] { expected };
		this.lct = lct;
	}
	
	public JsonParseUnfinishedException(char[] expected, LineColumnTracker lct){
		super(DEFAULT_MESSAGE+", expected "+charsToStr(expected)+" at "+(lct!=null?lct.toString():"unknown"));
		this.expected = expected;
		this.lct = lct;
	}
	
	/**
	 * Returns an array containing the expected characters (can be null or empty)
	 * @return Expected characters
	 */
	public char[] getExpectedChars() {
		return expected;
	}
	
	/**
	 * Returns a copy of the {@link LineColumnTracker} that holds information 
	 * about the line and column where the error has occurred
	 * @return Line and column
	 */
	public LineColumnTracker getLineColumn() {
		return lct!=null ? lct.clone() : null;
	}
}

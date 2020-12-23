package com.lupcode.JSON.exceptions;

import com.lupcode.JSON.utils.LineColumnTracker;

/** Exception especially if an error occurs while parsing a JSON string
 * @author LupCode.com (Luca Vogels)
 * @since 2020-12-23
 */
public class JsonParseException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private static final String DEFAULT_MESSAGE = "Could not parse JSON string";

	private char[] expected = null;
	private String actual = null;
	private LineColumnTracker lct = null;
	
	public JsonParseException(){
		super(DEFAULT_MESSAGE);
	}
	
	public JsonParseException(String message){
		super(message);
	}
	
	public JsonParseException(Throwable throwable){
		super(DEFAULT_MESSAGE, throwable);
	}
	
	public JsonParseException(String message, Throwable throwable){
		super(message, throwable);
	}
	
	public JsonParseException(char expected, String actual, LineColumnTracker lct){
		super("Expected '"+expected+"' but it was '"+actual+"' at "+(lct!=null?lct.toString():"unknown"));
		this.expected = new char[] { expected };
		this.actual = actual;
		this.lct = lct;
	}
	
	public JsonParseException(char[] expected, String actual, LineColumnTracker lct){
		super("Expected "+charsToStr(expected)+" but it was '"+actual+"' at "+(lct!=null?lct.toString():"unknown"));
		this.expected = expected;
		this.actual = actual;
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
	 * Returns the actually read character (can be null)
	 * @return Actually read character
	 */
	public String getActualChars() {
		return actual;
	}
	
	/**
	 * Returns a copy of the {@link LineColumnTracker} that holds information 
	 * about the line and column where the error has occurred
	 * @return Line and column
	 */
	public LineColumnTracker getLineColumn() {
		return lct!=null ? lct.clone() : null;
	}
	
	protected static String charsToStr(char[] expected){
		StringBuilder sb = new StringBuilder();
		if(expected==null||expected.length==0){
			sb.append("?");
		} else {
			sb.append("'").append(expected[0]).append("'");
			for(int i=1; i<expected.length; i++){ sb.append(", '").append(expected[i]).append("'"); }
		} return sb.toString();
	}
}

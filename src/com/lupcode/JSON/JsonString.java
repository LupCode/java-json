package com.lupcode.JSON;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.lupcode.JSON.exceptions.JsonParseException;
import com.lupcode.JSON.exceptions.JsonParseUnfinishedException;
import com.lupcode.JSON.utils.LineColumnTracker;
import com.lupcode.Utilities.streams.UTF8CharInputStream;

/** Represents a JSON string
 * @author LupCode.com (Luca Vogels)
 * @since 2020-12-23
 */
public class JsonString extends JSON<JsonString> {

	private String value = null;
	
	public JsonString(){
		
	}
	
	public JsonString(String value){
		this.value = value;
	}
	
	/**
	 * Returns the value as {@link String}
	 * @return String value
	 */
	public String getValue(){
		return value;
	}
	
	/**
	 * Sets the value
	 * @param value String value that should be set
	 * @return This instance
	 */
	public JsonString setValue(String value){
		this.value = value; return this;
	}
	
	@Override
	protected void toJSON(OutputStream output, boolean prettyPrint, String whitespace) throws IOException {
		if(value!=null){
			output.write("\"".getBytes(StandardCharsets.UTF_8));
			output.write(value.getBytes(StandardCharsets.UTF_8));
			output.write("\"".getBytes(StandardCharsets.UTF_8));
		} else {
			new JsonNull().toJSON(output, prettyPrint, whitespace);
		}
	}
	
	@Override
	protected JsonString parseJSON(UTF8CharInputStream input, LineColumnTracker lct) throws JsonParseException, IOException {
		boolean ignore = false, ended = false;
		StringBuilder sb = new StringBuilder();
		
		String c = skipIgnorers(input, lct);
		if(c==null){ throw new JsonParseUnfinishedException(new char[]{'"', '\''}, lct); }
		if(!c.equals("\"") && !c.equals("\'"))
			throw new JsonParseException(new char[]{'"', '\''}, c, lct);
		String opener = c;
		lct.increaseColumn();
		
		while((c = input.readChar())!=null){
			if(!isLineBreaker(c)) {
				lct.increaseColumn();
			} else { lct.increaseLine(); }
			
			if(c.equals(opener)){
				if(ignore){
					sb.append(c);
					ignore = false;
				} else {
					ended = true;
					break;
				}
			} else if(c.equals("\\")){
				if(ignore){
					sb.append(c);
					ignore = false;
				} else {
					ignore = true;
				}
			} else {
				sb.append(c);
				ignore = false;
			}
		}
		if(!ended){ throw new JsonParseUnfinishedException(opener.charAt(0), lct); }
		
		this.value = sb.toString();
		return this;
	}
	
	/** Tries to parse a given JSON string as a {@link JsonString}
	 * @param json String that should be parsed
	 * @return Parsed JSON string
	 * @throws JsonParseException if JSON string could not be parsed correctly
	 * @throws NullPointerException if JSON string is null
	 */
	public static JsonString parse(String json) throws JsonParseException, NullPointerException {
		return new JsonString().parseJSON(json);
	}
	
	/** Tries to parse a given JSON string as a {@link JsonString}
	 * @param json String that should be parsed
	 * @param offset Offset where to start reading the JSON string
	 * @return Parse JSON string
	 * @throws JsonParseException if JSON string could not be parsed correctly
	 * @throws NullPointerException if JSON string is null
	 */
	public static JsonString parse(String json, int offset) throws JsonParseException, NullPointerException {
		return new JsonString().parseJSON(json, offset);
	}
	
	/** Tries to parse a {@link JsonString} from a given {@link InputStream} in UTF-8
	 * @param json Stream in UTF-8 that should be parsed
	 * @return Parse JSON string
	 * @throws JsonParseException if input could not be parsed correctly
	 * @throws IOException if an error while reading {@link InputStream} occurred
	 * @throws NullPointerException if input is null
	 */
	public static JsonString parse(UTF8CharInputStream input) throws JsonParseException, IOException, NullPointerException {
		return new JsonString().parseJSON(input);
	}
	
	/**
	 * Tries to parse a {@link JsonString} from a given {@link File}
	 * @param file File the JSON data should be read from
	 * @return Parsed JSON string
	 * @throws JsonParseException if file could not be parsed correctly
	 * @throws NullPointerException if file is null
	 * @throws IOException if an error occurs while reading the {@link File}
	 */
	public static JsonString parse(File file) throws JsonParseException, NullPointerException, IOException{
		return new JsonString().parseJSON(file);
	}
	
	/**
	 * Tries to parse a {@link JsonString} from a given {@link URL}
	 * @param url URL the JSON data should be read from
	 * @return Parsed JSON string
	 * @throws JsonParseException if {@link URL} could not be parsed correctly
	 * @throws NullPointerException if {@link URL} is null
	 * @throws IOException if an error occurs while reading the from the {@link URL}
	 */
	public static JsonString parse(URL url) throws JsonParseException, NullPointerException, IOException{
		return new JsonString().parseJSON(url);
	}
}

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
import com.lupcode.JSON.utils.UTF8CharInputStream;

/** Represents a JSON number
 * @author LupCode.com (Luca Vogels)
 * @since 2020-12-23
 */
public class JsonNumber extends JSON<JsonNumber> {

	private String value = "0";
	private boolean has_comma = false;
	
	public JsonNumber(){
		
	}
	
	public JsonNumber(String value){
		setValue(value);
	}
	
	public JsonNumber(double value){
		setValue(value);
	}
	
	public JsonNumber(float value){
		setValue(value);
	}
	
	public JsonNumber(int value){
		setValue(value);
	}
	
	public JsonNumber(long value){
		setValue(value);
	}
	
	public JsonNumber(short value){
		setValue(value);
	}
	
	public JsonNumber(byte value){
		setValue(value);
	}
	
	
	/**
	 * Returns if the number contains a comma
	 * @return True if comma is contained
	 */
	public boolean hasComma(){
		return has_comma;
	}
	
	/**
	 * Returns the value as {@link String}
	 * @return Number value
	 */
	public String getValue(){
		return value;
	}
	
	/**
	 * Sets the value (if null then zero)
	 * @param value Number value that should be set
	 * @return This instance
	 */
	public JsonNumber setValue(String value){
		if(value!=null){
			parseJSON(value, new LineColumnTracker());
		} else { this.value = "0"; this.has_comma = false; }
		return this;
	}
	
	public JsonNumber setValue(double value){
		this.value = value+""; this.has_comma = true; return this;
	}
	
	public JsonNumber setValue(float value){
		this.value = value+""; this.has_comma = true; return this;
	}
	
	public JsonNumber setValue(long value){
		this.value = value+""; this.has_comma = false; return this;
	}
	
	public JsonNumber setValue(short value){
		this.value = value+""; this.has_comma = false; return this;
	}
	
	public JsonNumber setValue(byte value){
		this.value = value+""; this.has_comma = false; return this;
	}
	
	public double getAsDouble(){
		return Double.parseDouble(value);
	}
	
	public float getAsFloat(){
		return Float.parseFloat(value);
	}
	
	public int getAsInt(){
		return Integer.parseInt(value);
	}
	
	public long getAsLong(){
		return Long.parseLong(value);
	}
	
	public short getAsShort(){
		return Short.parseShort(value);
	}
	
	public byte getAsByte(){
		return Byte.parseByte(value);
	}
	
	@Override
	protected void toJSON(OutputStream output, boolean prettyPrint, String whitespace) throws IOException {
		output.write(value.getBytes(StandardCharsets.UTF_8));
	}
	
	
	@Override
	protected JsonNumber parseJSON(UTF8CharInputStream input, LineColumnTracker lct) throws JsonParseException, IOException {
		StringBuilder sb = new StringBuilder();
		boolean had_number = false, need_number = false, has_comma = false;
		
		String c = skipIgnorers(input, lct);
		if(c==null){ throw new JsonParseUnfinishedException(new char[]{'0','1','2','3','4','5','6','7','8','9','-','+','.'}, lct); }
		
		if(c.equals("+") || c.equals("-")){
			sb.append(c); need_number = true;
		} else if(c.equals(".")){
			sb.append("0."); had_number = true; need_number = true; has_comma = true;
		} else if(isNumber(c)){
			sb.append(c); had_number = true;
		} else {  throw new JsonParseException(new char[]{'0','1','2','3','4','5','6','7','8','9','-','+','.'}, c, lct);  }
		lct.increaseColumn();
		
		while((c = skipIgnorers(input, lct))!=null){
			if(isNumber(c)){
				had_number = true; need_number = false;
				sb.append(c);
			} else if(c.equals(".")){
				if(has_comma){
					throw new JsonParseException(new char[]{'0','1','2','3','4','5','6','7','8','9'}, c, lct);
				}
				has_comma = true; need_number = true;
				if(had_number){ sb.append('.'); } else { sb.append("0."); had_number = true; }
			} else if(c.equals("+") || c.equals("-")){
				throw new JsonParseException(new char[]{'0','1','2','3','4','5','6','7','8','9'}, c, lct);
			} else { 
				input.insertReadAgainAtBeginning(c);
				break;
			}
			lct.increaseColumn();
		}
		if(need_number){ throw new JsonParseUnfinishedException(new char[]{'0','1','2','3','4','5','6','7','8','9'}, lct); }
		
		this.value = sb.toString();
		this.has_comma = has_comma;
		return this;
	}
	
	private boolean isNumber(String c){
		return c!=null && (c.equals("0") || c.equals("1") || c.equals("2") || c.equals("3") || 
				c.equals("4") || c.equals("5") || c.equals("6") || c.equals("7") || c.equals("8") || c.equals("9"));
	}
	
	/** Tries to parse a given JSON string as a {@link JsonNumber}
	 * @param json String that should be parsed
	 * @return Parsed JSON number
	 * @throws JsonParseException if JSON string could not be parsed correctly
	 * @throws NullPointerException if JSON string is null
	 */
	public static JsonNumber parse(String json) throws JsonParseException, NullPointerException {
		return new JsonNumber().parseJSON(json);
	}
	
	/** Tries to parse a given JSON string as a {@link JsonNumber}
	 * @param json String that should be parsed
	 * @param offset Offset where to start reading the JSON string
	 * @return Parse JSON number
	 * @throws JsonParseException if JSON string could not be parsed correctly
	 * @throws NullPointerException if JSON string is null
	 */
	public static JsonNumber parse(String json, int offset) throws JsonParseException, NullPointerException {
		return new JsonNumber().parseJSON(json, offset);
	}
	
	/** Tries to parse a {@link JsonNumber} from a given {@link InputStream} in UTF-8
	 * @param json Stream in UTF-8 that should be parsed
	 * @return Parse JSON number
	 * @throws JsonParseException if input could not be parsed correctly
	 * @throws IOException if an error while reading {@link InputStream} occurred
	 * @throws NullPointerException if input is null
	 */
	public static JsonNumber parse(UTF8CharInputStream input) throws JsonParseException, IOException, NullPointerException {
		return new JsonNumber().parseJSON(input);
	}
	
	/**
	 * Tries to parse a {@link JsonNumber} from a given {@link File}
	 * @param file File the JSON data should be read from
	 * @return Parsed JSON number
	 * @throws JsonParseException if file could not be parsed correctly
	 * @throws NullPointerException if file is null
	 * @throws IOException if an error occurs while reading the {@link File}
	 */
	public static JsonNumber parse(File file) throws JsonParseException, NullPointerException, IOException{
		return new JsonNumber().parseJSON(file);
	}
	
	/**
	 * Tries to parse a {@link JsonNumber} from a given {@link URL}
	 * @param url URL the JSON data should be read from
	 * @return Parsed JSON number
	 * @throws JsonParseException if {@link URL} could not be parsed correctly
	 * @throws NullPointerException if {@link URL} is null
	 * @throws IOException if an error occurs while reading the from the {@link URL}
	 */
	public static JsonNumber parse(URL url) throws JsonParseException, NullPointerException, IOException{
		return new JsonNumber().parseJSON(url);
	}
}

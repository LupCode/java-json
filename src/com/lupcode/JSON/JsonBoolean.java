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

/** Represents a JSON boolean
 * @author LupCode.com (Luca Vogels)
 * @since 2020-12-23
 */
public class JsonBoolean extends JSON<JsonBoolean> {

	private boolean value = false;
	
	public JsonBoolean(){
		
	}
	
	public JsonBoolean(boolean value){
		this.value = value;
	}
	
	/**
	 * Returns the value as boolean
	 * @return Boolean value
	 */
	public boolean getValue(){
		return value;
	}
	
	/**
	 * Sets the value
	 * @param value Boolean value that should be set
	 * @return This instance
	 */
	public JsonBoolean setValue(boolean value){
		this.value = value; return this;
	}
	
	@Override
	protected void toJSON(OutputStream output, boolean prettyPrint, String whitespace) throws IOException {
		output.write((value ? "true" : "false").getBytes(StandardCharsets.UTF_8));
	}

	@Override
	protected JsonBoolean parseJSON(UTF8CharInputStream input, LineColumnTracker lct) throws JsonParseException, IOException {
		
		String c = skipIgnorers(input, lct);
		if(c==null){ throw new JsonParseUnfinishedException(new char[]{'t','f'}, lct); }
		lct.increaseColumn();
		if(c.equalsIgnoreCase("t")){
			// parse true
			
			c = skipIgnorers(input, lct);
			if(c==null){ throw new JsonParseUnfinishedException('r', lct); }
			if(!c.equalsIgnoreCase("r")){ throw new JsonParseException('r', c, lct); }
			lct.increaseColumn();
			
			c = skipIgnorers(input, lct);
			if(c==null){ throw new JsonParseUnfinishedException('u', lct); }
			if(!c.equalsIgnoreCase("u")){ throw new JsonParseException('u', c, lct); }
			lct.increaseColumn();
			
			c = skipIgnorers(input, lct);
			if(c==null){ throw new JsonParseUnfinishedException('e', lct); }
			if(!c.equalsIgnoreCase("e")){ throw new JsonParseException('e', c, lct); }
			lct.increaseColumn();
			
			this.value = true;
			return this;
			
		} else if(c.equalsIgnoreCase("f")){
			// parse false
			
			c = skipIgnorers(input, lct);
			if(c==null){ throw new JsonParseUnfinishedException('a', lct); }
			if(!c.equalsIgnoreCase("a")){ throw new JsonParseException('a', c, lct); }
			lct.increaseColumn();
			
			c = skipIgnorers(input, lct);
			if(c==null){ throw new JsonParseUnfinishedException('l', lct); }
			if(!c.equalsIgnoreCase("l")){ throw new JsonParseException('l', c, lct); }
			lct.increaseColumn();
			
			c = skipIgnorers(input, lct);
			if(c==null){ throw new JsonParseUnfinishedException('s', lct); }
			if(!c.equalsIgnoreCase("s")){ throw new JsonParseException('s', c, lct); }
			lct.increaseColumn();
			
			c = skipIgnorers(input, lct);
			if(c==null){ throw new JsonParseUnfinishedException('e', lct); }
			if(!c.equalsIgnoreCase("e")){ throw new JsonParseException('e', c, lct); }
			lct.increaseColumn();
			
			this.value = false;
			return this;
			
		} else {
			throw new JsonParseException(new char[]{'t','f'}, c, lct);
		}
	}
	
	
	/** Tries to parse a given JSON string as an {@link JsonBoolean}
	 * @param json String that should be parsed
	 * @return Parsed JSON boolean
	 * @throws JsonParseException if JSON string could not be parsed correctly
	 * @throws NullPointerException if JSON string is null
	 */
	public static JsonBoolean parse(String json) throws JsonParseException, NullPointerException {
		return new JsonBoolean().parseJSON(json);
	}
	
	/** Tries to parse a given JSON string as an {@link JsonBoolean}
	 * @param json String that should be parsed
	 * @param offset Offset where to start reading the JSON string
	 * @return Parse JSON boolean
	 * @throws JsonParseException if JSON string could not be parsed correctly
	 * @throws NullPointerException if JSON string is null
	 */
	public static JsonBoolean parse(String json, int offset) throws JsonParseException, NullPointerException {
		return new JsonBoolean().parseJSON(json, offset);
	}
	
	/** Tries to parse a {@link JsonBoolean} from a given {@link InputStream} in UTF-8
	 * @param json Stream in UTF-8 that should be parsed
	 * @return Parse JSON boolean
	 * @throws JsonParseException if input could not be parsed correctly
	 * @throws IOException if an error while reading {@link InputStream} occurred
	 * @throws NullPointerException if input is null
	 */
	public static JsonBoolean parse(UTF8CharInputStream input) throws JsonParseException, IOException, NullPointerException {
		return new JsonBoolean().parseJSON(input);
	}
	
	/**
	 * Tries to parse a {@link JsonBoolean} from a given {@link File}
	 * @param file File the JSON data should be read from
	 * @return Parsed JSON boolean
	 * @throws JsonParseException if file could not be parsed correctly
	 * @throws NullPointerException if file is null
	 * @throws IOException if an error occurs while reading the {@link File}
	 */
	public static JsonBoolean parse(File file) throws JsonParseException, NullPointerException, IOException{
		return new JsonBoolean().parseJSON(file);
	}
	
	/**
	 * Tries to parse a {@link JsonBoolean} from a given {@link URL}
	 * @param url URL the JSON data should be read from
	 * @return Parsed JSON boolean
	 * @throws JsonParseException if {@link URL} could not be parsed correctly
	 * @throws NullPointerException if {@link URL} is null
	 * @throws IOException if an error occurs while reading the from the {@link URL}
	 */
	public static JsonBoolean parse(URL url) throws JsonParseException, NullPointerException, IOException{
		return new JsonBoolean().parseJSON(url);
	}
}

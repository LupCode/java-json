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

/** Represents the null values in JSON
 * @author LupCode.com (Luca Vogels)
 * @since 2020-12-23
 */
public class JsonNull extends JSON<JsonNull> {

	@Override
	protected void toJSON(OutputStream output, boolean prettyPrint, String whitespace) throws IOException {
		output.write("null".getBytes(StandardCharsets.UTF_8));
	}

	@Override
	protected JsonNull parseJSON(UTF8CharInputStream input, LineColumnTracker lct) throws JsonParseException, IOException {
		// TODO Auto-generated method stub
		String c = skipIgnorers(input, lct);
		if(c==null){ throw new JsonParseUnfinishedException('n', lct); }
		if(!c.equalsIgnoreCase("n")){ throw new JsonParseException('n', c, lct); }
		lct.increaseColumn();
		
		c = skipIgnorers(input, lct);
		if(c==null){ throw new JsonParseUnfinishedException(new char[]{'u','i'}, lct); }
		lct.increaseColumn();
		if(c.equalsIgnoreCase("u")){
			// parse null
			
			for(int i=0; i<2; i++){
				c = skipIgnorers(input, lct);
				if(c==null){ throw new JsonParseUnfinishedException(new char[]{'l'}, lct); }
				if(!c.equalsIgnoreCase("l")){ throw new JsonParseException('l', c, lct); }
				lct.increaseColumn();
			}
			
		} else if(c.equalsIgnoreCase("i")){
			// parse nil
			
			c = skipIgnorers(input, lct);
			if(c==null){ throw new JsonParseUnfinishedException(new char[]{'l'}, lct); }
			if(!c.equalsIgnoreCase("l")){ throw new JsonParseException('l', c, lct); }
			lct.increaseColumn();
			
		} else {
			throw new JsonParseException(new char[]{'u','i'}, c, lct);
		}
		return this;
	}
	
	
	/** Tries to parse a given JSON string as an {@link JsonNull}
	 * @param json String that should be parsed
	 * @return Parsed JSON null
	 * @throws JsonParseException if JSON string could not be parsed correctly
	 * @throws NullPointerException if JSON string is null
	 */
	public static JsonNull parse(String json) throws JsonParseException, NullPointerException {
		return new JsonNull().parseJSON(json);
	}
	
	/** Tries to parse a given JSON string as an {@link JsonNull}
	 * @param json String that should be parsed
	 * @param offset Offset where to start reading the JSON string
	 * @return Parse JSON null
	 * @throws JsonParseException if JSON string could not be parsed correctly
	 * @throws NullPointerException if JSON string is null
	 */
	public static JsonNull parse(String json, int offset) throws JsonParseException, NullPointerException {
		return new JsonNull().parseJSON(json, offset);
	}
	
	/** Tries to parse a {@link JsonNull} from a given {@link InputStream} in UTF-8
	 * @param json Stream in UTF-8 that should be parsed
	 * @return Parse JSON null
	 * @throws JsonParseException if input could not be parsed correctly
	 * @throws IOException if an error while reading {@link InputStream} occurred
	 * @throws NullPointerException if input is null
	 */
	public static JsonNull parse(UTF8CharInputStream input) throws JsonParseException, IOException, NullPointerException {
		return new JsonNull().parseJSON(input);
	}
	
	/**
	 * Tries to parse a {@link JsonNull} from a given {@link File}
	 * @param file File the JSON data should be read from
	 * @return Parsed JSON null
	 * @throws JsonParseException if file could not be parsed correctly
	 * @throws NullPointerException if file is null
	 * @throws IOException if an error occurs while reading the {@link File}
	 */
	public static JsonNull parse(File file) throws JsonParseException, NullPointerException, IOException{
		return new JsonNull().parseJSON(file);
	}
	
	/**
	 * Tries to parse a {@link JsonNull} from a given {@link URL}
	 * @param url URL the JSON data should be read from
	 * @return Parsed JSON null
	 * @throws JsonParseException if {@link URL} could not be parsed correctly
	 * @throws NullPointerException if {@link URL} is null
	 * @throws IOException if an error occurs while reading the from the {@link URL}
	 */
	public static JsonNull parse(URL url) throws JsonParseException, NullPointerException, IOException{
		return new JsonNull().parseJSON(url);
	}
}

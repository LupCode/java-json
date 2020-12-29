package com.lupcode.JSON;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import com.lupcode.JSON.exceptions.JsonParseException;
import com.lupcode.JSON.exceptions.JsonParseUnfinishedException;
import com.lupcode.JSON.utils.LineColumnTracker;
import com.lupcode.Utilities.others.UTF8String;
import com.lupcode.Utilities.streams.UTF8CharInputStream;

/** Abstract class for every JSON class that represents a JSON type
 * @param <THIS> must be the class that extends this parent class
 * @author LupCode.com (Luca Vogels)
 * @since 2020-12-23
 */
public abstract class JSON<THIS extends JSON<THIS>> {

	protected static UTF8String[] LINE_BREAKERS = new UTF8String[] {
			new UTF8String("\n"), new UTF8String("\r\n")
		};
	
	protected static UTF8String LINE_BREAKER = LINE_BREAKERS[0];
	protected static String SPACER = "  ";
	
	protected static UTF8String[] IGNORE_CHARS = new UTF8String[]{
			new UTF8String(" "), new UTF8String("\n"), new UTF8String("\r\n"), new UTF8String("\t"), 
			new UTF8String(SPACER)
		};
	
	
	
	@Override
	public final String toString() {
		return toString(false);
	}
	
	/** Converts data of this object to a JSON string
	 * @param prettyPrint If true will add extra spacers and line breaks for easy human reading
	 * @return Data represented by this object as JSON string
	 */
	public String toString(boolean prettyPrint){
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try { toStream(output, prettyPrint); } catch (Exception ex) {}
		return new String(output.toByteArray(), StandardCharsets.UTF_8);
	}
	
	/**
	 * Converts data of this object to a JSON string and writes it to the given file
	 * @param file File the data should be written into
	 * @param prettyPrint If true will add extra spacers and line breaks for easy human reading
	 * @throws IOException if error while writing occurs
	 */
	public void toFile(File file, boolean prettyPrint) throws IOException {
		FileOutputStream output = new FileOutputStream(file);
		toStream(output, prettyPrint);
		output.close();
	}
	
	/**
	 * Converts data of this object to a JSON string and writes it to a given {@link OutputStream}
	 * @param output Stream the data should be written to
	 * @param prettyPrint If true will add extra spacers and line breaks for easy human reading
	 * @throws IOException if error while writing occurs
	 */
	public void toStream(OutputStream output, boolean prettyPrint) throws IOException {
		toJSON(output, prettyPrint, "");
	}
	
	/** Appends the data of this object to the given {@link StringBuilder}
	 * @param output Stream the data of this object should be written to.
	 * @param prettyPrint If true will print extra spacers and line breaks for easy human reading
	 * @param whitespace Whitespace that must be added in front of data if pretty_print is true. 
	 * Append on every recursive call a new spacer.
	 * @return Data represented by this object as JSON string
	 * @throws IOException if writing to {@link OutputStream} fails
	 */
	protected abstract void toJSON(OutputStream output, boolean prettyPrint, String whitespace) throws IOException;
	
	
	/** Tries to parse the JSON data from a given {@link String}
	 * @param json String that should be parsed
	 * @return This instance
	 * @throws JsonParseException if JSON string could not be parsed correctly
	 * @throws NullPointerException if JSON string is null
	 */
	public THIS parseJSON(String json) throws JsonParseException, NullPointerException {
		if(json==null){ throw new NullPointerException("Json string cannot be null"); }
		return parseJSON(json, new LineColumnTracker());
	}
	
	/** Tries to parse the JSON data from a given {@link String}
	 * @param json String that should be parsed
	 * @param offset Offset where to start reading the JSON string
	 * @return This instance
	 * @throws JsonParseException if JSON string could not be parsed correctly
	 * @throws NullPointerException if JSON string is null
	 */
	public THIS parseJSON(String json, int offset) throws JsonParseException, NullPointerException {
		if(json==null){ throw new NullPointerException("Json string cannot be null"); }
		return parseJSON(json.substring(offset), new LineColumnTracker(offset));
	}
	
	/** Tries to parse the JSON data from a given {@link String} in UTF-8
	 * @param json String that should be parsed (doesn't be null)
	 * @param lct Tracker for error feedback (doesn't be null)
	 * @return This instance
	 * @throws JsonParseException if JSON string could not be parsed correctly
	 */
	protected THIS parseJSON(String json, LineColumnTracker lct) throws JsonParseException {
		try {
			return parseJSON(new UTF8CharInputStream(new ByteArrayInputStream(json.getBytes())), lct);
		} catch (IOException ex) {
			throw new JsonParseException(ex);
		}
	}
	
	
	/** Tries to parse the JSON data from a given {@link InputStream} in UTF-8
	 * @param json Stream in UTF-8 that should be parsed
	 * @return This instance
	 * @throws JsonParseException if input could not be parsed correctly
	 * @throws IOException if an error while reading {@link InputStream} occurred
	 * @throws NullPointerException if input is null
	 */
	public THIS parseJSON(InputStream input) throws JsonParseException, IOException, NullPointerException {
		if(input==null){ throw new NullPointerException("InputStream cannot be null"); }
		return parseJSON(new UTF8CharInputStream(input), new LineColumnTracker());
	}
	
	/** Tries to parse the JSON data from a given {@link InputStream} in UTF-8
	 * @param json Stream in UTF-8 that should be parsed
	 * @return This instance
	 * @throws JsonParseException if input could not be parsed correctly
	 * @throws IOException if an error occurs while reading the {@link InputStream}
	 * @throws NullPointerException if input is null
	 */
	public THIS parseJSON(UTF8CharInputStream input) throws JsonParseException, IOException, NullPointerException {
		if(input==null){ throw new NullPointerException("InputStream cannot be null"); }
		return parseJSON(input, new LineColumnTracker());
	}
	
	/**
	 * Tries to parse the JSON data from a given {@link File}
	 * @param file File the data should be read from
	 * @return This instance
	 * @throws NullPointerException if file is null
	 * @throws JsonParseException if an error while reading the {@link File}
	 * @throws IOException if an error occurs while reading the {@link File}
	 */
	public THIS parseJSON(File file) throws NullPointerException, JsonParseException, IOException {
		if(file==null) throw new NullPointerException("File cannot be null");
		FileInputStream input = new FileInputStream(file);
		THIS o = parseJSON(input);
		try { input.close(); } catch (Exception e) {}
		return o;
	}
	
	/**
	 * Tries to parse the JSON data from a given {@link URL}
	 * @param url URL the data should be read from
	 * @return This instance
	 * @throws NullPointerException if file is null
	 * @throws JsonParseException if an error while reading from the {@link URL}
	 * @throws IOException if an error occurs while reading from the {@link URL}
	 */
	public THIS parseJSON(URL url) throws NullPointerException, JsonParseException, IOException {
		if(url==null) throw new NullPointerException("URL cannot be null");
		InputStream input = url.openStream();
		THIS o = parseJSON(input);
		try { input.close(); } catch (Exception e) {}
		return o;
	}
	
	/** Tries to parse the JSON data from a given {@link DataInputStream}
	 * @param input Stream that should be parsed (doesn't be null)
	 * @param lct Tracker for error feedback (doesn't be null)
	 * @return This instance
	 * @throws JsonParseException if input could not be parsed correctly
	 * @throws IOException if an error while reading {@link UTF8CharInputStream} occurred
	 */
	protected abstract THIS parseJSON(UTF8CharInputStream input, LineColumnTracker lct) throws JsonParseException, IOException;
	
	
	/**
	 * Prints multi-line text with line numbers in front
 	 * @param text Text that should be printed
	 */
	public static void printWithLineNumbers(String text) {
		if(text==null || text.length()==0 || LINE_BREAKERS.length==0) { System.out.println(text); return; }
		StringBuilder sb = new StringBuilder();
		for(UTF8String lb : LINE_BREAKERS)
			sb.append("|").append(lb.toString());
		if(sb.length()<=1) { System.out.println(text); return; }
		String[] lines = text.split(sb.substring(1));
		int line = 1;
		for(String str : lines) {
			System.out.println((line++)+".\t"+str);
		}
	}
	
	
	/** Tries to parse the JSON string and automatically detect the JSON data type that is parsed
	 * @param json String that should be parsed
	 * @param offset Offset where to start reading the JSON string
	 * @return JSON data type that was detected containing the parsed information
	 * @throws JsonParseException if JSON string could not be parsed correctly
	 * @throws NullPointerException if JSON string is null
	 */
	public static JSON<?> parseAutoJSON(String json) throws JsonParseException, NullPointerException {
		return parseAutoJSON(json, 0);
	}
	
	/** Tries to parse the JSON string and automatically detect the JSON data type that is parsed
	 * @param json String that should be parsed
	 * @param offset Offset where to start reading the JSON string
	 * @return JSON data type that was detected containing the parsed information
	 * @throws JsonParseException if JSON string could not be parsed correctly
	 * @throws NullPointerException if JSON string is null
	 */
	public static JSON<?> parseAutoJSON(String json, int offset) throws JsonParseException, NullPointerException {
		return parseAutoJSON(json, new LineColumnTracker(offset));
	}
	
	/** Tries to parse the JSON string and automatically detect the JSON data type that is parsed
	 * @param json String that should be parsed
	 * @param lct Tracker for offset, line and column tracking
	 * @return JSON data type that was detected containing the parsed information
	 * @throws JsonParseException if JSON string could not be parsed correctly
	 * @throws NullPointerException if JSON string is null
	 */
	protected static JSON<?> parseAutoJSON(String json, LineColumnTracker lct) throws JsonParseException, NullPointerException {
		if(json==null){ throw new NullPointerException("Json string cannot be null"); }
		try {
			return parseAutoJSON(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));
		} catch (IOException e) { return null; }
	}
	
	
	/** Tries to parse the JSON data and automatically detect the JSON data type that is parsed
	 * @param input Stream that should be parsed in UTF-8
	 * @return JSON data type that was detected containing the parsed information
	 * @throws JsonParseException if input could not be parsed correctly
	 * @throws IOException if an error occurs while reading {@link InputStream} occurred
	 * @throws NullPointerException if {@link InputStream} is null
	 */
	public static JSON<?> parseAutoJSON(InputStream input) throws JsonParseException, NullPointerException, IOException{
		if(input==null){ throw new NullPointerException("InputStream cannot be null"); }
		return parseAutoJSON(new UTF8CharInputStream(input), new LineColumnTracker());
	}
	
	/** Tries to parse the JSON data and automatically detect the JSON data type that is parsed
	 * @param input Stream that should be parsed in UTF-8
	 * @return JSON data type that was detected containing the parsed information
	 * @throws JsonParseException if input could not be parsed correctly
	 * @throws IOException if an error occurred while reading {@link InputStream} occurred
	 * @throws NullPointerException if {@link InputStream} is null
	 */
	public static JSON<?> parseAutoJSON(UTF8CharInputStream input) throws JsonParseException, NullPointerException, IOException{
		if(input==null){ throw new NullPointerException("InputStream cannot be null"); }
		return parseAutoJSON(input, new LineColumnTracker());
	}
	
	/**
	 * Tries to parse the JSON data and automatically detect the JSON data type that is parsed
	 * @param file File the JSON data should be read from
	 * @return JSON data type that was detected containing the parse information
	 * @throws JsonParseException if file could not be parsed correctly
	 * @throws NullPointerException if file is null
	 * @throws IOException if an error occurs while reading the {@link File}
	 */
	public static JSON<?> parseAutoJSON(File file) throws JsonParseException, NullPointerException, IOException{
		if(file==null) throw new NullPointerException("File cannot be null");
		FileInputStream input = new FileInputStream(file);
		JSON<?> obj = parseAutoJSON(input);
		try { input.close(); } catch (Exception e) {}
		return obj;
	}
	
	/**
	 * Tries to parse the JSON data and automatically detect the JSON data type that is parsed
	 * @param url URL the JSON data should be read from
	 * @return JSON data type that was detected containing the parse information
	 * @throws JsonParseException if {@link URL} could not be parsed correctly
	 * @throws NullPointerException if {@link URL} is null
	 * @throws IOException if an error occurs while reading the from the {@link URL}
	 */
	public static JSON<?> parseAutoJSON(URL url) throws JsonParseException, NullPointerException, IOException{
		if(url==null) throw new NullPointerException("File cannot be null");
		InputStream input = url.openStream();
		JSON<?> obj = parseAutoJSON(input);
		try { input.close(); } catch (Exception e) {}
		return obj;
	}
	
	/** Tries to parse automatically detect JSON data from {@link UTF8CharInputStream}
	 * @param json string that should be parsed
	 * @param lct for offset, line and column tracking
	 * @return JSON data type that was detected containing the parsed information
	 * @throws JsonParseException if input could not be parsed correctly
	 * @throws IOException if an error while reading {@link InputStream} occurred
	 * @throws NullPointerException if {@link InputStream} is null
	 */
	protected static JSON<?> parseAutoJSON(UTF8CharInputStream input, LineColumnTracker lct) throws JsonParseException, IOException, NullPointerException {
		if(input==null){ throw new NullPointerException("InputStream cannot be null"); }
		String ch = skipIgnorers(input, lct);
		if(ch==null){ throw new JsonParseUnfinishedException(lct); }
		input.insertReadAgainAtBeginning(new UTF8String(ch));
		final char c = ch.charAt(0);
		switch (c) {
			case '{': return new JsonObject().parseJSON(input, lct);
			case '[': return new JsonArray().parseJSON(input, lct);
			case '\'':
			case '"': return new JsonString().parseJSON(input, lct);
			case 't': 
			case 'T':
			case 'f':
			case 'F': return new JsonBoolean().parseJSON(input, lct);
			case '0': 
			case '1': 
			case '2': 
			case '3': 
			case '4': 
			case '5': 
			case '6': 
			case '7': 
			case '8': 
			case '9': 
			case '-': 
			case '+':
			case '.': return new JsonNumber().parseJSON(input, lct);
			case 'n':
			case 'N': return new JsonNull().parseJSON(input, lct);
			default: throw new JsonParseException("Could not detect json data type at "+lct.toString());
		}
	}
	
	
	protected static boolean isLineBreaker(String str) {
		if(str==null) return false;
		for(UTF8String lb : LINE_BREAKERS)
			if(Arrays.equals(lb.getBytes(), str.getBytes(StandardCharsets.UTF_8))) return true;
		return false;
	}
	protected static boolean isLineBreaker(UTF8String str) {
		if(str==null) return false;
		for(UTF8String lb : LINE_BREAKERS)
			if(lb.equals(str))
				return true;
		return false;
	}
	
	/** Skips whitespace and returns next valid char
	 * @param input that should be checked for whitespace
	 * @param lct for offset, line and column tracking
	 * @param include_linebreak if line breaks should also be skipped
	 * @return next valid char
	 * @throws IOException 
	 */
	protected static String skipIgnorers(UTF8CharInputStream input, LineColumnTracker lct) throws IOException {
		if(lct==null){ throw new NullPointerException("LineColumnTracker cannot be null"); }
		if(input==null){ return null; }
		int index = 0;
		boolean[] cancelChecking = new boolean[IGNORE_CHARS.length];
		boolean cancel;
		UTF8String buffer = new UTF8String();
		String c;
		while((c = input.readChar())!=null){
			buffer.add(c);
			cancel = true;
			
			for(int i=0; i<cancelChecking.length; i++) {
				if(cancelChecking[i]) continue;
				UTF8String ic = IGNORE_CHARS[i];
				if(index < ic.length() && c.equals(ic.charAt(index))) {
					if(index+1 < ic.length()) {
						cancel = false;
					} else {
						if(!isLineBreaker(buffer)) {
							lct.increaseColumn(buffer.length());
						} else { lct.increaseLine(); }
						return skipIgnorers(input, lct);
					}
				} else { cancelChecking[i] = true; }
			}
			if(cancel) {
				break;
			} index++;
		}
		if(buffer.length()==1) return buffer.toString(); // directly return single char in buffer
		input.insertReadAgainAtBeginning(buffer); // put multiple read chars back and read only one char again
		return buffer.isEmpty() ? null : input.readChar();
	}
}

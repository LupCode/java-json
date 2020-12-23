package com.lupcode.JSON;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.lupcode.JSON.exceptions.JsonParseException;
import com.lupcode.JSON.exceptions.JsonParseUnfinishedException;
import com.lupcode.JSON.utils.LineColumnTracker;
import com.lupcode.JSON.utils.UTF8CharInputStream;

import java.util.Set;

/** Represents a JSON object
 * @author LupCode.com (Luca Vogels)
 * @since 2020-12-23
 */
public class JsonObject extends JSON<JsonObject> {
	
	private Map<String, JSON<?>> values = new LinkedHashMap<>();
	
	public JsonObject(){
		
	}
	
	public JsonObject(Collection<JSON<?>> values){
		if(values!=null){
			int index = 0;
			for(JSON<?> value : values){
				this.values.put(index+"", value);
				index++;
			}
		}
	}
	
	public int size(){
		return values.size();
	}
	
	public JsonObject clear(){
		values.clear();
		return this;
	}
	
	public boolean has(String key){
		return key!=null && values.containsKey(key);
	}
	
	public boolean containsKey(String key){
		return key!=null && values.containsKey(key);
	}
	
	public boolean containsValue(JSON<?> object){
		return values.containsValue(object);
	}
	
	public JSON<?> get(String key){
		return key!=null ? values.get(key) : null;
	}
	
	public JsonArray getAsArray(String key) throws ClassCastException {
		JSON<?> obj = get(key);
		return (obj!=null && !(obj instanceof JsonNull)) ? (JsonArray)obj : null;
	}
	
	public JsonBoolean getAsJBoolean(String key) throws ClassCastException {
		JSON<?> obj = get(key);
		return (obj!=null && !(obj instanceof JsonNull)) ? (JsonBoolean)obj : null;
	}
	
	public Boolean getAsBoolean(String key) throws ClassCastException {
		JSON<?> obj = get(key);
		return (obj!=null && !(obj instanceof JsonNull)) ? ((JsonBoolean)obj).getValue() : null;
	}
	
	public JsonNumber getAsNumber(String key) throws ClassCastException {
		JSON<?> obj = get(key);
		return (obj!=null && !(obj instanceof JsonNull)) ? (JsonNumber)obj : null;
	}
	
	public Double getAsDouble(String key) throws ClassCastException { 
		JSON<?> obj = get(key);
		return (obj!=null && !(obj instanceof JsonNull)) ? ((JsonNumber)obj).getAsDouble() : null;
	}
	
	public Float getAsFloat(String key) throws ClassCastException { 
		JSON<?> obj = get(key);
		return (obj!=null && !(obj instanceof JsonNull)) ? ((JsonNumber)obj).getAsFloat() : null;
	}
	
	public Integer getAsInt(String key) throws ClassCastException { 
		JSON<?> obj = get(key);
		return (obj!=null && !(obj instanceof JsonNull)) ? ((JsonNumber)obj).getAsInt() : null;
	}
	
	public Long getAsLong(String key) throws ClassCastException { 
		JSON<?> obj = get(key);
		return (obj!=null && !(obj instanceof JsonNull)) ? ((JsonNumber)obj).getAsLong() : null;
	}
	
	public Short getAsShort(String key) throws ClassCastException { 
		JSON<?> obj = get(key);
		return (obj!=null && !(obj instanceof JsonNull)) ? ((JsonNumber)obj).getAsShort() : null;
	}
	
	public Byte getAsByte(String key) throws ClassCastException { 
		JSON<?> obj = get(key);
		return (obj!=null && !(obj instanceof JsonNull)) ? ((JsonNumber)obj).getAsByte() : null;
	}
	
	public JsonObject getAsObject(String key) throws ClassCastException {
		JSON<?> obj = get(key);
		return (obj!=null && !(obj instanceof JsonNull)) ? (JsonObject)obj : null;
	}
	
	public JsonString getAsJString(String key) throws ClassCastException {
		JSON<?> obj = get(key);
		return (obj!=null && !(obj instanceof JsonNull)) ? (JsonString)obj : null;
	}
	
	public String getAsString(String key) throws ClassCastException {
		JSON<?> obj = get(key);
		return (obj!=null && !(obj instanceof JsonNull)) ? ((JsonString)obj).getValue() : null;
	}
	
	public JsonObject put(String key, JSON<?> object){
		if(key==null){ throw new NullPointerException("Key cannot be null"); }
		values.put(key, object!=null ? object : new JsonNull());
		return this;
	}
	
	public JsonObject put(String key, String value){
		return put(key, new JsonString(value));
	}
	
	public JsonObject put(String key, boolean value){
		return put(key, new JsonBoolean(value));
	}
	
	public JsonObject put(String key, double value){
		return put(key, new JsonNumber(value));
	}
	
	public JsonObject put(String key, float value){
		return put(key, new JsonNumber(value));
	}
	
	public JsonObject put(String key, int value){
		return put(key, new JsonNumber(value));
	}
	
	public JsonObject put(String key, long value){
		return put(key, new JsonNumber(value));
	}
	
	public JsonObject put(String key, short value){
		return put(key, new JsonNumber(value));
	}
	
	public JsonObject put(String key, byte value){
		return put(key, new JsonNumber(value));
	}
	
	public JsonObject putArray(String key, JSON<?>... values){
		return put(key, new JsonArray(values));
	}
	
	public JsonObject putArray(String key, Collection<JSON<?>> values){
		return put(key, new JsonArray(values));
	}
	
	public JSON<?> remove(String key){
		return key!=null ? values.remove(key) : null;
	}
	
	public Set<String> listKeys(){
		return values.keySet();
	}
	
	public Collection<JSON<?>> listValues(){
		return values.values();
	}
	
	public Set<Entry<String, JSON<?>>> entrySet(){
		return values.entrySet();
	}
	
	public JsonArray keysAsJsonArray(){
		return new JsonArray(values.keySet());
	}

	public JsonArray toJsonArray(){
		return new JsonArray(values.values());
	}
	
	@Override
	protected void toJSON(OutputStream output, boolean prettyPrint, String whitespace) throws IOException {
		output.write("{".getBytes(StandardCharsets.UTF_8));
		if(!values.isEmpty()){
			if(prettyPrint){
				boolean notFirst = false;
				String ws = whitespace + SPACER;
				byte[] wsArr = ws.getBytes(StandardCharsets.UTF_8);
				for(Entry<String, JSON<?>> entry : values.entrySet()){
					if(notFirst) output.write(",".getBytes(StandardCharsets.UTF_8)); else notFirst = true;
					output.write(LINE_BREAKER.getBytes());
					output.write(wsArr);
					output.write("\"".getBytes(StandardCharsets.UTF_8));
					output.write(entry.getKey().getBytes(StandardCharsets.UTF_8));
					output.write("\": ".getBytes(StandardCharsets.UTF_8));
					
					(entry.getValue()!=null ? entry.getValue() : new JsonNull()).toJSON(output, prettyPrint, ws);
				}
				output.write(LINE_BREAKER.getBytes());
				output.write(whitespace.getBytes(StandardCharsets.UTF_8));
			} else {
				boolean notFirst = false;
				for(Entry<String, JSON<?>> entry : values.entrySet()){
					if(notFirst) output.write(",".getBytes(StandardCharsets.UTF_8)); else notFirst = true;
					output.write("\"".getBytes(StandardCharsets.UTF_8));
					output.write(entry.getKey().getBytes(StandardCharsets.UTF_8));
					output.write("\": ".getBytes(StandardCharsets.UTF_8));
					(entry.getValue()!=null ? entry.getValue() : new JsonNull()).toJSON(output, prettyPrint, whitespace);
				}
			}
		}
		output.write("}".getBytes(StandardCharsets.UTF_8));
	}
	

	@Override
	protected JsonObject parseJSON(UTF8CharInputStream input, LineColumnTracker lct) throws JsonParseException, IOException {
		String c = skipIgnorers(input, lct);
		if(c==null){ throw new JsonParseUnfinishedException('{', lct); }
		if(!c.equals("{")){ throw new JsonParseException('{', c, lct); }
		lct.increaseColumn();
		
		c = skipIgnorers(input, lct);
		if(c==null){ throw new JsonParseUnfinishedException(lct); }
		if(c.equals("}")){ lct.increaseColumn(); this.values.clear(); return this; }
		
		input.insertReadAgainAtBeginning(c); // to parse JsonString next
		
		boolean nextEntry = false;
		Map<String, JSON<?>> map = new LinkedHashMap<>();
		do {
			if(nextEntry) lct.increaseColumn(); // increment for ',' 
			
			// check if empty ','
			c = skipIgnorers(input, lct);
			if(c!=null && c.equals("}")) break;
			input.insertReadAgainAtBeginning(c);
			
			// read key as JsonString
			JsonString str = new JsonString().parseJSON(input, lct);
			c = skipIgnorers(input, lct);
			if(c==null){ throw new JsonParseUnfinishedException(':', lct); }
			if(!c.equals(":")){ throw new JsonParseException(':', c, lct); }
			lct.increaseColumn();
			
			// read value as JSON<?>
			map.put(str.getValue(), parseAutoJSON(input, lct));

			c = skipIgnorers(input, lct);
		} while((nextEntry = (c!=null && c.equals(","))));
		
		if(c==null){ throw new JsonParseUnfinishedException('}', lct); }
		if(!c.equals("}")){ throw new JsonParseException('}', c, lct); }
		lct.increaseColumn();
		
		this.values = map;
		return this;
	}
	
	/** Tries to parse a given JSON string as a {@link JsonObject}
	 * @param json String that should be parsed
	 * @return Parsed JSON object
	 * @throws JsonParseException if JSON string could not be parsed correctly
	 * @throws NullPointerException if JSON string is null
	 */
	public static JsonObject parse(String json) throws JsonParseException, NullPointerException {
		return new JsonObject().parseJSON(json);
	}
	
	/** Tries to parse a given JSON string as a {@link JsonObject}
	 * @param json String that should be parsed
	 * @param offset Offset where to start reading the JSON string
	 * @return Parse JSON object
	 * @throws JsonParseException if JSON string could not be parsed correctly
	 * @throws NullPointerException if JSON string is null
	 */
	public static JsonObject parse(String json, int offset) throws JsonParseException, NullPointerException {
		return new JsonObject().parseJSON(json, offset);
	}
	
	/** Tries to parse a {@link JsonObject} from a given {@link InputStream} in UTF-8
	 * @param json Stream in UTF-8 that should be parsed
	 * @return Parse JSON object
	 * @throws JsonParseException if input could not be parsed correctly
	 * @throws IOException if an error while reading {@link InputStream} occurred
	 * @throws NullPointerException if input is null
	 */
	public static JsonObject parse(UTF8CharInputStream input) throws JsonParseException, IOException, NullPointerException {
		return new JsonObject().parseJSON(input);
	}
	
	/**
	 * Tries to parse a {@link JsonObject} from a given {@link File}
	 * @param file File the JSON data should be read from
	 * @return Parsed JSON object
	 * @throws JsonParseException if file could not be parsed correctly
	 * @throws NullPointerException if file is null
	 * @throws IOException if an error occurs while reading the {@link File}
	 */
	public static JsonObject parse(File file) throws JsonParseException, NullPointerException, IOException{
		return new JsonObject().parseJSON(file);
	}
	
	/**
	 * Tries to parse a {@link JsonObject} from a given {@link URL}
	 * @param url URL the JSON data should be read from
	 * @return Parsed JSON object
	 * @throws JsonParseException if {@link URL} could not be parsed correctly
	 * @throws NullPointerException if {@link URL} is null
	 * @throws IOException if an error occurs while reading the from the {@link URL}
	 */
	public static JsonObject parse(URL url) throws JsonParseException, NullPointerException, IOException{
		return new JsonObject().parseJSON(url);
	}
}

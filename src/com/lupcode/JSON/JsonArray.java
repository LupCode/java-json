package com.lupcode.JSON;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import com.lupcode.JSON.exceptions.JsonParseException;
import com.lupcode.JSON.exceptions.JsonParseUnfinishedException;
import com.lupcode.JSON.utils.LineColumnTracker;
import com.lupcode.JSON.utils.UTF8CharInputStream;

/** Represents a JSON array
 * @author LupCode.com (Luca Vogels)
 * @since 2020-12-23
 */
public class JsonArray extends JSON<JsonArray> implements List<JSON<?>> {

	private ArrayList<JSON<?>> values = new ArrayList<>();
	
	public JsonArray() {
		
	}
	
	public JsonArray(JSON<?>... values) {
		addAll(values);
	}
	
	public JsonArray(Collection<JSON<?>> values) {
		addAll(values);
	}
	
	JsonArray(Set<String> values){
		if(values!=null){
			for(String v : values){
				this.values.add(v!=null ? new JsonString(v) : new JsonNull());
			}
		}
	}
	
	
	public int size(){
		return values.size();
	}
	
	
	public JSON<?> get(int index){
		return values.get(index);
	}
	
	public JsonObject getAsJsonObject(int index) {
		JSON<?> obj = values.get(index);
		return (obj!=null && !(obj instanceof JsonNull)) ? (JsonObject)obj : null;
	}
	
	public JsonArray getAsJsonArray(int index) {
		JSON<?> obj = values.get(index);
		return (obj!=null && !(obj instanceof JsonNull)) ? (JsonArray)obj : null;
	}
	
	public String getAsString(int index) {
		JSON<?> obj = values.get(index);
		return (obj!=null && !(obj instanceof JsonNull)) ? ((JsonString)obj).getValue() : null;
	}
	
	public Byte getAsByte(int index) {
		JSON<?> obj = values.get(index);
		return (obj!=null && !(obj instanceof JsonNull)) ? ((JsonNumber)obj).getAsByte() : null;
	}
	
	public Short getAsShort(int index) {
		JSON<?> obj = values.get(index);
		return (obj!=null && !(obj instanceof JsonNull)) ? ((JsonNumber)obj).getAsShort() : null;
	}
	
	public Integer getAsInt(int index) {
		JSON<?> obj = values.get(index);
		return (obj!=null && !(obj instanceof JsonNull)) ? ((JsonNumber)obj).getAsInt() : null;
	}
	
	public Long getAsLong(int index) {
		JSON<?> obj = values.get(index);
		return (obj!=null && !(obj instanceof JsonNull)) ? ((JsonNumber)obj).getAsLong() : null;
	}
	
	public Float getAsFloat(int index) {
		JSON<?> obj = values.get(index);
		return (obj!=null && !(obj instanceof JsonNull)) ? ((JsonNumber)obj).getAsFloat() : null;
	}
	
	public Double getAsDouble(int index) {
		JSON<?> obj = values.get(index);
		return (obj!=null && !(obj instanceof JsonNull)) ? ((JsonNumber)obj).getAsDouble() : null;
	}
	
	
	public JsonArray insert(int index, JSON<?> value){
		this.values.add(index, value!=null ? value : new JsonNull()); return this;
	}
	
	public JsonArray insert(int index, String value){
		return insert(index, new JsonString(value));
	}
	
	public JsonArray insert(int index, boolean value){
		return insert(index, new JsonBoolean(value));
	}
	
	public JsonArray insert(int index, double value){
		return insert(index, new JsonNumber(value));
	}
	
	public JsonArray insert(int index, float value){
		return insert(index, new JsonNumber(value));
	}
	
	public JsonArray insert(int index, int value){
		return insert(index, new JsonNumber(value));
	}
	
	public JsonArray insert(int index, long value){
		return insert(index, new JsonNumber(value));
	}
	
	public JsonArray insert(int index, short value){
		return insert(index, new JsonNumber(value));
	}
	
	public JsonArray insert(int index, byte value){
		return insert(index, new JsonNumber(value));
	}
	
	public JsonArray set(int index, JSON<?> value){
		this.values.set(index, value!=null ? value : new JsonNull()); return this;
	}
	
	public JsonArray set(int index, String value){
		return set(index, new JsonString(value));
	}
	
	public JsonArray set(int index, boolean value){
		return set(index, new JsonBoolean(value));
	}
	
	public JsonArray set(int index, double value){
		return set(index, new JsonNumber(value));
	}
	
	public JsonArray set(int index, float value){
		return set(index, new JsonNumber(value));
	}
	
	public JsonArray set(int index, int value){
		return set(index, new JsonNumber(value));
	}
	
	public JsonArray set(int index, long value){
		return set(index, new JsonNumber(value));
	}
	
	public JsonArray set(int index, short value){
		return set(index, new JsonNumber(value));
	}
	
	public JsonArray set(int index, byte value){
		return set(index, new JsonNumber(value));
	}
	
	
	public boolean add(String value){
		return add(value!=null ? new JsonString(value) : new JsonNull());
	}
	
	public boolean add(boolean value){
		return add(new JsonBoolean(value));
	}
	
	public boolean add(double value){
		return add(new JsonNumber(value));
	}
	
	public boolean add(float value){
		return add(new JsonNumber(value));
	}
	
	public boolean add(int value){
		return add(new JsonNumber(value));
	}
	
	public boolean add(long value){
		return add(new JsonNumber(value));
	}
	
	public boolean add(short value){
		return add(new JsonNumber(value));
	}
	
	public boolean add(byte value){
		return add(new JsonNumber(value));
	}
	
	@Override
	public boolean add(JSON<?> e) {
		return values.add(e!=null ? e : new JsonNull());
	}
	
	public boolean addArray(JSON<?>... values){
		return add((JSON<?>)new JsonArray(values));
	}
	
	public boolean addArray(Collection<JSON<?>> values){
		return add((JSON<?>)new JsonArray(values));
	}
	
	public JsonArray addAll(JSON<?>... values){
		if(values!=null){
			for(JSON<?> json : values){
				this.values.add(json!=null ? json : new JsonNull());
			}
		} return this;
	}
	
	public JSON<?> remove(int index){
		return values.remove(index);
	}
	
	public boolean remove(JSON<?> object){
		return values.remove(object);
	}
	
	public boolean contains(JSON<?> object){
		return values.contains(object);
	}
	
	public List<JSON<?>> toList(int fromIndex, int toIndex){
		return new ArrayList<>(values.subList(fromIndex, toIndex));
	}
	
	public List<JSON<?>> toList(){
		return new ArrayList<>(values);
	}
	
	public JsonObject toJsonObject(){
		return new JsonObject(values);
	}
	
	@Override
	protected void toJSON(OutputStream output, boolean prettyPrint, String whitespace) throws IOException {
		output.write("[".getBytes(StandardCharsets.UTF_8));
		if(!values.isEmpty()){
			if(prettyPrint){
				boolean notFirst = false;
				String ws = (whitespace + SPACER);
				byte[] wsArr = (whitespace + SPACER).getBytes(StandardCharsets.UTF_8);
				for(JSON<?> obj : values){
					if(notFirst) output.write(",".getBytes(StandardCharsets.UTF_8)); else notFirst = true;
					output.write(LINE_BREAKER.getBytes());
					output.write(wsArr);
					obj.toJSON(output, prettyPrint, ws);
				}
				output.write(LINE_BREAKER.getBytes());
				output.write(whitespace.getBytes(StandardCharsets.UTF_8));
				
			} else {
				boolean notFirst = false;
				for(JSON<?> obj : values){
					if(notFirst) output.write(",".getBytes(StandardCharsets.UTF_8)); else notFirst = true;
					obj.toJSON(output, prettyPrint, whitespace);
				}
			}
		}
		output.write("]".getBytes(StandardCharsets.UTF_8));
	}

	@Override
	public boolean isEmpty() {
		return values.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return values.contains(o);
	}

	@Override
	public Iterator<JSON<?>> iterator() {
		return values.iterator();
	}

	@Override
	public Object[] toArray() {
		return values.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return values.toArray(a);
	}

	@Override
	public boolean remove(Object o) {
		return values.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return values.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends JSON<?>> c) {
		boolean v = true;
		for(JSON<?> o : c) {
			v &= values.add(o!=null ? o : new JsonNull());
		} return v;
	}

	@Override
	public boolean addAll(int index, Collection<? extends JSON<?>> c) {
		for(JSON<?> o : c) {
			values.add(index++, o!=null ? o : new JsonNull());
		} return true;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return values.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return values.retainAll(c);
	}

	@Override
	public void clear() {
		values.clear();
	}

	@Override
	public void add(int index, JSON<?> element) {
		values.add(index, element!=null ? element : new JsonNull());
	}

	@Override
	public int indexOf(Object o) {
		return values.indexOf(o!=null ? o : new JsonNull());
	}

	@Override
	public int lastIndexOf(Object o) {
		return values.lastIndexOf(o!=null ? o : new JsonNull());
	}

	@Override
	public ListIterator<JSON<?>> listIterator() {
		return values.listIterator();
	}

	@Override
	public ListIterator<JSON<?>> listIterator(int index) {
		return values.listIterator(index);
	}

	@Override
	public List<JSON<?>> subList(int fromIndex, int toIndex) {
		return values.subList(fromIndex, toIndex);
	}

	
	@Override
	protected JsonArray parseJSON(UTF8CharInputStream input, LineColumnTracker lct) throws JsonParseException, IOException {
		String c = skipIgnorers(input, lct);
		if(c==null){ throw new JsonParseUnfinishedException('[', lct); }
		if(!c.equals("[")){ throw new JsonParseException('[', c, lct); }
		lct.increaseColumn();
		
		c = skipIgnorers(input, lct);
		if(c==null){ throw new JsonParseUnfinishedException(lct); }
		if(c.equals("]")){ lct.increaseColumn(); this.values.clear(); return this; }
		
		input.insertReadAgainAtBeginning(c);
		
		boolean nextEntry = false;
		ArrayList<JSON<?>> list = new ArrayList<>();
		do {
			if(nextEntry) lct.increaseColumn(); // increment for ',' 
			
			// check if empty ','
			c = skipIgnorers(input, lct);
			if(c!=null && c.equals("]")) break;
			input.insertReadAgainAtBeginning(c);
			
			// read entry as JSON<?>
			list.add(parseAutoJSON(input, lct));
			
			c = skipIgnorers(input, lct);
		} while((nextEntry = (c!=null && c.equals(","))));
		
		if(c==null){ throw new JsonParseUnfinishedException(lct); }
		if(!c.equals("]")){ throw new JsonParseException(']', c, lct); }
		lct.increaseColumn();
		
		this.values = list;
		return this;
	}
	
	
	/** Tries to parse a given JSON string as a {@link JsonArray}
	 * @param json String that should be parsed
	 * @return Parsed JSON array
	 * @throws JsonParseException if JSON string could not be parsed correctly
	 * @throws NullPointerException if JSON string is null
	 */
	public static JsonArray parse(String json) throws JsonParseException, NullPointerException {
		return new JsonArray().parseJSON(json);
	}
	
	/** Tries to parse a given JSON string as a {@link JsonArray}
	 * @param json String that should be parsed
	 * @param offset Offset where to start reading the JSON string
	 * @return Parse JSON array
	 * @throws JsonParseException if JSON string could not be parsed correctly
	 * @throws NullPointerException if JSON string is null
	 */
	public static JsonArray parse(String json, int offset) throws JsonParseException, NullPointerException {
		return new JsonArray().parseJSON(json, offset);
	}
	
	/** Tries to parse a {@link JsonArray} from a given {@link InputStream} in UTF-8
	 * @param json Stream in UTF-8 that should be parsed
	 * @return Parse JSON array
	 * @throws JsonParseException if input could not be parsed correctly
	 * @throws IOException if an error while reading {@link InputStream} occurred
	 * @throws NullPointerException if input is null
	 */
	public static JsonArray parse(UTF8CharInputStream input) throws JsonParseException, IOException, NullPointerException {
		return new JsonArray().parseJSON(input);
	}
	
	/**
	 * Tries to parse a {@link JsonArray} from a given {@link File}
	 * @param file File the JSON data should be read from
	 * @return Parsed JSON array
	 * @throws JsonParseException if file could not be parsed correctly
	 * @throws NullPointerException if file is null
	 * @throws IOException if an error occurs while reading the {@link File}
	 */
	public static JsonArray parse(File file) throws JsonParseException, NullPointerException, IOException{
		return new JsonArray().parseJSON(file);
	}
	
	/**
	 * Tries to parse a {@link JsonArray} from a given {@link URL}
	 * @param url URL the JSON data should be read from
	 * @return Parsed JSON array
	 * @throws JsonParseException if {@link URL} could not be parsed correctly
	 * @throws NullPointerException if {@link URL} is null
	 * @throws IOException if an error occurs while reading the from the {@link URL}
	 */
	public static JsonArray parse(URL url) throws JsonParseException, NullPointerException, IOException{
		return new JsonArray().parseJSON(url);
	}
}

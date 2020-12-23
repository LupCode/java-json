package com.lupcode.JSON.utils;

/** Class is used for tracking line and column numbers while parsing
 * @author LupCode.com (Luca Vogels)
 * @since 2020-12-23
 */
public class LineColumnTracker {

	private long line=1, offset=0;
	private int column=0;
	
	public LineColumnTracker(){
		
	}
	
	public LineColumnTracker(long offset){
		this.offset = offset;
	}
	
	public LineColumnTracker(long line, int column, long offset){
		this.line = line;
		this.column = column;
		this.offset = offset;
	}
	
	/**
	 * Returns the current line beginning from one
	 * @return Line number
	 */
	public long getLine(){
		return line;
	}
	
	/**
	 * Increments the line counter and resets column counter back to zero
	 * @return Total offset
	 */
	public long increaseLine(){
		this.line++; this.column=0; this.offset++; return this.offset;
	}
	
	/**
	 * Returns the current column beginning from zero
	 * @return Column number
	 */
	public int getColumn(){
		return column;
	}
	
	/**
	 * Increases the column counter
	 * @return Total offset
	 */
	public long increaseColumn(){
		this.column++;
		this.offset++;
		return this.offset;
	}
	
	/**
	 * Increases the column counter 
	 * @param value How much the counter should be increased
	 * @return Total offset
	 */
	public long increaseColumn(int value){
		this.column += value;
		this.offset += value;
		return this.offset;
	}
	
	/**
	 * Returns the total character offset from the beginning
	 * @return Total offset
	 */
	public long getOffset(){
		return offset;
	}
	
	@Override
	public LineColumnTracker clone() {
		return new LineColumnTracker(line, column, offset);
	}
	
	public String toString(){
		return "line "+line+" column "+column+" (totalOffset="+offset+")";
	}
}

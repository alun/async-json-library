package org.async.json.in;

public class ParseException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int line = 0;
	private int symbol = 0;

	public ParseException(int line, int symbol, String message) {
		super(message + " at line " + line + " symbol " + symbol);
		this.line = line;
		this.symbol = symbol;
	}

	public ParseException(int line, int symbol, String message, Throwable cause) {
		super(message + " at line " + line + " symbol " + symbol, cause);
		this.line = line;
		this.symbol = symbol;
	}

	public ParseException(String message) {
		super(message);
	}

	public ParseException(String message, Throwable cause) {
		super(message, cause);
	}

	public int getLine() {
		return line;
	}

	public int getSymbol() {
		return symbol;
	}
}

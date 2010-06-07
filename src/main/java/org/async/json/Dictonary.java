package org.async.json;

import java.util.HashMap;
import java.util.Map;

public class Dictonary {
	public static final char NEW_LINE = '\n';
	public static final char BACKSLASH = '\\';
	public static final char E = 'e';
	public static final char E_UPPER = 'E';
	public static final char PLUS = '+';
	public static final char MINUS = '-';
	public static final char QUOTE = '"';
	public static final char SPACE = ' ';
	public static final char DOT = '.';
	public static final char COMMA = ',';
	public static final char ARRAY_END = ']';
	public static final char OBJECT_END = '}';
	public static final char COLON = ':';
	public static final char ARRAY_START = '[';
	public static final char OBJECT_START = '{';
	public static final char FALSE_START = 'f';
	public static final char TRUE_START = 't';
	public static final char NULL_START = 'n';
	public static final char UNICODE_START = 'u';
	public static final char LONG_TYPE = 'L';

	public static Map<Character, Character> SPECIAL_CHARS = new HashMap<Character, Character>();
	static {
		SPECIAL_CHARS.put('\'', '\'');
		SPECIAL_CHARS.put('"', '"');
		SPECIAL_CHARS.put('\\', '\\');
		SPECIAL_CHARS.put('/', '/');
		SPECIAL_CHARS.put('b', '\b');
		SPECIAL_CHARS.put('f', '\f');
		SPECIAL_CHARS.put('n', '\n');
		SPECIAL_CHARS.put('r', '\r');
		SPECIAL_CHARS.put('t', '\t');
	}
	
}

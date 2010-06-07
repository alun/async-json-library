package org.async.json.out;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;
import java.util.Stack;

import org.async.json.Dictonary;
import org.async.json.JSONArray;
import org.async.json.JSONObject;

public class JSONWriter {
	protected Writer writer;
	protected Stack<Character> stack = new Stack<Character>();
	protected char tail;

	protected static final char STACK_DATA_OBJECT = 'o';
	protected static final char STACK_DATA_ARRAY = 'a';

	public JSONWriter(Writer writer) {
		super();
		this.writer = writer;
	}

	protected void write(String s) throws IOException {
		writer.write(s);
		if (s.length() > 0) {
			tail = s.charAt(s.length() - 1);
		}
	}

	protected void write(int c) throws IOException {
		writer.write(c);
		tail = (char) c;
	}

	public void openObject(String name) throws IOException {

		if ((stack.size() != 0) && stack.lastElement() != STACK_DATA_ARRAY) {
			writeFieldName(name);
		}
		if ((stack.size() != 0) && stack.lastElement() == STACK_DATA_ARRAY) {
			writeComa();
		}
		write(Dictonary.OBJECT_START);
		stack.push(STACK_DATA_OBJECT);
	}

	public void closeObject() throws IOException {
		write(Dictonary.OBJECT_END);

		if (stack.isEmpty() || !(stack.pop()).equals(STACK_DATA_OBJECT)) {
			throw new IllegalStateException("");
		}
	}

	public void openArray(String name) throws IOException {

		if ((stack.size() != 0) && stack.lastElement() != STACK_DATA_ARRAY) {
			writeFieldName(name);
		}
		if (tail != Dictonary.COLON && stack.size() != 0) {
			writeComa();
		}
		write(Dictonary.ARRAY_START);
		stack.push(STACK_DATA_ARRAY);
	}

	public void closeArray() throws IOException {
		write(Dictonary.ARRAY_END);
		if (stack.isEmpty() || !(stack.pop()).equals(STACK_DATA_ARRAY)) {
			throw new IllegalStateException("");
		}
	}

	private void writeString(String s) throws IOException {
		if (s == null) {
			write(_w("null"));
		} else {
			write(Dictonary.QUOTE);
			write(_w(s));
			write(Dictonary.QUOTE);
		}
	}

	protected String _w(String s) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if ((c >= 0x0001) && (c <= 0x007F)) {
				if (c == Dictonary.QUOTE || c == Dictonary.BACKSLASH) {
					sb.append(Dictonary.BACKSLASH);
				}
				sb.append(c);
			} else {
				sb.append("\\u");
				sb.append(String.format("%04x", (int) c));
			}
		}
		return sb.toString();
	}

	private void writeNumber(Number n) throws IOException {
		if (n == null) {
			writeString(null);
		} else {
			write(n.toString());
		}

	}

	private void writeBoolean(Boolean b) throws IOException {
		if (b == null) {
			writeString(null);
		} else {
			write(b.toString());
		}
	}

	public void writeField(String name, Number value) throws IOException {
		if (stackIsEmpty()) {
			throw new IllegalStateException("");
		} else {
			writeFieldName(name);
			writeNumber(value);
		}
	}

	public void writeField(String name, Boolean value) throws IOException {
		if (stackIsEmpty()) {
			throw new IllegalStateException("");
		} else {
			writeFieldName(name);
			writeBoolean(value);
		}
	}

	public void writeField(String name, String value) throws IOException {
		if (stackIsEmpty()) {
			throw new IllegalStateException("");
		} else {
			writeFieldName(name);
			writeString(value);

		}
	}

	protected boolean stackIsEmpty() {
		return stack.empty();
	}

	@SuppressWarnings("unchecked")
	public void writeObject(String name, Map map) throws IOException {
		if (map.size() > 0) {

			openObject(name);
			for (Object o : map.keySet()) {
				String key = o.toString();
				Object v = map.get(key);
				if (v instanceof Map) {
					writeObject(key, (Map) v);
				} else if (v instanceof Collection) {
					writeArray(key, (Collection) v);
				} else if (v instanceof Number) {
					writeField(key, (Number) v);
				} else if (v instanceof Boolean) {
					writeField(key, (Boolean) v);
				} else {
					writeField(key, (String) v);
				}
			}
			closeObject();
		}
	}

	@SuppressWarnings("unchecked")
	public void writeArray(String name, Collection collection)
			throws IOException {
		if (collection.size() > 0) {
			openArray(name);
			for (Object object : collection) {

				if (object instanceof Collection) {
					writeArray(name, (Collection) object);
				} else if (object instanceof Map) {
					writeObject(name, (Map) object);
				} else if (object instanceof Number) {
					writeArrayItem((Number) object);
				} else if (object instanceof Boolean) {
					writeArrayItem((Boolean) object);
				} else {
					writeArrayItem((String) object);
				}
			}
			closeArray();
		}
	}

	public void writeObject(String name, JSONObject obj) throws IOException {

		if (obj.keySet().size() > 0) {
			openObject(name);
			for (Object o : obj.keySet()) {
				String key = o.toString();
				Object v = obj.get(key);
				if (v instanceof JSONObject) {
					writeObject(key, (JSONObject) v);
				} else if (v instanceof JSONArray) {
					writeArray(key, (JSONArray<?>) v);
				} else if (v instanceof Number) {
					writeField(key, (Number) v);
				} else if (v instanceof Boolean) {
					writeField(key, (Boolean) v);
				} else {
					writeField(key, (String) v);
				}
			}
			closeObject();
		}
	}

	public void writeArray(String name, JSONArray<?> arr) throws IOException {
		if (arr.size() > 0) {
			openArray(name);
			for (Object object : arr) {
				if (object instanceof JSONArray) {
					writeArray(name, (JSONArray<?>) object);
				} else if (object instanceof JSONObject) {
					writeObject(name, (JSONObject) object);
				} else if (object instanceof Number) {
					writeArrayItem((Number) object);
				} else if (object instanceof Boolean) {
					writeArrayItem((Boolean) object);
				} else {
					writeArrayItem((String) object);
				}
			}
			closeArray();
		}
	}

	public void writeField(String name) throws IOException {
		if (stackIsEmpty()) {
			throw new IllegalStateException("");
		} else {
			writeFieldName(name);
			writeString(null);
		}
	}

	protected void writeFieldName(String fname) throws IOException {
		if (tail == Dictonary.COLON) {
			throw new IllegalStateException("");
		}
		writeComa();
		writeString(fname);
		write(Dictonary.COLON);
	}

	protected void writeComa() throws IOException {

		if (tail != Dictonary.OBJECT_START && tail != Dictonary.ARRAY_START) {
			write(Dictonary.COMMA);
		}
	}

	protected void writeArrayItem(String s) throws IOException {
		writeComa();
		writeString(s);
	}

	protected void writeArrayItem(Number n) throws IOException {
		writeComa();
		writeNumber(n);
	}

	protected void writeArrayItem(Boolean b) throws IOException {
		writeComa();
		writeBoolean(b);
	}

	public void writeArray(String... args) throws IOException {
		for (int i = 0; i < args.length; i++) {
			writeArrayItem(args[i]);
		}
	}

	public void writeArray(Number... args) throws IOException {
		for (int i = 0; i < args.length; i++) {
			writeArrayItem(args[i]);
		}
	}

	public void writeArray(Boolean... args) throws IOException {
		for (int i = 0; i < args.length; i++) {
			writeArrayItem(args[i]);
		}
	}

	public Writer getWriter() {
		return writer;
	}

	public void flush() throws IOException {
		writer.flush();
	}

}

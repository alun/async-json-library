package org.async.json.jpath;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

import org.async.json.Dictonary;

public class JPathReader extends Reader {
	private Reader reader;
	private CharArrayWriter buffer = new CharArrayWriter();
	int pos = 0;
	protected static Set<Character> booleanChars = new HashSet<Character>();
	{
		booleanChars.add('t');
		booleanChars.add('r');
		booleanChars.add('u');
		booleanChars.add('e');
		booleanChars.add('f');
		booleanChars.add('a');
		booleanChars.add('l');
		booleanChars.add('s');
		booleanChars.add('n');

	}
	protected static Set<Character> numberChars = new HashSet<Character>();
	{
		for (char i = '0'; i <= '9'; i++) {
			numberChars.add(i);
		}
		numberChars.add('.');
		numberChars.add('e');
		numberChars.add('E');
		numberChars.add('+');
		numberChars.add('-');
	}

	protected static Set<Character> stopChars = new HashSet<Character>();
	{
		stopChars.add('.');
		stopChars.add('[');
		stopChars.add('?');
	}

	protected static Set<Character> jpathStopChars = new HashSet<Character>();
	{
		jpathStopChars.add(' ');
		jpathStopChars.add(')');
	}

	protected static Set<Character> operatorsStopChars = new HashSet<Character>();
	{
		operatorsStopChars.add(' ');
		operatorsStopChars.add(')');
	}

	public JPathReader(Reader reader) {
		super();
		this.reader = reader;
	}

	public Number readNumber(char[] tail) throws IOException {
		String v = readAllowedChars(tail, numberChars);
		if (v.length() == 0) {
			return null;
		} else {
			return v.indexOf('.') > -1 ? Double.parseDouble(v) : Integer
					.parseInt(v);
		}
	}

	private String readAllowedChars(char[] tail, Set<Character> chars)
			throws IOException {
		do {
			if (chars.contains(tail[0])) {
				pos++;
				buffer.write(tail);
			} else {
				break;
			}
		} while (reader.read(tail) > 0);
		String v = buffer.toString();
		buffer.reset();
		return v;
	}

	public String readJPath(char[] tail) throws IOException {
		return readUntil(tail, jpathStopChars);
	}

	private String readUntil(char[] tail, Set<Character> stop)
			throws IOException {
		boolean backslashed = false;
		int i = 0;
		do {
			if (tail[0]>0&&!stop.contains(tail[0]) || backslashed) {
				pos++;
				if (!backslashed && tail[0] == '\\') {
					backslashed = true;
				} else {
					buffer.write(tail);
					backslashed = false;

				}
			} else {
				break;
			}
		} while ((i = reader.read(tail)) > 0);
		if (i < 0)
			tail[0] = 0;
		String v = buffer.toString();
		buffer.reset();
		return v;
	}

	public String readName(char[] tail) throws IOException {
		return readUntil(tail, stopChars);
	}

	public String readString(char[] tail) throws IOException {
		boolean backslashed = false;
		while (reader.read(tail) > 0 && (tail[0] != '"' || backslashed)) {
			pos++;
			if (!backslashed && tail[0] == '\\') {
				backslashed = true;
			} else {
				buffer.write(tail);
				backslashed = false;

			}
		}
		read(tail);
		String v = buffer.toString();
		buffer.reset();
		return v;
	}

	public String readOperator(char[] tail) throws IOException {
		return readUntil(tail, operatorsStopChars);
	}

	@Override
	public void close() throws IOException {
		reader.close();

	}

	@Override
	public int read(char[] tail) throws IOException {
		int i = 0;
		while ((i = reader.read(tail)) != -1 && tail[0] <= Dictonary.SPACE) {
			pos++;
		}
		if (i < 0)
			tail[0] = 0;
		return i;
	}

	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		return reader.read(cbuf, off, len);
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public Boolean readBoolean(char[] tail) throws IOException {
		String v = readAllowedChars(tail, booleanChars);
		if(v.equals("null")) return null;
		else return Boolean.parseBoolean(v);
	}

}

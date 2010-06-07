package org.async.json.in;

import java.io.IOException;
import java.io.Reader;

import org.async.json.Dictonary;

public class JSONReader extends Reader {
	protected Reader reader;
	protected int line = 1;
	protected int symbol = 1;

	public JSONReader(Reader reader) {
		super();
		this.reader = reader;
	}

	@Override
	public void close() throws IOException {
		reader.close();

	}

	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		return reader.read(cbuf, off, len);
	}

	@Override
	public int read(char[] tail) throws IOException {
		int i = 0;
		while ((i = reader.read(tail)) != -1 && tail[0] <= Dictonary.SPACE) {
			if (tail[0] == Dictonary.NEW_LINE) {
				line++;
				symbol = 1;
			} else if(tail[0]=='\t') {
				symbol+=4;
			} else {

				symbol++;
			}
		}
		if (i > -1) {
			symbol++;
		}
		return i;
	}

	public int readString(char[] tail) throws IOException {
		int i = reader.read(tail);
		if (i > -1) {
			if (tail[0] == Dictonary.NEW_LINE) {
				line++;
				symbol = 1;
			} else {
				symbol++;
			}
		}
		return i;
	}

	public int getLine() {
		return line;
	}

	public int getSymbol() {
		return symbol;
	}

	public Reader getReader() {
		return reader;
	}

	public void setReader(Reader reader) {
		this.reader = reader;
	}
	
	
}

package org.async.json.in.ex;

import java.io.IOException;
import java.io.Reader;

import org.async.json.Dictonary;
import org.async.json.in.JSONReader;

public class XJSONReader extends JSONReader {

	private static final char STAR = '*';
	private static final char SLASH = '/';

	public XJSONReader(Reader reader) {
		super(reader);
	}

	@Override
	public int read(char[] tail) throws IOException {
		int i = super.read(tail);
		if (tail[0] == SLASH) {
			readString(tail);
			if (tail[0] == SLASH) {
				while ((i = readString(tail)) != -1
						&& tail[0] != Dictonary.NEW_LINE);
				i = read(tail);
			} else if (tail[0] == STAR) {
				do {
					while ((i = readString(tail)) != -1 && tail[0] != STAR)
						;
				} while ((i = readString(tail)) != -1 && tail[0] != SLASH);
				i = read(tail);
			}
		}
		return i;
	}
}

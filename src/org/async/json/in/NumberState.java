package org.async.json.in;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class NumberState implements State {
	private StringBuilder builder = new StringBuilder();
	protected Set<Character> allowedChars = new HashSet<Character>();
	{
		for (char i = '0'; i <= '9'; i++) {
			allowedChars.add(i);
		}
		allowedChars.add('.');
		allowedChars.add('e');
		allowedChars.add('E');
		allowedChars.add('+');
		allowedChars.add('-');
	}

	@Override
	public int run(JSONReader reader, char[] tail, int state, Callback callback)
			throws IOException, ParseException {
		do {
			if (allowedChars.contains(tail[0])) {
				builder.append(tail[0]);
			} else {
				String string = builder.toString();
				builder.setLength(0);
				try {
					Number result = parseNumber(string);
					callback.number(result);
				} catch (Exception e) {
					throw new ParseException(reader.getLine(), reader
							.getSymbol(), "Unmatched input '" + string + "'");

				}
				return RootParser.GO_TO_PARENT_STATE;
			}
		} while (reader.read(tail) > -1);
		return RootParser.RESUME;
	}

	protected Number parseNumber(String s) {
		if ((s.indexOf('.') > -1) || (s.indexOf('e') > -1)
				|| (s.indexOf('E') > -1))
			return Double.parseDouble(s);
		else
			return Integer.parseInt(s);
	}

	@Override
	public boolean isReadNext() {
		return false;
	}

	@Override
	public int getState() {
		return RootParser.COMPLETED;
	}

}

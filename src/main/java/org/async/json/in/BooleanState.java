package org.async.json.in;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


public class BooleanState implements State {
	private static final String NULL = "null";
	private StringBuilder builder = new StringBuilder(6);
	// TODO reset builders when throw exceptions
	protected Set<Character> allowedChars = new HashSet<Character>();
	{
		allowedChars.add('t');
		allowedChars.add('r');
		allowedChars.add('u');
		allowedChars.add('e');
		allowedChars.add('f');
		allowedChars.add('a');
		allowedChars.add('l');
		allowedChars.add('s');
		allowedChars.add('n');

	}

	@Override
	public int run(JSONReader reader, char[] tail, int state, Callback callback)
			throws IOException, ParseException {
		do {
			if (allowedChars.contains(tail[0])) {
				builder.append(tail[0]);
				if (builder.length() > 5) {
					throw new ParseException(reader.getLine(), reader
							.getSymbol(), "Unmatched input '"
							+ builder.toString() + "'");
				}
			} else {
				String str = builder.toString();
				builder.setLength(0);
				if (str.equals(NULL)) {
					callback.nil();
				} else {
					if (str.equals("true"))
						callback.bool(true);
					else if (str.equals("false"))
						callback.bool(false);
					else
						throw new ParseException(reader.getLine(), reader
								.getSymbol(), "Unmatched input '"
								+ str + "'");
				}
				return RootParser.GO_TO_PARENT_STATE;
			}
		} while (reader.read(tail) > -1);
		return RootParser.RESUME;
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

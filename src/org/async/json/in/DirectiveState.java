package org.async.json.in;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


public class DirectiveState implements State {
	private static final char DIRECTIVE_ARGUMENT_AFTER = ')';
	private static final char DIRECTIVE_ARGUMENT_BEFORE = '(';
	private StringBuilder directive = new StringBuilder();
	private StringBuilder directive_argument = new StringBuilder();
	private int state;
	private static int NAME = 0;
	private static int ARGUMENT = 1;
	private static int END = 2;

	protected Set<String> directives = new HashSet<String>();
	{
		directives.add("include");
	}

	@Override
	public int getState() {
		return state;
	}

	@Override
	public boolean isReadNext() {
		return false;
	}

	@Override
	public int run(JSONReader reader, char[] tail, int st, Callback callback)
			throws IOException, ParseException {
		this.state = st;

		do {
			if (tail[0] != DIRECTIVE_ARGUMENT_BEFORE && state == NAME) {
				directive.append(tail[0]);
			} else if (tail[0] == DIRECTIVE_ARGUMENT_BEFORE) {
				state = ARGUMENT;
			} else if (tail[0] != DIRECTIVE_ARGUMENT_AFTER && state == ARGUMENT) {
				directive_argument.append(tail[0]);
			} else if (tail[0] == DIRECTIVE_ARGUMENT_AFTER) {
				state = END;
			} else {
				throw new ParseException(reader.getLine(), reader.getSymbol(),
						"Unmatched input '" + tail[0] + "'");
			}

		} while (reader.read(tail) > -1);
		directive = new StringBuilder(directive.toString().substring(1));
		if (!directives.contains(directive.toString())) {
			throw new ParseException(reader.getLine(), reader.getSymbol(),
					"Unmatched input, unsupported directive '" + directive.toString() + "'");
		}
		return RootParser.RESUME;
	}

}

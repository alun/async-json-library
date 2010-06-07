package org.async.json.in;

import java.io.IOException;


public class ObjectState implements State {
	private static int NEXT = 1;
	private static int NAME = 0;
	private static int NOT_SET = 2;
	private int state;

	@Override
	public int run(JSONReader reader, char[] tail, int st, Callback callback)
			throws IOException, ParseException {
		this.state=st;
		do {
			if (tail[0] == '{'&&state!=NEXT) {
				callback.startObject();
				state = NAME;
			} else if (tail[0] == '}') {
				callback.endObject();
				state = RootParser.COMPLETED;
				return RootParser.GO_TO_PARENT_STATE;
			} else if (tail[0] == ',' && state != NAME) {
				state = NAME;
			} else if (tail[0] == ':' && state != NEXT) {
				state = NEXT;
			} else if (state == NAME) {
				state = NOT_SET;
				return RootParser.STRING_STATE;
			} else if (state == NEXT) {
				state = NOT_SET;
				return RootParser.VALUE_STATE;
			} else {
				throw new ParseException(reader.getLine(), reader.getSymbol(),
						"Unmatched input '" + tail[0] + "'");
			}
		} while (reader.read(tail) > -1);
		return RootParser.RESUME;
	}


	@Override
	public boolean isReadNext() {
		return true;
	}


	@Override
	public int getState() {
		return state;
	}



}

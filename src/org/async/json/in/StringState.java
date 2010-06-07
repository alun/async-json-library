package org.async.json.in;

import java.io.IOException;


public class StringState implements State {
	protected StringBuilder builder = new StringBuilder(128);
	protected StringBuilder unicode = new StringBuilder(4);
	protected int state;
	protected int NOT_SET = 0;
	protected int INSIDE = 3;
	protected int BACKSLASH = 1;
	protected int UNICODE = 2;

	@Override
	public int run(JSONReader reader, char[] tail, int st, Callback callback)
			throws IOException, ParseException {
		this.state = st;
		do {
			if(state==NOT_SET&&state==NOT_SET) {
				state=INSIDE;
			} else if (state == BACKSLASH) {
				if (tail[0] == 'u') {
					state = UNICODE;
				} else {
					builder.append(tail[0]);
					state=INSIDE;
				}
			} else if (state == UNICODE) {
				if ((tail[0] >= '0' && tail[0] <= '9')
						|| (tail[0] >= 'a' && tail[0] <= 'f')
						|| (tail[0] >= 'A' && tail[0] <= 'F')) {
					unicode.append(tail[0]);
				}
				if (unicode.length() == 4) {
					builder.append((char) Integer.parseInt(unicode.toString(),
							16));
					unicode.setLength(0);
					state = INSIDE;
				}//
			} else if (tail[0] == '"'&&state==INSIDE) {
				return finish(tail,callback);
			} else if (tail[0] == '\\') {
				state = BACKSLASH;
			} else {
				builder.append(tail[0]);
			}
		}while (reader.readString(tail) > 0);
		return -1;
	}

	protected int finish(char[] tail,Callback callback) throws ParseException{
		callback.string(builder);
		builder.setLength(0);
		return RootParser.GO_TO_PARENT_STATE;
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

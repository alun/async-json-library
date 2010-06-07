package org.async.json.in;

import java.io.IOException;


public class ReadNextState implements State {

	@Override
	public boolean isReadNext() {
		return false;
	}

	@Override
	public int run(JSONReader reader, char[] tail, int state, Callback callback)
			throws IOException {
		if (reader.read(tail) > -1) {
			return RootParser.GO_TO_PARENT_STATE;
		}
		return -1;
	}

	@Override
	public int getState() {
		return RootParser.COMPLETED;
	}

}

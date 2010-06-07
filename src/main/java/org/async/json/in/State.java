package org.async.json.in;

import java.io.IOException;


public interface State {
	int run(JSONReader reader, char[] tail, int state, Callback callback) throws IOException,ParseException;

	boolean isReadNext();

	int getState();

}

package org.async.json.in;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;




public class ValueState implements State {
	protected Map<Character, Integer> forward = new HashMap<Character, Integer>();
	{
		forward.put('"', RootParser.STRING_STATE);
		forward.put('-', RootParser.NUMBER_STATE);
		forward.put('+', RootParser.NUMBER_STATE);
		for (char i = '0'; i <= '9'; i++) {
			forward.put(i, RootParser.NUMBER_STATE);
		}
		forward.put('[',RootParser.ARRAY_STATE);
		forward.put('{',RootParser.OBJECT_STATE);
		forward.put('n',RootParser.BOOLEAN_STATE);
		forward.put('t',RootParser.BOOLEAN_STATE);
		forward.put('f',RootParser.BOOLEAN_STATE);
	}

	@Override
	public int run(JSONReader reader, char[] tail, int state, Callback callback) throws IOException, ParseException {
		Integer i = forward.get(tail[0]);
		if (i == null) {
			throw new ParseException(reader.getLine(),reader.getSymbol(),"Unmatched input '"+tail[0]+"'");
		}
		return i;
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

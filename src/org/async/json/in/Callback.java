package org.async.json.in;


public interface Callback {
	void startObject();

	void endObject();

	void startArray();

	void endArray();

	void string(StringBuilder string);

	void bool(Boolean bool);

	void nil();

	void number(Number number);
	
	void unknownError(Exception e);
	
	void parseError(ParseException e);
}

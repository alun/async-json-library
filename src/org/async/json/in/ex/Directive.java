package org.async.json.in.ex;

import org.async.json.in.ParseException;

public interface Directive {
	void run(String arg) throws ParseException;
}

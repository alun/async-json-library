package org.async.json.validation;

import java.io.Reader;

import org.async.json.in.JSONParser;
import org.async.json.in.RootParser;

public class JSONPatternParser extends JSONParser {

	public JSONPatternParser(RootParser parser) {
		this.parser = parser;
		this.callback = new JSONPatternBuildeCallback();
	}
	
	public JSONPatternParser() {
		this(new RootParser());
	}
	
	public JSONPattern parsePattern(Reader reader){
		return (JSONPattern) parseObject(reader);
	}
	
	
}

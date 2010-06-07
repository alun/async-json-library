package org.async.json.in;

import java.io.Reader;

import org.async.json.JSONArray;
import org.async.json.JSONObject;

public class JSONParser {
	protected RootParser parser;
	protected ObjectBuilderCallback callback;

	public JSONParser(RootParser parser) {
		super();
		this.parser = parser;
		this.callback = new ObjectBuilderCallback();
	}

	public JSONParser() {
		this(new RootParser());
	}

	public String parseString(Reader reader) {
		return (String) parseObject(reader);
	}

	public Number parseNumber(Reader reader) {
		return (Number) parseObject(reader);
	}

	public JSONObject parse(Reader reader)  {
		return (JSONObject) parseObject(reader);
	}

	public JSONArray<?> parseArray(Reader reader)  {
		return (JSONArray<?>) parseObject(reader);
	}

	protected Object parseObject(Reader reader)  {
		JSONReader jsonReader=reader instanceof JSONReader?(JSONReader)reader:new JSONReader(reader);
		parser.parse(jsonReader, callback);
		return callback.getResult();
	}

	public RootParser getParser() {
		return parser;
	}

	public void setParser(RootParser parser) {
		this.parser = parser;
	}







}

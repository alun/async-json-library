package org.async.json.in.ex;

import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

import org.async.json.in.JSONParser;
import org.async.json.in.JSONReader;
import org.async.json.in.ObjectBuilderCallback;
import org.async.json.in.RootParser;

public class XJSONParser extends JSONParser {

	protected List<JSONReader> readerStack = new LinkedList<JSONReader>();
	protected RootParser parser = new RootParser();
	protected ObjectBuilderCallback callback;
	protected IncludeDirective includeDirective = new IncludeDirective(
			readerStack);

	public XJSONParser() {
		this(new ObjectBuilderCallback());
	}

	public XJSONParser(ObjectBuilderCallback callback) {
		super();
		XStringState stringState = new XStringState();
		stringState.getDirectives().put("include", includeDirective);
		parser.getStates()[RootParser.STRING_STATE] = stringState;
		this.callback=callback;
	}

	@Override
	protected Object parseObject(Reader reader) {
		XJSONReader jsonReader = reader instanceof XJSONReader ? (XJSONReader) reader
				: new XJSONReader(reader);
		readerStack.add(jsonReader);
		while (!readerStack.isEmpty()) {
			int size = readerStack.size();
			boolean result = parser.parse(readerStack.get(size - 1), callback);
			if (result) {
				return callback.getResult();
			} else {
				if (size == readerStack.size()) {
					readerStack.remove(size - 1);
				} else {
					parser.getStack().set(parser.getStack().size() - 1,
							RootParser.VALUE_STATE);
				}
			}
		}
		return null;
	}

	public IncludeDirective getIncludeDirective() {
		return includeDirective;
	}
}

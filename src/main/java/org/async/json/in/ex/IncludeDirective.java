package org.async.json.in.ex;

import java.io.FileReader;
import java.util.List;

import org.async.json.in.JSONReader;
import org.async.json.in.ParseException;

public class IncludeDirective implements Directive {
	private String root = ".";
	protected List<JSONReader> readerStack;

	public IncludeDirective(List<JSONReader> readerStack) {
		super();
		this.readerStack = readerStack;
	}

	@Override
	public void run(String arg) throws ParseException {
		try {
			JSONReader reader = new XJSONReader(
					new FileReader(root + '/' + arg));
			readerStack.add(reader);
		} catch (Exception e) {
			throw new ParseException("Error processing directive: ", e);
		}

	}

	public void setRoot(String rootPath) {
		this.root = rootPath;
	}
}

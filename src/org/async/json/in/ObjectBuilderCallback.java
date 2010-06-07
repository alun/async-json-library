package org.async.json.in;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.async.json.JSONArray;
import org.async.json.JSONObject;
import org.async.json.Utils;


@SuppressWarnings("unchecked")
public class ObjectBuilderCallback implements Callback {
	private static final Logger logger=Logger.getLogger("org.async.json.in.ObjectBuilderCallback");
	protected LinkedList stack = new LinkedList();
	private Object result = null;

	public ObjectBuilderCallback() {
		super();
	}

	@Override
	public void bool(Boolean bool) {
		build(bool);
	}

	private boolean build(Object obj) {
		if (stack.isEmpty()){
			result = obj;
		}else{
			Object current = stack.getFirst();
			if (current instanceof JSONArray) {
				Utils.add((JSONArray<Object>) current,obj);
			} else if (current instanceof String) {
				String fname = (String) stack.removeFirst();
				Utils.put((JSONObject) stack.getFirst(),fname, obj);
			} else {
				return false;
			}
		}
		return true;
	}

	@Override
	public void endArray() {
		build((JSONArray<Object>) stack.removeFirst());
	}

	@Override
	public void endObject() {
		JSONObject obj = (JSONObject) stack.removeFirst();
		build(obj);
	}

	@Override
	public void nil() {
		number(null);
	}

	@Override
	public void number(Number number) {
		build(number);
	}

	@Override
	public void startArray() {
		stack.addFirst(new JSONArray<Object>());
	}

	@Override
	public void startObject() {
		stack.addFirst(new JSONObject());
	}

	@Override
	public void string(StringBuilder string) {
		if (!build(string.toString()) && stack.getFirst() instanceof JSONObject) {
			stack.addFirst(string.toString());
		}
	}

	public Object getResult() {
		return result;
	}

	@Override
	public void parseError(ParseException e) {
		if(logger.isLoggable(Level.SEVERE)) {
			logger.log(Level.SEVERE,e.getMessage(),e);
		}
	}

	@Override
	public void unknownError(Exception e) {
		if(logger.isLoggable(Level.SEVERE)) {
			logger.log(Level.SEVERE,e.getMessage(),e);
		}
	}
}

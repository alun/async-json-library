package org.async.json.jpath.points;

import org.async.json.JSONObject;
import org.async.json.jpath.Iterable;
import org.async.json.jpath.JPathCondition;
import org.async.json.jpath.JPathPoint;

public class ObjectPoint extends JPathPoint {
	private String key;

	public ObjectPoint(String key) {
		super();
		this.key = key;
	}

	public ObjectPoint(String key, JPathCondition condition) {
		super(condition);
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean matches(Iterable<Object, Object> current,
			Iterable<Object, Object> root) {
		//TODO .length for any object
		if (((Iterable) current) instanceof JSONObject) {
			return ((key!=null)||((JSONObject) ((Iterable) current)).contains(key))&&super.matches(current, root);
		}
//		else if(key.equals("_keys")||key.equals("_length")) {
//			return true;
//		}
		return false;
	}

	@Override
	public String toString() {

		return "." + key;
	}

}

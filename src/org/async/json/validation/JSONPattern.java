package org.async.json.validation;

import java.util.Iterator;

import org.async.json.JSONArray;
import org.async.json.JSONObject;

public class JSONPattern extends JSONObject {

	public boolean matches(JSONObject obj) {
		if (obj.keySet().size() != keySet().size()) {
			return false;
		}
		for (String key : keySet()) {
			Object o = obj.get(key);
			Object p = get(key);
			if (o == null || p == null) {
				return false;
			}
			Class<?> oType = o.getClass();
			Class<?> pType = p.getClass();
			if (!oType.isAssignableFrom(pType)) {
				return false;
			}
			if (pType.equals(JSONPattern.class)) {
				if (!((JSONPattern) p).matches(obj.getObject(key))) {
					return false;
				}
			}
			if (pType.equals(JSONArray.class)) {
				if (!matchesArray((JSONArray<?>) o, (JSONArray<?>) p)) {
					return false;
				}
			}

		}
		return true;
	}

	private boolean matchesArray(JSONArray<?> o, JSONArray<?> p) {

		JSONArray<?> oArr = ((JSONArray<?>) o);
		JSONArray<?> pArr = ((JSONArray<?>) p);
		Iterator<?> pIterator = pArr.iterator();
		Iterator<?> oIterator = oArr.iterator();
		if (pIterator.hasNext()) {
			Object pElem = pIterator.next();
			if (!oIterator.hasNext()) {
				return false;
			}
			while (oIterator.hasNext()) {
				Object oElem = oIterator.next();
				if (!oElem.getClass().isAssignableFrom(pElem.getClass())) {
					return false;
				} else {
					if (pElem.getClass().equals(JSONArray.class)) {
						if (!matchesArray((JSONArray<?>) oElem,
								(JSONArray<?>) pElem)) {
							return false;
						}
					} else if (pElem.getClass().equals(JSONPattern.class)) {
						if (!((JSONPattern) pElem).matches((JSONObject) oElem)) {
							return false;
						}
					}
				}
			}
			return true;
		} else {
			return true;
		}
	}
}

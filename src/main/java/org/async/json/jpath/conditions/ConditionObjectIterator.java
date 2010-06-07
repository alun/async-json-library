package org.async.json.jpath.conditions;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.async.json.JSONEntry;
import org.async.json.JSONObject;
import org.async.json.ObjectIterator;
import org.async.json.Utils;
import org.async.json.jpath.Iterable;
import org.async.json.jpath.JPathPoint;

public class ConditionObjectIterator extends ObjectIterator {
	private List<String> matches;
	private Iterator<String> it;
	private String k;

	@SuppressWarnings("unchecked")
	public ConditionObjectIterator(JSONObject object, JPathPoint point,
			Iterable<Object, Object> root) {
		super(object);
		if (key != null) {
			Object child = object.get(key);
			if (!point.getCondition().matches((Iterable) child, root)) {
				e = (new JSONEntry<String, Object>(key, child));
			}
		} else {
			matches = new LinkedList<String>();
			for (String key : object.keySet()) {
				if (point.getCondition().matches((Iterable) object.get(key),
						root)) {
					matches.add(key);
				}
			}
			it = matches.iterator();
		}

	}

	@Override
	public boolean hasNext() {
		if (key != null)
			return super.hasNext();
		return it.hasNext();
	}

	@Override
	public void remove() {
		if (key != null) {
			Utils.remove(object, key);
		} else {
			Utils.remove(object, k);
		}
	}

	@Override
	public Entry<String, Object> next() {
		if (key != null)
			return super.next();
		k = it.next();
		return new JSONEntry<String, Object>(k, Utils.get(object, k));
	}
}

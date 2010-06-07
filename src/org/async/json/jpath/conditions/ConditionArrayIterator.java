package org.async.json.jpath.conditions;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.async.json.ArrayIterator;
import org.async.json.JSONArray;
import org.async.json.Utils;
import org.async.json.jpath.Iterable;
import org.async.json.jpath.JPathPoint;

public class ConditionArrayIterator extends ArrayIterator {
	private List<Integer> matches = new LinkedList<Integer>();
	private Iterator<Integer> it;

	@SuppressWarnings("unchecked")
	public ConditionArrayIterator(JSONArray<?> array, JPathPoint point,
			Iterable<Object, Object> root) {
		super(array, point,root);
		for (int i = idx+step; step>0?i <= to:i>=to; i += step) {
			Object o = Utils.get(array, i);
			if (o instanceof Iterable) {
				if (point.getCondition().matches((Iterable<Object, Object>) o,
						root)) {
					matches.add(i);
				}

			}
		}
		it=matches.iterator();
	}

	@Override
	public boolean hasNext() {
		return it.hasNext();
	}

	@Override
	public Map.Entry<Integer, Object> next() {
		idx = it.next();
		return entry();
	}

	@Override
	public void remove() {
		super.remove();
	}

}

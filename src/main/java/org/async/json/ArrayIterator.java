package org.async.json;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.async.json.jpath.Iterable;
import org.async.json.jpath.JPath;
import org.async.json.jpath.JPathPoint;
import org.async.json.jpath.conditions.ConditionArrayIterator;
import org.async.json.jpath.points.ArrayPoint;

public class ArrayIterator implements Iterator<Map.Entry<Integer, Object>> {
	protected int idx;
	protected Object current;
	protected JSONArray<?> array;
	protected Integer to;
	protected Integer step;

	protected ArrayIterator(JSONArray<?> array, JPathPoint point,
			Iterable<Object, Object> root) {
		super();
		this.array = array;
		ArrayPoint ap = (point instanceof ArrayPoint) ? (ArrayPoint) point
				: null;
		if (ap != null) {
			step = i(getValue(array, root, ap.getStep()), 1);
			idx = i(getValue(array, root, ap.getI()), -step);
			to = i(getValue(array, root, ap.getTo()), ap.getI()==null?array.size()-1:idx+step);
		} else {
			to = array.size()-1;
			step = 1;
			idx = -step;

		}
		if(step==0) throw new IllegalArgumentException();
		if(to-idx/step<0) throw new IllegalArgumentException();
	}

	private Object getValue(JSONArray<?> array, Iterable<Object, Object> root,
			Object v) {
		if (v != null && v instanceof JPath) {
			JPath path = ((JPath) v);
			Iterator<Entry<Object, Object>> it = path.isRoot() ? root
					.iterator(path) : array.iterator(path);
			if (it.hasNext()) {
				v = it.next().getValue();
			}
		}
		return v;
	}

	private Integer i(Object v, Integer def) {
		if (v instanceof Integer) {
			return n(v == null ? def : (Integer) v);
		} else {
			return def;
		}
	}

	public static ArrayIterator getIterator(JSONArray<?> array,
			JPathPoint point, Iterable<Object, Object> root) {
		return point == null || point.getCondition() == null ? new ArrayIterator(
				array, point, root)
				: new ConditionArrayIterator(array, point, root);
	}

	private int n(int i) {
		return i < 0 ? array.size() + i : i;
	}

	@Override
	public boolean hasNext() {
		return step > 0 ? (idx + step <= to) : (idx + step >= to);
	}

	@Override
	public Map.Entry<Integer, Object> next() {
		idx += step;
		return entry();
	}

	protected JSONEntry<Integer, Object> entry() {
		return new JSONEntry<Integer, Object>(idx, current = array.array
				.get(idx));
	}

	@Override
	public void remove() {
		array.array.remove(idx);
		idx -= step;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public Object getCurrent() {
		return current;
	}

	public void setCurrent(Object current) {
		this.current = current;
	}

	public JSONArray<?> getArray() {
		return array;
	}

	public void setArray(JSONArray<?> array) {
		this.array = array;
	}

}

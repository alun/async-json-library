package org.async.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.async.json.jpath.Iterable;
import org.async.json.jpath.JPath;
import org.async.json.jpath.JPathIterator;
import org.async.json.jpath.JPathPoint;

public class JSONArray<T> extends Iterable<Integer, T> {
	protected List<Object> array = new ArrayList<Object>();

	public JSONArray() {

	}

	public JSONArray( Collection<T> collection ) {
		for( T elem : collection )
			add( elem );
	}


	public Integer getInteger(int idx) {
		return ((Number) array.get(idx)).intValue();
	}

	public Double getDouble(int idx) {
		return ((Number) array.get(idx)).doubleValue();
	}

	public Long getLong(int idx) {
		return ((Number) array.get(idx)).longValue();
	}

	public String getString(int idx) {
		Object rs = array.get(idx);
		return rs == null ? null : rs.toString();
	}

	public JSONObject getObject(int idx) {
		return (JSONObject) array.get(idx);
	}

	public Boolean getBoolean(int idx) {
		return (Boolean) array.get(idx);
	}

	public JSONArray<?> getArray(int idx) {
		return (JSONArray<?>) array.get(idx);
	}

	public void add(String value) {
		array.add(value);
	}

	public void add(JSONObject value) {
		array.add(value);
	}

	public void add(JSONArray<?> value) {
		array.add(value);
	}

	public void add(Number value) {
		array.add(value);
	}

	public void add(int idx, String value) {
		array.add(idx, value);
	}

	public void add(int idx, JSONObject value) {
		array.add(idx, value);
	}

	public void add(int idx, JSONArray<?> value) {
		array.add(idx, value);
	}

	public void add(int idx, Number value) {
		array.add(idx, value);
	}

	public void add(Boolean value) {
		array.add(value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<T> iterator() {
		return ((List<T>) array).iterator();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Iterator<Entry<Integer, T>> iterator(JPathPoint point,Iterable<Object,Object> root) {
		return (Iterator) ArrayIterator.getIterator(this,point,root);
	}

	protected void add(Object res) {
		array.add(res);

	}

	@Override
	public String toString() {
		return array.toString();
	}

	public int size() {
		return array.size();
	}

	public T[] toArray(T[] rs) {
		return array.toArray(rs);
	}

	@SuppressWarnings("unchecked")
	public List<T> toList(List<T> list) {
		((List) list).addAll(array);
		return list;
	}

	@Override
	protected boolean matches(Iterator<JPathPoint> it) {
		if(it==null) {
			return true;
		} else {
			//TODO iteration check for Conditions
			//			JPathPoint next = it.next();
//			if(next.matches(this)) {
//				Iterator<Entry<Integer, T>> iterator = contentIterator(next);
//				while (iterator.hasNext()) {
//
//
//				}
//			}
			return false;
		}
	}


	@SuppressWarnings("unchecked")
	public Iterator<Entry<Object, Object>> iterator(JPath path) {
		return new JPathIterator((Iterable)this,path);
	}



}

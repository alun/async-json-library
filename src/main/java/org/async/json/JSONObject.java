package org.async.json;


import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.async.json.jpath.Iterable;
import org.async.json.jpath.JPath;
import org.async.json.jpath.JPathIterator;
import org.async.json.jpath.JPathPoint;

public class JSONObject extends Iterable<String,Object> {
	protected SortedMap<String, Object> fields = new TreeMap<String, Object>();

	public Double getDouble(String key) {
		Number number = ((Number) fields.get(key));
		return number==null?null:number.doubleValue();
	}

	public Boolean getBoolean(String key) {
		Boolean bool = ((Boolean) fields.get(key));
		return bool==null?null:bool;
	}

	public Integer getInteger(String key) {
		Number number = ((Number) fields.get(key));
		return number==null?null:number.intValue();
	}

	public Long getLong(String key) {
		Number number = ((Number) fields.get(key));
		return number==null?null:number.longValue();
	}

	public Number getNumber(String key) {
		return ((Number) fields.get(key));
	}

	public String getString(String key) {
		Object result = fields.get(key);
		return result == null ? null : result.toString();
	}

	public JSONObject getObject(String key) {
		return (JSONObject) fields.get(key);
	}

	public Object get(String key) {
		return fields.get(key);
	}



	public boolean contains(String key) {
		return fields.containsKey(key);
	}

	public JSONArray<?> getArray(String key) {
		return (JSONArray<?>) fields.get(key);
	}

	public void put(String key, Number number) {
		this.fields.put(key, number);
	}

	protected void put(String key, Object obj) {
		this.fields.put(key, obj);
	}

	public void put(String key, String value) {
		this.fields.put(key, value);
	}

	public void put(String key, Boolean value) {
		this.fields.put(key, value);
	}
	public void put(String key, JSONObject object) {
		this.fields.put(key, object);
	}

	public void put(String key, JSONArray<?> array) {
		this.fields.put(key, array);
	}

	@Override
	public String toString() {
		return fields.toString();
	}

	public Set<String> keySet() {
		return fields.keySet();
	}

	public SortedMap<String, Object> getFields() {
		return fields;
	}

	public void setFields(SortedMap<String, Object> fields) {
		this.fields = fields;
	}

	protected Iterator<Map.Entry<String,Object>> iterator(JPathPoint point,Iterable<Object,Object> root) {
		return new ObjectIterator(this,point);
	}

//	@SuppressWarnings("unchecked")
//	@Override
//	public JPathIterator jpathIterator() {
//		return new JPathIterator((Iterable)this);
//	}

	@Override
	public Iterator<Object> iterator() {
		return fields.values().iterator();
	}

	@Override
	protected boolean matches(Iterator<JPathPoint> it) {
		return false;
	}

	@SuppressWarnings("unchecked")
	public Iterator<Entry<Object, Object>> iterator(JPath path) {
		return new JPathIterator((Iterable)this,path);
	}



}

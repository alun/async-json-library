package org.async.json;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.async.json.jpath.Iterable;
import org.async.json.jpath.JPathPoint;
import org.async.json.jpath.conditions.ConditionObjectIterator;
import org.async.json.jpath.points.ObjectPoint;

public class ObjectIterator implements Iterator<Map.Entry<String, Object>> {
	protected JSONObject object;
	protected Entry<String, Object> e;
	protected Iterator<Entry<String, Object>> it;
	protected String key;


	protected ObjectIterator(JSONObject object) {
		this(object, null);
	}

	protected ObjectIterator(JSONObject object, JPathPoint point) {
		super();
		this.object = object;
		if(point instanceof ObjectPoint) {
			key=((ObjectPoint)point).getKey();
		}
		it=object.getFields().entrySet().iterator();
	}

	public static ObjectIterator getIterator(JSONObject object, JPathPoint point,Iterable<Object,Object> root) {
		return point==null||point.getCondition()==null?new ObjectIterator(object,point):new ConditionObjectIterator(object,point,root);
	}

	@Override
	public boolean hasNext() {
		if(key!=null) {
			return e==null&&object.contains(key);
		}
		return it.hasNext();
	}

	@Override
	public Entry<String, Object> next() {
		if(key!=null) {
			return e=new JSONEntry<String,Object>(key,object.get(key));
		}
		return e = it.next();
	}

	@Override
	public void remove() {
		if(key!=null) {
			Utils.remove(object,key);
		}

	}

	public JSONObject getObject() {
		return object;
	}

	public void setObject(JSONObject object) {
		this.object = object;
	}

	public Entry<String, Object> getE() {
		return e;
	}

	public void setE(Entry<String, Object> e) {
		this.e = e;
	}

	public Iterator<Entry<String, Object>> getIt() {
		return it;
	}

	public void setIt(Iterator<Entry<String, Object>> it) {
		this.it = it;
	}

}

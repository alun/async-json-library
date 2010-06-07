package org.async.json.jpath;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


public abstract class Iterable<K,V> implements java.lang.Iterable<V>{
	protected abstract Iterator<Map.Entry<K, V>> iterator(JPathPoint point,Iterable<Object,Object> root);

	protected abstract boolean matches(Iterator<JPathPoint> it);

	public abstract Iterator<Map.Entry<Object, Object>> iterator(JPath path);

	@SuppressWarnings("unchecked")
	public Iterator<Entry<Object, Object>> iterator(String jpath) throws IOException, ParseException {
		return new JPathIterator((Iterable)this,JPathParser.parse(new StringReader(jpath)));
	}

}

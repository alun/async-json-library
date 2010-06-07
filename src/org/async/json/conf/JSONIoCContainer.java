package org.async.json.conf;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.async.json.JSONObject;
import org.async.json.Utils;
import org.async.json.in.RootParser;
import org.async.json.in.ex.XJSONParser;
import org.async.json.in.ex.XJSONReader;
import org.async.json.jpath.Iterable;

public class JSONIoCContainer {
	protected RootParser parser = new RootParser();
	protected JSONConfObject root;

	public JSONIoCContainer(Reader reader) throws IOException, ParseException,
			NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		super();
		XJSONParser parser = new XJSONParser(new ConfObjectBuilderCallback());
		root = (JSONConfObject) parser.parse(new XJSONReader(reader));
		iterate(root, root);
		configure(root);
		initialize(root);

	}

	public Object getBean(String jpath) throws IOException, ParseException {
		Iterator<Entry<Object, Object>> iterator = root.iterator(jpath);
		if (iterator.hasNext()) {
			return getValue(iterator.next().getValue());
		}
		return null;
	}

	private void initialize(java.lang.Iterable<?> iterable) {
		Iterator<?> iterator = iterable.iterator();
		while (iterator.hasNext()) {
			Object next = iterator.next();
			Object value = getValue(next);
			if (value instanceof Initializable) {
				if(((Configurable) next).isInitialized()) {
					continue;
				}
				((Initializable) value).init();
				((Configurable) next).setInitialized(true);

			}
			if (next instanceof java.lang.Iterable) {
				initialize((java.lang.Iterable<?>) next);
			}
		}

	}

	private void configure(JSONConfObject object) throws NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		for (String key : object.keySet()) {
			Object v = Utils.get(object, key);
			configure(v);
			set(object.getAttachment(), key, getValue(v));
		}

	}

	@SuppressWarnings("unchecked")
	private void configure(Object v) throws NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		if (v instanceof Configurable&&!((Configurable)v).isConfigured()) {
			((Configurable)v).setConfigured(true);
			if (v instanceof JSONConfObject) {
				configure((JSONConfObject) v);
			} else if (v instanceof JSONConfArray) {
				configure((JSONConfArray) v);

			}
		}
	}

	@SuppressWarnings("unchecked")
	private void configure(JSONConfArray ar) throws NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		List list = (List) ar.getAttachment();
		for (int i = 0; i < ar.size(); i++) {
			Object v = Utils.get(ar, i);
			configure(v);
			list.add(getValue(v));
		}
	}

	@SuppressWarnings("unchecked")
	private void iterate(JSONConfObject root, JSONConfObject object)
			throws IOException, ParseException {
		for (String key : object.keySet()) {
			Object v = Utils.get(object, key);
			Iterator<Entry<Object, Object>> it = getIterator(root,
					(Iterable) object, v);
			if (it != null) {
				if (it.hasNext()) {
					Object value = it.next().getValue();
					Utils.put(object, key, value);
				} else {
					throw new IllegalArgumentException(v.toString());
				}
			}
			iterate(root, v);
		}
	}

	private Object getValue(Object value) {
		return value instanceof Configurable ? ((Configurable) value)
				.getAttachment() : value;
	}

	public Iterator<Entry<Object, Object>> getIterator(JSONObject root,
			Iterable<Object, Object> object, Object v) throws IOException,
			ParseException {
		if (v instanceof String
				&& (v.toString().startsWith("$") || v.toString()
						.startsWith("@"))) {
			return v.toString().startsWith("$") ? root.iterator(v.toString())
					: object.iterator(v.toString());
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private void iterate(JSONConfObject root, JSONConfArray<?> ar)
			throws IOException, ParseException {
		for (int i = 0; i < ar.size(); i++) {
			Object v = Utils.get(ar, i);
			Iterator<Entry<Object, Object>> it = getIterator(root,
					(Iterable) ar, v);
			if (it != null) {
				if (it.hasNext()) {
					Object value = it.next().getValue();
					Utils.set(ar, i, value);
					//((List) ar.getAttachment()).add(getValue(value));
				} else {
					throw new IllegalArgumentException(v.toString());
				}
			}
			iterate(root, v);
		}
	}

	@SuppressWarnings("unchecked")
	private void iterate(JSONConfObject root, Object v) throws IOException,
			ParseException {
		if (v instanceof JSONConfObject) {
			iterate(root, (JSONConfObject) v);
		} else if (v instanceof JSONConfArray) {
			iterate(root, (JSONConfArray) v);
		}
	}

	private Method findMethod(String name, Class<?> instanceClass,
			Class<?> paramClass) throws NoSuchMethodException {
		for (Method m : instanceClass.getDeclaredMethods()) {
			if (m.getName().equals(name)) {
				if (checkParam(m, paramClass)) {
					return m;
				}
			}
		}
		if (instanceClass.getSuperclass() != null) {
			return findMethod(name, instanceClass.getSuperclass(), paramClass);
		}
		throw new NoSuchMethodException();
	}

	private boolean checkParam(Method m, Class<?> paramClass) {
		if (m.getParameterTypes().length > 0) {
			Class<?> p = m.getParameterTypes()[0];
			if (p.isPrimitive()) {
				return p.equals(getFieldByName("TYPE", paramClass));
			} else {
				return p.isAssignableFrom(paramClass);
			}
		} else {
			return true;
		}
	}

	private Object getFieldByName(String fname, Class<?> paramClass) {
		try {
			Field field = paramClass.getDeclaredField(fname);
			field.setAccessible(true);
			return field.get(paramClass);
		} catch (Exception e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private void set(Object instance, String key, Object var)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		if (instance instanceof Map) {
			((Map<String, Object>) instance).put(key, var);
		} else {
			callSetter(instance, key, var);
		}
	}

	private void callSetter(Object instance, String key, Object var)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		Method m = findMethod(("set" + key.substring(0, 1).toUpperCase() + key
				.substring(1, key.length())), instance.getClass(), var
				.getClass());
		Class<?> p0 = m.getParameterTypes()[0];
		m.invoke(instance, p0.isPrimitive() ? getFieldByName("value", var) : p0
				.cast(var));
	}

	private Object getFieldByName(String fname, Object o) {
		try {
			Field field = o.getClass().getDeclaredField(fname);
			field.setAccessible(true);
			return field.get(o);
		} catch (Exception e) {
			return null;
		}
	}

}

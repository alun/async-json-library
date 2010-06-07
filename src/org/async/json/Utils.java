package org.async.json;

public class Utils {
	public static void add(JSONArray<Object> array, Object object) {
		array.add(object);
	}

	public static Object get(JSONArray<?> array, int idx) {
		return array.array.get(idx);
	}

	public static Object get(JSONObject object, String key) {
		return object.fields.get(key);
	}

	public static void put(JSONObject obj, String name, Object object) {
		obj.put(name, object);
	}

	public static void set(JSONArray<?> array, int idx,Object object) {
		array.array.set(idx,object);
	}

	public static void remove(JSONObject object, String k) {
		object.fields.remove(k);

	}
}

package org.async.json;

public class JSONEntry<K, V> implements java.util.Map.Entry<K, V> {
	private K key;
	private V value;

	public JSONEntry(K key, V value) {
		super();
		this.key = key;
		this.value = value;
	}

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	@Override
	public V setValue(V value) {
		this.value=value;
		return null;
	}



}

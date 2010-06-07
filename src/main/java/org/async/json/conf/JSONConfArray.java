package org.async.json.conf;

import org.async.json.JSONArray;

public class JSONConfArray<T> extends JSONArray<T> implements Configurable {
	private Object attachment;
	private boolean configured;
	private boolean initialized;

	public Object getAttachment() {
		return attachment;
	}

	public void setAttachment(Object attachment) {
		this.attachment = attachment;
	}

	public boolean isConfigured() {
		return configured;
	}

	public void setConfigured(boolean configured) {
		this.configured = configured;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

}

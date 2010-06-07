package org.async.json.conf;

import org.async.json.JSONObject;

public class JSONConfObject extends JSONObject implements Configurable {
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

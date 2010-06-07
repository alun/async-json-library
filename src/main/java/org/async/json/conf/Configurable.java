package org.async.json.conf;

public interface Configurable {
	public Object getAttachment();

	public void setAttachment(Object attachment);

	public boolean isConfigured();

	public boolean isInitialized();

	public void setConfigured(boolean configured);

	public void setInitialized(boolean initialized);
}

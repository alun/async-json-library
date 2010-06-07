package org.async.json.in;

import java.util.LinkedList;
import java.util.List;

public class ObjectListBuilderCallback extends ObjectBuilderCallback {
	private List<Object> objects = new LinkedList<Object>();

	@Override
	public void endObject() {
		super.endObject();
		if (stack.isEmpty()) {
			objects.add(getResult());
		}
	}

	public List<Object> getObjects() {
		return objects;
	}

	public void setObjects(List<Object> objects) {
		this.objects = objects;
	}

	
	
	
}

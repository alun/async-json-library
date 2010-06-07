package org.async.json.conf;

import java.util.ArrayList;
import java.util.HashMap;

import org.async.json.Utils;
import org.async.json.in.ObjectBuilderCallback;

@SuppressWarnings("unchecked")
public class ConfObjectBuilderCallback extends ObjectBuilderCallback {
	@Override
	public void startObject() {
		stack.addFirst(new JSONConfObject());
	}

	@Override
	public void endObject() {
		JSONConfObject obj = (JSONConfObject) stack.getFirst();
		String className = obj.getString("className");
		Utils.remove(obj,"className");
		try {
			obj.setAttachment(className == null ? new HashMap<String, Object>()
					: Class.forName(className).newInstance());
		} catch (Exception e) {
			unknownError(e);
		}
		super.endObject();
	}



	@Override
	public void startArray() {
		stack.addFirst(new JSONConfArray<Object>());
	}

	@Override
	public void endArray() {
		JSONConfArray<Object> ar = (JSONConfArray<Object>) stack.getFirst();
		ar.setAttachment(new ArrayList(ar.size()));
		super.endArray();
	}
}

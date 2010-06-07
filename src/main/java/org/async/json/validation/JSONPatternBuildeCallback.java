package org.async.json.validation;

import org.async.json.in.ObjectBuilderCallback;

@SuppressWarnings("unchecked")
public class JSONPatternBuildeCallback extends ObjectBuilderCallback {

	@Override
	public void startObject() {
		stack.addFirst(new JSONPattern());
	}
}

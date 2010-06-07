package org.async.json.jpath;


public interface JPathCondition {
	boolean matches(Iterable<Object, Object> current,Iterable<Object, Object> root);
}

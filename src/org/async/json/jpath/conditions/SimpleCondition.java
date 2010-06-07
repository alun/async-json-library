package org.async.json.jpath.conditions;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.async.json.jpath.Iterable;
import org.async.json.jpath.JPath;
import org.async.json.jpath.JPathCondition;
import org.async.json.jpath.Operator;

public class SimpleCondition implements JPathCondition {
	private static class Undefined {
		//TODO replace Condition with Operators +-*/
	}
	public static Undefined UNDEFINED = new Undefined();
	private Object right = UNDEFINED;
	private Object left = UNDEFINED;
	private Operator operator;

	public SimpleCondition(Object left, Object right, Operator operator) {
		super();
		this.right = right;
		this.left = left;
		this.operator = operator;
	}

	@Override
	public boolean matches(Iterable<Object, Object> current,
			Iterable<Object, Object> root) {
		if(operator==null) {
			if(left instanceof Undefined) {
				return getValues(right, current, root).isEmpty();
			} else	if(right instanceof Undefined) {
				return !getValues(left, current, root).isEmpty();
			}
		} else {
			List<Object> a = getValues(left, current, root);
			List<Object> b = getValues(right, current, root);
			for(Object ai:a) {
				for(Object bi:b) {
					if(operator.run(ai, bi)) return true;
				}
			}
		}
		return false;
	}

	private List<Object> getValues(Object v, Iterable<Object, Object> current,
			Iterable<Object, Object> root) {
		List<Object> values = new LinkedList<Object>();
		if (v instanceof JPath) {
			JPath path = (JPath) v;
			Iterator<Entry<Object, Object>> i = path.isRoot() ? root
					.iterator(path) : current.iterator(path);
			while (i.hasNext()) {
				values.add(i.next());
			}
		} else {
			values.add(v);
		}
		return values;
	}

}

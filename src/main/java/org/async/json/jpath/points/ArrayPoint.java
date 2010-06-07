package org.async.json.jpath.points;

import org.async.json.JSONArray;
import org.async.json.jpath.Iterable;
import org.async.json.jpath.JPathCondition;
import org.async.json.jpath.JPathPoint;

public class ArrayPoint extends JPathPoint {
	private Object i;
	private Object to;
	private Object step;

	public ArrayPoint() {
		super();
	}

	public ArrayPoint(Object i, Object to, Object step) {
		super();
		//TODO i,to,step can be JPathExpression
		this.i = i;
		this.to = to;
		this.step = step;
	}

	public ArrayPoint(Object i, Object to, Object step,
			JPathCondition condition) {
		super(condition);
		this.i = i;
		this.to = to;
		this.step = step;
	}


	@SuppressWarnings("unchecked")
	@Override
	public boolean matches(Iterable<Object, Object> current,
			Iterable<Object, Object> root) {
		if (current instanceof JSONArray) {
			return super.matches(current, root);
		}
		return false;
	}

	public Object getI() {
		return i;
	}

	public void setI(Object i) {
		this.i = i;
	}

	public Object getTo() {
		return to;
	}

	public void setTo(Object to) {
		this.to = to;
	}

	public Object getStep() {
		return step;
	}

	public void setStep(Object step) {
		this.step = step;
	}
	@Override
	public String toString() {
		return "["+i+","+to+":"+step+"]";
	}

}

package org.async.json.jpath;

public class JPathPoint {
	protected JPathCondition condition;

	public JPathPoint() {
		super();
	}

	public JPathPoint(JPathCondition condition) {
		super();
		this.condition = condition;
	}

	public boolean matches(Iterable<Object, Object> current,
			Iterable<Object, Object> root) {
		return (condition == null) || (current.iterator(this, root).hasNext());
	}

	public JPathCondition getCondition() {
		return condition;
	}

	public void setCondition(JPathCondition condition) {
		this.condition = condition;
	}

}

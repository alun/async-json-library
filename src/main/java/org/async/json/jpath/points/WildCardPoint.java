package org.async.json.jpath.points;

import org.async.json.jpath.Iterable;
import org.async.json.jpath.JPathPoint;

public class WildCardPoint extends JPathPoint {


	public WildCardPoint() {
		super();

	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean matches(Iterable<Object, Object> current,
			Iterable<Object, Object> root) {
		return true;
	}


}

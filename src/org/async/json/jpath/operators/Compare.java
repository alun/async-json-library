package org.async.json.jpath.operators;

import org.async.json.jpath.Operator;

public class Compare implements Operator {
	public static int NEQ=-3;
	public static int EQ = 0;
	public static int GE = 1;
	public static int G = 2;
	public static int LE = -1;
	public static int L = -2;
	private int type;

	public Compare(int type) {
		super();
		this.type = type;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean run(Object left, Object right) {
		if (right == null || left == null)
			return (type == LE || type == GE || type == EQ) && (left == right);
		if (type == EQ) {
			return left.equals(right);
		}
		int cmp = ((Comparable) left).compareTo((Comparable) right);
		return (type == GE && cmp >= 0) || (type == LE && cmp <= 0)
				|| (type == G && cmp > 0) || (type == L && cmp < 0);
	}
}

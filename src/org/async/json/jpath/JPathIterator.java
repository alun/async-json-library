package org.async.json.jpath;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.Map.Entry;


public class JPathIterator implements Iterator<Map.Entry<Object, Object>> {
	private Stack<Iterator<Map.Entry<Object, Object>>> iterators = new Stack<Iterator<Map.Entry<Object, Object>>>();
	private Stack<Integer> indexes = new Stack<Integer>();
	private Iterator<Map.Entry<Object, Object>> iterator;
	private Iterable<Object, Object> root;
	private JPath path;
	private int pidx = 0;

	public JPathIterator(Iterable<Object, Object> iterable,JPath path) {
		super();
		this.path = path;
		this.root=iterable;
		if (matches(path.getPoints()[0], iterable)) {
			iterators.push(iterable.iterator(path.getPoints()[0],root));
			indexes.push(0);
			pidx++;
			if (pidx == path.getPoints().length) {
				iterator=iterators.peek();
			}

		}
	}

	@Override
	public boolean hasNext() {
		if(iterator==null) iterator=nextIterator();
		return iterator!=null&&iterator.hasNext();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Entry<Object, Object> next() {
		if (!iterators.isEmpty()) {
			if (iterator != null) {
				 Entry<Object, Object> next = iterator.next();
				 if(!iterator.hasNext()) iterator=null;
				 return next;
			} else {
				iterator=nextIterator();
			}
			if (iterator != null) {
				return next();
			}
		}
		throw new NoSuchElementException();
	}

	private Iterator<Map.Entry<Object, Object>> nextIterator() {
		removeEmptyIterators();
		while (!iterators.isEmpty()) {
			doNext();
			if (pidx == path.getPoints().length&&iterators.peek().hasNext()) {

				return iterators.peek();
			}
			removeEmptyIterators();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private void doNext() {
		Entry<Object, Object> next = iterators.peek().next();
		if (next.getValue() instanceof Iterable && pidx != path.getPoints().length) {
			Iterable itable = ((Iterable) next.getValue());
			if (path.getPoints()[pidx - 1] == null) {
				if (path.getPoints().length == pidx || !matches(path.getPoints()[pidx], itable)) {
					indexes.push(pidx - 1);
					iterators.push(itable.iterator(path.getPoints()[pidx - 1],root));
				} else {
					iterators.push(itable.iterator(path.getPoints()[pidx],root));
					indexes.push(pidx);
					pidx++;
				}
			} else {
				if (path.getPoints().length != pidx && matches(path.getPoints()[pidx], itable)) {
					iterators.push(itable.iterator(path.getPoints()[pidx],root));
					indexes.push(pidx);
					pidx++;
				}
			}
		}

	}

	private void removeEmptyIterators() {
		while (!iterators.isEmpty() && !iterators.peek().hasNext()) {
			iterators.pop();
			indexes.pop();
			if (!indexes.isEmpty()) {
				pidx = indexes.peek() + 1;
			}
		}
	}

	private boolean matches(JPathPoint point, Iterable<Object, Object> iterable) {
		return point == null || point.matches(iterable,root);
	}

	@Override
	public void remove() {
		iterators.peek().remove();
	}

}

package org.async.json.jpath;


public class JPath {
	private JPathPoint[] points;
	private boolean root;


	public JPath(JPathPoint[] points) {
		super();
		this.points = points;
	}

	public JPath(JPathPoint[] points, boolean root) {
		super();
		this.points = points;
		this.root = root;
	}

	protected JPath() {
		super();
	}

	public JPathPoint[] getPoints() {
		return points;
	}

	public void setPoints(JPathPoint[] points) {
		this.points = points;
	}

	public boolean isRoot() {
		return root;
	}

	public void setRoot(boolean root) {
		this.root = root;
	}

}

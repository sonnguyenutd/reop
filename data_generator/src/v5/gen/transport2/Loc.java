package v5.gen.transport2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gen.utils.Utils;

public class Loc {

	public String name;
	int level;
	boolean isAHub;

	List<Loc> children;
	// Generated part
	Set<Route> routes;

	public Loc(String lName) {
		this.name = lName;
		routes = new HashSet<Route>();
		level = -1;
		isAHub = false;
		children = new ArrayList<>();
	}

	public void connect() {
		connectChildren();
		connectToChildren();
	}

	private void connectToChildren() {
		for (Loc l : children) {
			if (l.isAHub()) {
				Route r1 = new Route(this, l);
				Route r2 = new Route(l, this);
				r1.setHubConnector(true);
				r2.setHubConnector(true);
				routes.add(r1);
				routes.add(r2);
//				System.out.println("connectToChildren");
			}
		}
	}

	private boolean isAHub() {
		return isAHub;
	}

	private void connectChildren() {
		for (Loc l1 : children) {
			for (Loc l2 : children) {
				int r = Utils.randomNumber(1, Generator.BRANCHING_FACTOR);
				if (!l1.equals(l2) && r % Generator.BRANCHING_FACTOR != 0) {
					Route route = new Route(l1, l2);
					routes.add(route);
				}
			}
		}
	}

	public Set<Route> getRoutes() {
		return routes;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		return name.equals(obj.toString());
	}

	public String getName() {
		return name;
	}

	public List<Loc> getChildren() {
		return children;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}

	public void setChildren(List<Loc> prevLocs) {
		int numOfHub = Utils.randomNumber(1, prevLocs.size());
		for (int i = 0; i < numOfHub; i++)
			prevLocs.get(i).isAHub = true;
		for (Loc l : prevLocs) {
			children.add(l);
		}
	}

}

package v5.gen.transport2;

import java.util.HashSet;
import java.util.Set;

public class Route {
	public Loc from;
	public Loc to;
	boolean hubConnector;

	public Route(Loc from, Loc to) {
		this.from = from;
		this.to = to;
	}

	public Route(String f, String t) {
		this.from = new Loc(f);
		this.to = new Loc(t);
	}

	public void setHubConnector(boolean hubConnector) {
		this.hubConnector = hubConnector;
	}

	public boolean isHubConnector() {
		return hubConnector;
	}

	public Set<Loc> getAllLocs() {
		Set<Loc> result = new HashSet<>();
		result.add(from);
		result.add(to);
		return result;
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public String toString() {
		return from.name + "->" + to.name;
	}

	@Override
	public boolean equals(Object obj) {
		return toString().equals(obj.toString());
	}
}

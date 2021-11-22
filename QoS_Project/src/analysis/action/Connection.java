package analysis.action;

import java.util.ArrayList;
import java.util.List;

public class Connection {
	Operator dependent;
	List<Predicate> connectors;

	public Connection() {
		connectors = new ArrayList<>();
	}

	public Connection(Operator dependent) {
		this.dependent = dependent;
		connectors = new ArrayList<>();
	}

	public void setConnectors(List<Predicate> connectors) {
		this.connectors = connectors;
	}

	public List<Predicate> getConnectors() {
		return connectors;
	}

	public Operator getDependent() {
		return dependent;
	}

	@Override
	public boolean equals(Object obj) {
		return hashCode() == obj.hashCode();
	}

	@Override
	public int hashCode() {
		return dependent.hashCode();
	}

	@Override
	public String toString() {
		return dependent.toString() + "\n--->" + connectors.size() + "==" + connectors;
	}
}

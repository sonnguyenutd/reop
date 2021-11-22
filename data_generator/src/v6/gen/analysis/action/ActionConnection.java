package v6.gen.analysis.action;

import java.util.ArrayList;
import java.util.List;

import v5.gen.supply_chain.Action;

public class ActionConnection {
	Action dependent;
	List<String> connectors;

	public ActionConnection() {
		connectors = new ArrayList<>();
	}

	public ActionConnection(Action dependent) {
		this.dependent = dependent;
		connectors = new ArrayList<>();
	}

	public void setConnectors(List<String> connectors) {
		this.connectors = connectors;
	}

	public List<String> getConnectors() {
		return connectors;
	}

	public Action getDependent() {
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

package v6.gen.analysis.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DependentCombo {
	public Operator host;
	public Set<Operator> dependents;
	public List<Connection> cons;
	public Map<Operator, Connection> mergedCons;

	public DependentCombo() {
		dependents = new HashSet<>();
		cons = new ArrayList<Connection>();
	}

	public DependentCombo(Operator host) {
		this.host = host;
		dependents = new HashSet<>();
		cons = new ArrayList<Connection>();
	}

	public void merge(Operator d, Connection c) {
		this.dependents.add(d);
		this.cons.add(c);
	}

	public void mergeAll(List<Operator> ds, List<Connection> cs) {
		this.dependents.addAll(ds);
		this.cons.addAll(cs);
	}

	public void merge(DependentCombo indirectCombo) {
		this.dependents.addAll(indirectCombo.dependents);
		this.cons.addAll(indirectCombo.cons);
	}

	public void mergeCons() {
		mergedCons = new HashMap<Operator, Connection>();
		Map<Operator, Set<Predicate>> dependent_cons = new HashMap<>();
		for (int i = 0; i < cons.size() - 1; i++) {
			Connection c = cons.get(i);
			Operator d = c.dependent;
			Set<Predicate> dc = dependent_cons.get(d);
			if (dc == null)
				dc = new HashSet<Predicate>();
			dc.addAll(c.connectors);
			dependent_cons.put(d, dc);
		}
		for (Operator o : dependent_cons.keySet()) {
			Connection c = new Connection(o);
			c.setConnectors(new ArrayList<>(dependent_cons.get(o)));
			mergedCons.put(o, c);
		}
	}
	public Map<Operator, Connection> getMergedCons() {
		return mergedCons;
	}
}

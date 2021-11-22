package analysis.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DendentActionCombo {
	public Action host;
	public Set<Action> dependents;
	public List<ActionConnection> cons;
	public Map<Action, ActionConnection> mergedCons;

	public DendentActionCombo() {
		dependents = new HashSet<>();
		cons = new ArrayList<ActionConnection>();
	}

	public DendentActionCombo(Action host) {
		this.host = host;
		dependents = new HashSet<>();
		cons = new ArrayList<ActionConnection>();
	}

	public void merge(Action d, ActionConnection c) {
		this.dependents.add(d);
		this.cons.add(c);
	}

	public void mergeAll(List<Action> ds, List<ActionConnection> cs) {
		this.dependents.addAll(ds);
		this.cons.addAll(cs);
	}

	public void merge(DendentActionCombo indirectCombo) {
		this.dependents.addAll(indirectCombo.dependents);
		this.cons.addAll(indirectCombo.cons);
	}

	public void mergeCons() {
		mergedCons = new HashMap<Action, ActionConnection>();
		Map<Action, Set<String>> dependent_cons = new HashMap<>();
		for (int i = 0; i < cons.size() - 1; i++) {
			ActionConnection c = cons.get(i);
			Action d = c.dependent;
			Set<String> dc = dependent_cons.get(d);
			if (dc == null)
				dc = new HashSet<String>();
			dc.addAll(c.connectors);
			dependent_cons.put(d, dc);
		}
		for (Action o : dependent_cons.keySet()) {
			ActionConnection c = new ActionConnection(o);
			c.setConnectors(new ArrayList<>(dependent_cons.get(o)));
			mergedCons.put(o, c);
		}
	}

	public Map<Action, ActionConnection> getMergedCons() {
		return mergedCons;
	}
}

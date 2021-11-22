package v6.gen.analysis.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import v5.gen.supply_chain.Action;

public class Analyzer {
	private static final int MAX_LEVEL = 10;

	public static void main(String[] args) {
		List<Operator> ops = Operator.allOps();
		for (Operator o : ops) {
			DependentCombo combo = identifyDependents2(o, ops, 0);
//			System.out.println(combo.dependents.size());
			combo.mergeCons();
			o.setCombo(combo);
		}
		System.out.println(isGroupableOperatorLeve(ops.get(0), ops.get(0)));
	}

	public static boolean isGroupable(Action a1, Action a2, List<Operator> ops) {
		if(isGroupableOperatorLeve(a1.getOp(), a2.getOp())) 
			return true;
		DendentActionCombo dependent1 = identifyDependentActions(a1, null, 0);
		a1.setCombo(dependent1);
		DendentActionCombo dependent2 = identifyDependentActions(a2, null, 0);
		a2.setCombo(dependent2);
		Set<Action> commonDependentActions = dependent1.dependents;
		commonDependentActions.retainAll(dependent2.dependents);

		Map<Action, ActionConnection> cons1 = a1.getCombo().getMergedCons();
		Map<Action, ActionConnection> cons2 = a2.getCombo().getMergedCons();
		for (Action d : commonDependentActions) {
			List<String> connectors1 = cons1.get(d).getConnectors();
			List<String> connectors2 = cons2.get(d).getConnectors();
			if(!connectors1.equals(connectors2))
				return false;
		}
		
		return true;
	}

	private static DendentActionCombo identifyDependentActions(Action a, List<Action> acts, int level) {
		DendentActionCombo result = new DendentActionCombo();
		if (level > MAX_LEVEL)
			return result;
		if(acts == null) {
			acts = new ArrayList<Action>();
			for (Operator o : a.getOp().getDependents()) {
				acts.addAll(o.getGroundedActs());
			}
		}
		List<ActionConnection> cons = new ArrayList<>();
		List<Action> temp = new ArrayList<Action>(acts);
		List<Action> directDependent = new ArrayList<Action>();
		for (Action other : temp) {
			if (other.isDependentOf(a)) {
				directDependent.add(other);
				ActionConnection c = new ActionConnection(other);
				c.setConnectors(a.getConnectors(other));
				cons.add(c);
			}
		}
		if (directDependent.isEmpty())
			return result;
		result.mergeAll(directDependent, cons);
		temp.remove(a);
		for (Action dependent : directDependent) {
			DendentActionCombo indirectCombo = identifyDependentActions(dependent, temp, level + 1);
			result.merge(indirectCombo);
		}
		return result;
	}

	private static boolean isGroupableOperatorLeve(Operator o1, Operator o2) {
		Set<Operator> commonDependents = new HashSet<>(o1.getDependents());
		commonDependents.retainAll(o2.getDependents());
		if(commonDependents.isEmpty())
			return true;
		Map<Operator, Connection> cons1 = o1.getCombo().getMergedCons();
		Map<Operator, Connection> cons2 = o2.getCombo().getMergedCons();
		for (Operator d : commonDependents) {
			List<Predicate> connectors1 = cons1.get(d).getConnectors();
			List<Predicate> connectors2 = cons2.get(d).getConnectors();
			if(connectors1.size() != 1 || !connectors1.equals(connectors2))
				return false;
		}
		return true;
	}

	public static DependentCombo identifyDependents2(Operator o, List<Operator> ops, int level) {
		DependentCombo result = new DependentCombo();
		if (level > MAX_LEVEL)
			return result;
		List<Connection> cons = new ArrayList<>();
		List<Operator> temp = new ArrayList<Operator>(ops);
		List<Operator> directDependent = new ArrayList<Operator>();
		for (Operator other : temp) {
			if (other.isDependentOf(o)) {
				directDependent.add(other);
				Connection c = new Connection(other);
				c.setConnectors(o.getConnectors(other));
				cons.add(c);
			}
		}
		if (directDependent.isEmpty())
			return result;
		result.mergeAll(directDependent, cons);
		temp.remove(o);
		for (Operator dependent : directDependent) {
			DependentCombo indirectCombo = identifyDependents2(dependent, temp, level + 1);
			result.merge(indirectCombo);
		}
		return result;
	}

	public static Set<Operator> identifyDependents(Operator o, List<Operator> ops, int level) {
		Set<Operator> result = new HashSet<>();
		if (level > MAX_LEVEL)
			return result;
		List<Operator> temp = new ArrayList<Operator>(ops);
		List<Operator> directDependent = new ArrayList<Operator>();
		for (Operator other : temp) {
			if (other.isDependentOf(o)) {
				directDependent.add(other);
			}
		}
		if (directDependent.isEmpty())
			return result;
		result.addAll(directDependent);
		temp.remove(o);
		for (Operator dependent : directDependent) {
			result.addAll(identifyDependents(dependent, temp, level + 1));
		}
		return result;
	}
}

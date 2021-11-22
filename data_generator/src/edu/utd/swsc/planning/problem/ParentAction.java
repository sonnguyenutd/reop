package edu.utd.swsc.planning.problem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ParentAction extends Action {
	Set<Action> children;
	Map<Prop, List<Action>> dic;
	private static int COUNTER = 0;


	public void setChildren(Set<Action> children) {
		this.children = children;
	}

	public ParentAction() {
		super();
		children = new HashSet<Action>();
		this.name = "Parent_" + COUNTER;
		dic = new HashMap<Prop, List<Action>>();
		COUNTER++;
	}

	public ParentAction(Set<Prop> preconds, Set<Action> children) {
		super();
		this.children = children;
		this.preconds = preconds;
		this.inputs.addAll(preconds);
		dic = new HashMap<Prop, List<Action>>();
		generalize();
		this.name = "Parent_" + COUNTER;
		COUNTER++;
	}

	private void generalize() {
		for (Action child : children) {
			this.outputs.addAll(child.getOutputs());
			this.effects.addAll(child.getEffects());
			for (Prop prop : child.getEffects()) {
				List<Action> cs = dic.get(prop);
				if (cs == null)
					cs = new ArrayList<Action>();
				cs.add(child);
				dic.put(prop, cs);
			}
		}
		for (Prop prop : dic.keySet()) {
			Collections.sort(dic.get(prop), new Comparator<Action>() {
				@Override
				public int compare(Action o1, Action o2) {
					return -(o1.getEffects().size() - o2.getEffects().size());
				}
			});
		}
	}

	public Set<Action> extractChildren(Set<Prop> goal) {
		Set<Prop> subGoals = new HashSet<>(goal);
		Set<Action> requiredChidren = new HashSet<Action>();
		// The case having only one children
		if (this.children.size() == 1)
			return this.children;

		while (!subGoals.isEmpty()) {
			Prop one = selectOne(subGoals);
			if (one != null) {
				Action child = dic.get(one).get(0);
//			acts = getLargestEffectsActions(acts);
				subGoals.removeAll(child.getEffects());
				requiredChidren.add(child);
			}
		}
		return requiredChidren;
	}

//	private Set<Action> getLargestEffectsActions(Set<Action> acts) {
//		Set<Action> result = new HashSet<Action>();
//		for (Action act : result) {
//			result = includeLargerEffects(result, act);
//		}
//		return result;
//	}

//	private Set<Action> includeLargerEffects(Set<Action> result, Action act) {
//		Set<Action> includedEffectsActions = new HashSet<Action>();
//		for (Action action : result) {
//			if (act.getEffects().containsAll(action.getEffects()))
//				includedEffectsActions.add(action);
//		}
//		if (!includedEffectsActions.isEmpty()) {
//			result.removeAll(includedEffectsActions);
//			result.add(act);
//		}
//		return result;
//	}

	private Prop selectOne(Set<Prop> goal) {
		for (Prop prop : goal)
			return prop;
		return null;
	}

}

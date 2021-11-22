package edu.utd.swsc.planning.problem;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import edu.utd.swsc.common.ParentService;
import edu.utd.swsc.common.Service;

public class PlanDomain {
	Set<Action> actions;
	Set<Prop> allProps;
	
	public PlanDomain() {
	}
	public void setActions(Collection<Action> as) {
		this.allProps = new HashSet<>();
		this.actions = new HashSet<Action>();
		for (Action a : as) {
			this.actions.add(a);
			this.actions.addAll(a.getNoOpActions());
			allProps.addAll(a.preconds);
			allProps.addAll(a.effects);
		}
	}
	
	public Set<Prop> getAllProps() {
		return allProps;
	}
	
	public PlanDomain(Set<Service> services) {
		this.actions = new HashSet<Action>();
		for (Service service : services) {
			Action a = service.toAction();
			this.actions.add(a);
			this.actions.addAll(a.getNoOpActions());
		}
	}

	public PlanDomain(Set<ParentService> parents, int x) {
		this.actions = new HashSet<Action>();
		for (ParentService s : parents) {
			Action a = s.toAction();
			this.actions.add(a);
			this.actions.addAll(a.getNoOpActions());
		}
	}

	public Set<Action> getActions() {
		return actions;
	}

	@Override
	public String toString() {
		String result = "";
		for (Action action : actions) {
			result += action.getName() + ":" + action.preconds.size() + "->" + action.effects.size() + "-"
					+ action.nEffects.size();
		}
		return result;
	}
}

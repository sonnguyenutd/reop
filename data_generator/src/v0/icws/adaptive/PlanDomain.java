package v0.icws.adaptive;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import v0.icws.Action;
import v0.icws.Cate;

public class PlanDomain {
	Set<Action> actions;
	Set<String> allProps;
	
	public PlanDomain() {
	}
	public void setActions(List<Cate> allCates) {
		this.allProps = new HashSet<>();
		this.actions = new HashSet<Action>();
		for (Action a : allCates) {
			this.actions.add(a);
			this.actions.addAll(a.getNoOpActions());
			allProps.addAll(a.preconds);
			allProps.addAll(a.pEffects);
		}
	}
	
	public Set<String> getAllProps() {
		return allProps;
	}
	
	public void addProps(Collection<String> props) {
		allProps.addAll(props);
	}

	public Set<Action> getActions() {
		return actions;
	}

	@Override
	public String toString() {
		String result = "";
		for (Action action : actions) {
			result += action.getName() + ":" + action.preconds.size() + "->" + action.pEffects.size() + "-"
					+ action.nEffects.size();
		}
		return result;
	}
}

package v0.icws.adaptive;

import java.util.HashSet;
import java.util.Set;

import v0.icws.Action;

public class Level {
	private static int COUNTER = -1;
	int id;
	ActionLayer A;
	PropLayer P;
	Set<Set<String>> nogood;
	
	public void updateNogood(Set<String> unachievableCombination) {
		this.nogood.add(unachievableCombination);
	}
	
	public boolean isNogood(Set<String> goal) {
		return this.nogood.contains(goal);
	}
	
	public Set<Action> getProviders(String subGoal){
		//TODO: consider mutex
		Set<Action> results = new HashSet<Action>();
		for (Action action : A.actions) {
			if(action.getEffects().contains(subGoal))
				results.add(action);
		}
		return results;
	}

 	public Level() {
		COUNTER++;
		id = COUNTER;
		nogood = new HashSet<Set<String>>();
	}

	public static Level createInit(PropLayer P) {
		Level init = new Level();
		init.P = P;
		return init;
	}

	public void print() {
		System.out.println("---- Level " + id + " -----");
		String content = "";
		String size = "0";
		if (A == null || A.actions == null || A.actions.isEmpty()) {
			content += "<empty actions set>";
		} else {
			for (Action act : A.actions)
				content += (act.getName() + "(" + act.getPreconds().size() + "-" + act.getEffects().size() + ")" + "\t");
			size = "" + A.actions.size();
		}
		content = ("(" + size + " actions) -\t") + (content) + "\n|\n";
		content += ("(" + P.props.size() + " props) -\t");
		if (P == null || P.props == null || P.props.isEmpty()) {
			content = "<empty props set>";
		} else {
			for (String p : P.props)
				content += (p + "\t");
		}
		content = (content) + "\n----------------------------\n";

		System.out.println(content);
	}

	public Level createNext() {
		Level next = new Level();
		next.A = this.P.createNext();
		next.P = next.A.createNext();
		return next;
	}


	public boolean reach(Set<String> subgoal) {
		return P.props.containsAll(subgoal);
	}

}

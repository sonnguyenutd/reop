package edu.utd.swsc.graphplan;

import java.util.HashSet;
import java.util.Set;

import edu.utd.swsc.planning.problem.Action;
import edu.utd.swsc.planning.problem.Prop;

public class Level {
	private static int COUNTER = -1;
	int id;
	ActionLayer A;
	PropLayer P;
	Set<Set<Prop>> nogood;
	
	public void updateNogood(Set<Prop> unachievableCombination) {
		this.nogood.add(unachievableCombination);
	}
	
	public boolean isNogood(Set<Prop> goal) {
		return this.nogood.contains(goal);
	}
	
	public Set<Action> getProviders(Prop subGoal){
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
		nogood = new HashSet<Set<Prop>>();
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
			for (Prop p : P.props)
				content += (p.getName() + "\t");
		}
		content = (content) + "\n----------------------------\n";

		System.out.println(content);
	}

	public Level createNext() {
		Level next = new Level();
		
		next.A = this.P.createNext();
		next.A.computeMu(this.P.PMu);
		
		next.P = next.A.createNext();
		next.P.computeMu(next.A.AMu);
		
		createEdges(this.P, next.A, next.P);
		
		return next;
	}

	private void createEdges(PropLayer pL1, ActionLayer aL, PropLayer pL2) {
		
	}

	public boolean reach(Set<Prop> subgoal) {
		return P.props.containsAll(subgoal);
	}

}

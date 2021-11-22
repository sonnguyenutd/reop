package edu.utd.swsc.graphplan;

import java.util.Set;

import edu.utd.swsc.planning.problem.Action;

public abstract class Layer {
	int id;
	// nullable
	public Layer prev;
	public Layer next;
	public Set<Action> remainingActions;

//	public abstract void computeMu(Set<Pair<Action>> AMu, Set<Pair<Prop>> PMu);
}

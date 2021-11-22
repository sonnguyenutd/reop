package v8.gen.parser;

import java.util.HashSet;
import java.util.Set;

import v5.gen.supply_chain.Action;

public abstract class GroundingThread extends Thread {
	protected int id;
	Set<Action> groundedActions;

	public GroundingThread(int id) {
		groundedActions = new HashSet<Action>();
		this.id = id;
	}

	public void addGroundedAction(Action a) {
		this.groundedActions.add(a);
	}

	public Set<Action> getGroundedActions() {
		return groundedActions;
	}

	public abstract void info();
}

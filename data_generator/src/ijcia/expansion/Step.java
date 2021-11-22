package ijcia.expansion;

import java.util.HashSet;
import java.util.Set;

public class Step {
	Set<String> prevState;
	Set<Action> acts;
	Set<String> nextState;

	public Step(Set<String> prevState) {
		this.prevState = prevState;
		acts = new HashSet<Action>();
	}

	public void setNextState(Set<String> nextState) {
		this.nextState = nextState;
	}

	public void addAction(Action a) {
		this.acts.add(a);
	}

	public Set<Action> getActs() {
		return acts;
	}

	public Set<String> getNextState() {
		return nextState;
	}

	public Set<String> getPrevState() {
		return prevState;
	}
}

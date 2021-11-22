package ijcia.expansion;

import java.util.HashSet;
import java.util.Set;

public class Expander {
	public static void main(String[] args) {

	}

	public static Step expand(Set<Action> acts, Set<String> currState) {
		Step s = new Step(currState);
		Set<String> resultingState = new HashSet<String>(currState);
		for (Action a : acts) {
			if (currState.containsAll(a.getPreconds())) {
				resultingState.addAll(a.getEffects());
				s.addAction(a);
			}
		}
		s.setNextState(resultingState);
		return s;
	}

	public static Set<Action> expand(int step, Set<Action> allParents, Set<String> init) {
		Set<Action> acts = new HashSet<Action>(allParents);
		Set<Action> resultParents = new HashSet<Action>();
		Set<String> currState = new HashSet<String>(init);
		Set<String> prevState = new HashSet<String>();
		for (int i = 0; i < step; i++) {
			prevState = new HashSet<String>(currState);
			for (Action a : acts) {
				if (currState.containsAll(a.getPreconds())) {
					currState.addAll(a.getEffects());
					resultParents.add(a);
				}
			}
			if (prevState.equals(currState))
				break;
			acts.removeAll(resultParents);
		}
		return resultParents;
	}

}

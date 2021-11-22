package v0.icws.adaptive;

import java.util.Set;

import v0.icws.Action;

public abstract class Layer {
	int id;
	// nullable
	public Layer prev;
	public Layer next;
	public Set<Action> remainingActions;

//	public abstract void computeMu(Set<Pair<Action>> AMu, Set<Pair<Prop>> PMu);
}

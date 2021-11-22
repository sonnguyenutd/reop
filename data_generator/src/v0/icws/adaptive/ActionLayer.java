package v0.icws.adaptive;

import java.util.HashSet;
import java.util.Set;

import v0.icws.Action;


public class ActionLayer extends Layer {
	private static int COUNTER = 1;
	Set<Action> actions;
	Set<String> effects;

	public ActionLayer(Set<Action> actions) {
		this.id = ActionLayer.COUNTER;
		this.actions = actions;
		createLayer();
		COUNTER++;
	}

	public PropLayer createNext() {
		Set<String> state = new HashSet<String>();
		// Adding the previous one's
		// state.addAll(((PropLayer) this.prev).props);
		// But using noop operation, so no need to add previous one's props
		state.addAll(this.effects);
		PropLayer next = new PropLayer(remainingActions, state);
//		next = new PropLayer(remainingActions, state);
		next.prev = this;
		this.next = next;
		return next;
	}

	private void createLayer() {
		effects = new HashSet<String>();
		for (Action action : actions)
			effects.addAll(action.getEffects());
	}

	public Set<Action> getActions() {
		return actions;
	}

	public Set<String> getEffects() {
		return effects;
	}
}

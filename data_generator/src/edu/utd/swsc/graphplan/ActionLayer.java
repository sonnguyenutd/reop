package edu.utd.swsc.graphplan;

import java.util.HashSet;
import java.util.Set;

import edu.utd.swsc.planning.problem.Action;
import edu.utd.swsc.planning.problem.Prop;

public class ActionLayer extends Layer {
	private static int COUNTER = 1;
	Set<Pair<Action>> AMu;
	Set<Action> actions;
	Set<Prop> effects;

	public ActionLayer(Set<Action> actions) {
		this.id = ActionLayer.COUNTER;
		this.actions = actions;
		createLayer();
		COUNTER++;
	}

	public PropLayer createNext() {
		Set<Prop> state = new HashSet<Prop>();
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
		effects = new HashSet<Prop>();
		for (Action action : actions)
			effects.addAll(action.getEffects());
	}

	public void computeMu(Set<Pair<Prop>> PMu) {
		this.AMu = new HashSet<Pair<Action>>();
		if (PMu == null)
			return;
		Set<Action> checked = new HashSet<>();
		for (Action a1 : actions) {
			checked.add(a1);
			for (Action a2 : actions) {
				if (!checked.contains(a2)) {
					if (isMu(a1, a2, PMu)) {
						Pair<Action> p = new Pair<Action>(a1, a2);
						this.AMu.add(p);
					}
				}
			}
		}
	}

	public Set<Action> getActions() {
		return actions;
	}

	public Set<Prop> getEffects() {
		return effects;
	}

	public Set<Pair<Action>> getMu() {
		return AMu;
	}

	private boolean isMu(Action a1, Action a2, Set<Pair<Prop>> propMu) {
		return isInterfering(a1, a2) || isInconsistent(a1, a2) || isNeedCompeting(a1, a2, propMu);
	}

	private boolean isNeedCompeting(Action a1, Action a2, Set<Pair<Prop>> propMu) {
		Set<Prop> a1Preconds = new HashSet<>(a1.getPreconds());
		Set<Prop> a2Preconds = new HashSet<>(a2.getPreconds());
		for (Prop p1 : a1Preconds) {
			for (Prop p2 : a2Preconds) {
				Pair<Prop> m = new Pair<Prop>(p1, p2);
				if (propMu.contains(m))
					return true;
			}
		}
		return false;
	}

	private boolean isInconsistent(Action a1, Action a2) {

		Set<Prop> a1Effects = new HashSet<>(a1.getEffects());
		Set<Prop> a2Effects = new HashSet<>(a2.getEffects());
		if (a2.getNEffects() != null)
			a1Effects.retainAll(a2.getNEffects());
		if (a1.getNEffects() != null)
			a2Effects.retainAll(a1.getNEffects());
		return !a1Effects.isEmpty() || !a2Effects.isEmpty();
	}

	private boolean isInterfering(Action a1, Action a2) {
		Set<Prop> a1Preconds = new HashSet<>(a1.getPreconds());
		Set<Prop> a2Preconds = new HashSet<>(a2.getPreconds());

		if (a2.getNEffects() != null)
			a1Preconds.retainAll(a2.getNEffects());
		if (a1.getNEffects() != null)
			a2Preconds.retainAll(a1.getNEffects());
		return !a1Preconds.isEmpty() || !a2Preconds.isEmpty();
	}

}

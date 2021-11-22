package edu.utd.swsc.graphplan;

import java.util.HashSet;
import java.util.Set;

import edu.utd.swsc.planning.problem.Action;
import edu.utd.swsc.planning.problem.Prop;

public class PropLayer extends Layer {
	private static int COUNTER = 0;
	Set<Pair<Prop>> PMu;
	Set<Prop> props;

	public PropLayer() {
		this.id = PropLayer.COUNTER;
		remainingActions = new HashSet<Action>();
		props = new HashSet<Prop>();
		COUNTER++;
	}

	public PropLayer(Set<Action> remainingActions, Set<Prop> props) {
		this.id = PropLayer.COUNTER;
		this.remainingActions = remainingActions;
		this.props = props;
		COUNTER++;
	}

	//PMu = null
	public void computeMu(Set<Pair<Action>> AMu) {
		this.PMu = new HashSet<Pair<Prop>>();
		if (this.prev != null && this.prev instanceof ActionLayer) {
			Set<Action> allProducers = ((ActionLayer) this.prev).getActions();

			for (Prop p1 : props) {
				for (Prop p2 : props) {
					if (!p1.equals(p2) && isMu(p1, p2, AMu, allProducers)) {
						Pair<Prop> p = new Pair<Prop>(p1, p2);
						this.PMu.add(p);
					}
				}
			}
		}
	}

	private boolean isMu(Prop p1, Prop p2, Set<Pair<Action>> AMu, Set<Action> allProducers) {
		Set<Action> producers1 = p1.getProducers(allProducers);
		Set<Action> producers2 = p2.getProducers(allProducers);
		for (Action a1 : producers1) {
			for (Action a2 : producers2) {
				if (!a1.equals(a2)) {
					Pair<Action> p = new Pair<Action>(a1, a2);
					if (AMu.contains(p))
						return true;
				}
			}
		}
		return false;
	}

	public ActionLayer createNext() {
//		computeMu(AMu);
		Set<Action> applicableActions = new HashSet<Action>();
		for (Action act : remainingActions) {
			if (act.isApplicable(props) && !isMuPreconds(act)) {
				applicableActions.add(act);
			}
		}
		// Update next
		ActionLayer next = new ActionLayer(applicableActions);
		next.remainingActions = new HashSet<>(remainingActions);
		next.remainingActions.removeAll(applicableActions);
		this.next = next;
		next.prev = this;
		return next;
	}

	private boolean isMuPreconds(Action act) {
		if (PMu == null || PMu.isEmpty())
			return false;
		for (Prop p1 : act.getPreconds()) {
			for (Prop p2 : act.getPreconds()) {
				if (!p1.equals(p2)) {
					Pair<Prop> p = new Pair<Prop>(p1, p2);
					if (PMu.contains(p))
						return true;
				}
			}
		}
		return false;
	}
}

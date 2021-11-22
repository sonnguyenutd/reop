package v0.icws.adaptive;

import java.util.HashSet;
import java.util.Set;

import v0.icws.Action;

public class PropLayer extends Layer {
	private static int COUNTER = 0;
	Set<String> props;

	public PropLayer() {
		this.id = PropLayer.COUNTER;
		remainingActions = new HashSet<Action>();
		props = new HashSet<String>();
		COUNTER++;
	}

	public PropLayer(Set<Action> remainingActions, Set<String> props) {
		this.id = PropLayer.COUNTER;
		this.remainingActions = remainingActions;
		this.props = props;
		COUNTER++;
	}


	public ActionLayer createNext() {
//		computeMu(AMu);
		Set<Action> applicableActions = new HashSet<Action>();
		for (Action act : remainingActions) {
			if (act.isApplicable(props)) {
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

}

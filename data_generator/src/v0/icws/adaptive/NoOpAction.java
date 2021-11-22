package v0.icws.adaptive;

import v0.icws.Action;

public class NoOpAction extends Action {
	String para;

	public NoOpAction(String prop) {
		super();
		this.name = "NOOP_" + prop;
		this.pEffects.add(prop);
		this.preconds.add(prop);
	}
}

package v8.gen.parser.childsnack;

import v5.gen.supply_chain.Action;

public class MoveTray extends Action {
	public MoveTray() {
		super();
	}

	public MoveTray(String txt) {
		String t = txt.replace("(", "").replace(")", "").trim();
		this.setName(t.replace(" ", "-"));
		String[] parts = this.getName().split("-");
		String tray = parts[1];
		String from = parts[2];
		String to = parts[3];
		addPre("at-" + tray + "-" + from);
		addNEffect("at-" + tray + "-" + from);
		addPEffect("at-" + tray + "-" + to);
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public String toString() {
		return this.getName();
	}

	@Override
	public boolean equals(Object obj) {
		return toString().equals(obj.toString());
	}
}

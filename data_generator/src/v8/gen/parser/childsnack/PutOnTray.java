package v8.gen.parser.childsnack;

import v5.gen.supply_chain.Action;

public class PutOnTray extends Action {
	public PutOnTray() {
		super();
	}

	public PutOnTray(String txt) {
		String t = txt.replace("(", "").replace(")", "").trim();
		this.setName(t.replace(" ", "-"));
		String[] parts = this.getName().split("-");
		String sandwich = parts[1];
		String tray = parts[2];
		addPre("at_kitchen_sandwich-" + sandwich);
		addPre("at-" + tray+"-kitchen");

		addPEffect("ontray-" + sandwich + "-" + tray);
		addNEffect("at_kitchen_sandwich-" + sandwich);
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

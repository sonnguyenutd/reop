package v8.gen.parser.childsnack;

import v5.gen.supply_chain.Action;

public class ServeSandwich extends Action {
	public ServeSandwich() {
		super();
	}

	public ServeSandwich(String txt) {
		String t = txt.replace("(", "").replace(")", "").trim();
		this.setName(t.replace(" ", "-"));
		String[] parts = this.getName().split("-");
		String sandwich = parts[1];
		String description = parts[2];
		String child = parts[3];
		String tray = parts[4];
		String place = parts[5];

		addPre("ontray-" + sandwich+"-"+tray);
		addPre("at-" + tray+"-"+place);
		addPre("sandwich_contents-" + sandwich + "-" + description);

		addNEffect("ontray-" + sandwich+"-"+tray);
		addPEffect("served-" + child);
		
		
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

package v8.gen.parser.childsnack;

import v5.gen.supply_chain.Action;

public class MakeSandwich extends Action {
	public MakeSandwich() {
		super();
	}

	public MakeSandwich(String txt) {
		String t = txt.replace("(", "").replace(")", "").trim();
		this.setName(t.replace(" ", "-"));
		String[] parts = this.getName().split("-");
		String sandwich = parts[1];
		String bread = parts[2];
		String content = parts[3];
		String description = parts[4];
		addPre("at_kitchen_bread-" + bread);
		addPre("at_kitchen_content-" + content);
		addPre("notexist-" + sandwich);

		addPEffect("at_kitchen_sandwich-" + sandwich);
		addPEffect("sandwich_contents-" + sandwich + "-" + description);

		addNEffect("at_kitchen_bread-" + bread);
		addNEffect("at_kitchen_content-" + content);
		addNEffect("notexist-" + sandwich);
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

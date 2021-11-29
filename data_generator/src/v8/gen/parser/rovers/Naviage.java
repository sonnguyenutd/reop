package v8.gen.parser.rovers;

import v5.gen.supply_chain.Action;

public class Naviage extends Action {
	String from;
	String to;
	String rover;
	int index;

	public Naviage() {
		super();
	}

	public Naviage(String txt) {
		String t = txt.replace("(", "").replace(")", "").trim();
		this.setName(t.replace(" ", "-"));
		String[] parts = this.getName().split("-");
		from = parts[2];
		to = parts[3];
		rover = parts[1];
		addPre("at-" + rover + "-" + from);
//		addPre("available-" + rover);
		
		addNEffect("at-" + rover + "-" + from);
		addPEffect("at-" + rover + "-" + to);
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

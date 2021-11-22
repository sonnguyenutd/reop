package v8.gen.parser.logistics;

import v5.gen.supply_chain.Action;

public class DRIVETRUCK extends Action {

	public DRIVETRUCK() {
		super();
	}

	public DRIVETRUCK(String txt) {
		String t = txt.replace("(", "").replace(")", "").trim();
		this.setName(t.replace(" ", "_"));
		String[] parts = this.getName().split("_");
		String truck = parts[1];
		String from = parts[2];
		String to = parts[3];
		addPre("at-" + truck + "-" + from);
		addNEffect("at-" + truck + "-" + from);
		addPEffect("at-" + truck + "-" + to);
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

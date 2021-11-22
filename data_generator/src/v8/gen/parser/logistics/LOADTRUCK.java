package v8.gen.parser.logistics;

import v5.gen.supply_chain.Action;

public class LOADTRUCK extends Action {

	public LOADTRUCK() {
		super();
	}

	public LOADTRUCK(String txt) {
		String t = txt.replace("(", "").replace(")", "").trim();
		this.setName(t.replace(" ", "_"));
		String[] parts = this.getName().split("_");
		String o = parts[1];
		String truck = parts[2];
		String from = parts[3];
		addPre("at-" + truck + "-" + from);
		addPre("at-" + o + "-" + from);
		addNEffect("at-" + o + "-" + from);
		addPEffect("in-" + o + "-" + truck);
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

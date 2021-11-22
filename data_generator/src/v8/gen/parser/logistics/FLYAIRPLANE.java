package v8.gen.parser.logistics;

import v5.gen.supply_chain.Action;

public class FLYAIRPLANE extends Action {

	public FLYAIRPLANE() {
		super();
	}

	public FLYAIRPLANE(String txt) {
		String t = txt.replace("(", "").replace(")", "").trim();
		this.setName(t.replace(" ", "_"));
		String[] parts = this.getName().split("_");
		String plane = parts[1];
		String from = parts[2];
		String to = parts[3];
		addPre("at-" + plane + "-" + from);
		addNEffect("at-" + plane + "-" + from);
		addPEffect("at-" + plane + "-" + to);
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

package v8.gen.parser.rovers;

import v5.gen.supply_chain.Action;

public class Drop extends Action {

	public Drop(String txt) {
		String t = txt.replace("(", "").replace(")", "").trim();
		this.setName(t.replace(" ", "-"));
		String[] parts = this.getName().split("-");
//		String rover = parts[1];
		String store = parts[2];
		addPre("full-" + store);
		addPre("empty-" + store);
		addPEffect("empty-" + store);
		addNEffect("full-" + store);
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

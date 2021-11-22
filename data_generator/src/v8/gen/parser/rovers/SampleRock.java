package v8.gen.parser.rovers;

import v5.gen.supply_chain.Action;

public class SampleRock extends Action {
	public SampleRock(String txt) {
		String t = txt.replace("(", "").replace(")", "").trim();
		this.setName(t.replace(" ", "-"));
		String[] parts = this.getName().split("-");
		String store = parts[2];
		String point = parts[3];
		String rover = parts[1];
		addPre("at-" + rover + "-" + point);
		addPre("at_rock_sample-" + point);
		addPre("empty-" + store);
		addPEffect("full-" + store);
		addPEffect("have_rock_analysis-" + rover + "-" + point);
		addNEffect("empty-" + store);
		addNEffect("at_rock_sample-" + point);
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public String toString() {
		return this.getName() ;
	}

	@Override
	public boolean equals(Object obj) {
		return toString().equals(obj.toString());
	}
}

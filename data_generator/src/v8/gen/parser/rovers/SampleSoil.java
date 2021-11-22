package v8.gen.parser.rovers;

import v5.gen.supply_chain.Action;

public class SampleSoil extends Action {
	public SampleSoil(String txt) {
		String t = txt.replace("(", "").replace(")", "").trim();
		this.setName(t.replace(" ", "-"));
		String[] parts = this.getName().split("-");
		String rover = parts[1];
		String store = parts[2];
		String point = parts[3];
		addPre("at-" + rover + "-" + point);
		addPre("at_soil_sample-" + point);
		addPre("empty-" + store);
		addPEffect("full-" + store);
		addPEffect("have_soil_analysis-" + rover + "-" + point);
		addNEffect("empty-" + store);
		addNEffect("at_soil_sample-" + point);
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

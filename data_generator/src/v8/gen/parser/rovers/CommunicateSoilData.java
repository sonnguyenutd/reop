package v8.gen.parser.rovers;

import v5.gen.supply_chain.Action;

public class CommunicateSoilData extends Action {
	public CommunicateSoilData(String txt) {
		String t = txt.replace("(", "").replace(")", "").trim();
		this.setName(t.replace(" ", "-"));
		String[] parts = this.getName().split("-");
		String rover = parts[1];
//		String lander = parts[2];
		String p = parts[3];
		String x = parts[4];
//		String y = parts[5];
		addPre("at-" + rover + "-" + x);
		addPre("have_soil_analysis-" + rover + "-" + p);
		addPEffect("communicated_soil_data-" + p);
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

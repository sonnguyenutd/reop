package v8.gen.parser.rovers;

import v5.gen.supply_chain.Action;

public class Calibrate extends Action {

	public Calibrate() {
	}

	public Calibrate(String txt) {
		String t = txt.replace("(", "").replace(")", "").trim();
		this.setName(t.replace(" ", "-"));
		String[] parts = this.getName().split("-");
		String rover = parts[1];
		String camera = parts[2];
//		String objective = parts[3]; 
		String point = parts[4];
		addPre("at-" + rover + "-" + point);
//		addPre("available-" + rover);
		addPEffect("calibrated-" + camera + "-" + rover);
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

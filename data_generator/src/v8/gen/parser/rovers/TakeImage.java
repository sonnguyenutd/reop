package v8.gen.parser.rovers;

import v5.gen.supply_chain.Action;

public class TakeImage extends Action {
	public TakeImage(String txt) {
		String t = txt.replace("(", "").replace(")", "").trim();
		this.setName(t.replace(" ", "-"));
		String[] parts = this.getName().split("-");
		String rover = parts[1];
		String point = parts[2];
		String objective = parts[3];
		String camera = parts[4];
		String mode = parts[5];
		addPre("at-" + rover + "-" + point);
		addPre("calibrated-" + camera + "-" + rover);

		addPEffect("have_image-" + rover + "-" + objective + "-" + mode);
		addNEffect("calibrated-" + camera + "-" + rover);
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

package v8.gen.parser.rovers;

import v5.gen.supply_chain.Action;

public class CommunicateImageData extends Action {

	public CommunicateImageData() {
	}


	public CommunicateImageData(String txt) {
		String t = txt.replace("(", "").replace(")", "").trim();
		this.setName(t.replace(" ", "-"));
		String[] parts = this.getName().split("-");
		String rover = parts[1];
//		String lander = parts[2];
		String objective = parts[3];
		String mode = parts[4];
		String x = parts[5];
//		String y = parts[6];
		addPre("at-" + rover + "-" + x);
		addPre("have_image-" + rover + "-" + objective + "-" + mode);
		addPEffect("communicated_image_data-" + objective + "-" + mode);
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

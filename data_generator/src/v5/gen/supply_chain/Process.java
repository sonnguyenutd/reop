package v5.gen.supply_chain;

import java.util.ArrayList;
import java.util.List;

public class Process {
	Part part;
	List<Action> steps;

	public void setPart(Part part) {
		this.part = part;
	}

	public Part getPart() {
		return part;
	}

	public List<Action> getSteps() {
		return steps;
	}

	public void setSteps(List<Action> steps) {
		this.steps = steps;
	}

	public Process(Part p) {
		steps = new ArrayList<Action>();
		this.part = p;
	}

	public void addStep(Action act) {
		this.steps.add(act);
	}

	@Override
	public String toString() {
		String result = "";
		for (Action s : steps) {
			result += (s.name + "(" + s.getPreconds().size() + "-" + s.getEffects().size() + ")" + " --> ");
		}
		return result;

	}
}

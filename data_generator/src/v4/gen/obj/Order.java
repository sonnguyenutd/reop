package v4.gen.obj;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import v4.gen.generator.ObjectGenerator;
import v3.gen.obj.PT;

public class Order extends PT {
	String destination;
	Collection<Frame> frames;

	public Order(String destination) {
		this.destination = destination;
	}

	public void setFrames(Collection<Frame> frames) {
		this.frames = frames;
	}

	@Override
	public Set<String> getActions() {
		ObjectGenerator.predicates.add("(order-done-" + getId() + ")");
		Set<String> result = new HashSet<String>();
		result.addAll(genOrderFrame());
//		Manufacturing-status-update
		result.addAll(genManufacturingStatusUpdate());
		return result;
	}

	private Collection<String> genManufacturingStatusUpdate() {
		Set<String> result = new HashSet<String>();
		for (Frame f : frames) {
			String a = "(:action manufacturing-status-update-" + getId() + "-" + f.getId() + "\n";
			a += "\t:precondition  (and" + "\n";
			a += "\t\t(assembled-" + f.getId() + ")" + "\n";
			a += "\t\t(frame-ordered-" + getId() + "-" + f.getId() + ")" + "\n";
			a += "\t)\n";

			a += "\t:effect  (and" + "\n";
			a += "\t\t(order-handled-" + getId() + ")" + "\n";
			a += "\t)\n";
			a += ")";

			ObjectGenerator.predicates.add(("\t\t(assembled-" + f.getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(frame-ordered-" + getId() + "-" + f.getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(order-handled-" + getId() + ")").trim());

			result.add(a);
		}
		return result;
	}

	private Collection<String> genOrderFrame() {
		Set<String> result = new HashSet<String>();
		for (Frame f : frames) {
			String a = "(:action order-frame-tube-" + getId() + "-" + f.getId() + "\n";
			a += "\t:precondition  (and" + "\n";
			a += "\t\t(order-requested-" + getId() + ")" + "\n";
			a += "\t)\n";

			a += "\t:effect  (and" + "\n";
			a += "\t\t(frame-ordered-" + getId() + "-" + f.getId() + ")" + "\n";
			a += "\t)\n";
			a += ")";

			ObjectGenerator.predicates.add(("\t\t(order-requested-" + getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(frame-ordered-" + getId() + "-" + f.getId() + ")").trim());
			result.add(a);
		}
		return result;
	}

	@Override
	public Set<String> getParentActions1() {
		Set<String> result = new HashSet<String>();
		result.addAll(genOrderFrame());
		result.addAll(genManufacturingStatusUpdate());
		return result;
	}

	@Override
	public Set<String> getParentActions2() {
		return getParentActions1();
	}

	@Override
	public String getState() {
		return "(order-requested-" + getId() + ")\n" + "\t\t(destination-" + destination + "-" + getId() + ")";
	}
	
	public String getDone() {
		return "(order-done-" + getId() + ")";
	}

}

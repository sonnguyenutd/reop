package v3.gen.obj;

import java.util.HashSet;
import java.util.Set;

import v3.gen.generator.ObjectGenerator;

public class ParkingLot extends PT {
	String loc;

	private ParkingLot() {
	}

	public ParkingLot(String loc) {
		this.loc = loc;
		this.id = "PARKING-LOT-" + loc;
	}

	@Override
	public Set<String> getActions() {
		return new HashSet<>();
	}

	@Override
	public String getState() {
		ObjectGenerator.predicates.add("(loc-" + getId() + "-" + loc + ")");
		return "(loc-" + getId() + "-" + loc + ")";
	}

	@Override
	public int hashCode() {
		return loc.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return this.toString().equals(obj.toString());
	}

	@Override
	public String toString() {
		return "PARKING-LOT-" + loc;
	}

	@Override
	public Set<String> getParentActions1() {
		return getActions();
	}

	@Override
	public Set<String> getParentActions2() {
		return getActions();
	}
}

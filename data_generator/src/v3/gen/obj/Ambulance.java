package v3.gen.obj;

import java.util.Collection;
import java.util.Set;

import v3.gen.generator.ObjectGenerator;

public class Ambulance extends Car {

	public Ambulance(String loc, Collection<Route> routes, Collection<String> locs, Collection<ParkingLot> lots) {
		super(loc, routes, locs, lots);
	}

	@Override
	public Set<String> getActions() {
		Set<String> result = super.getActions();
		for (String l : getLocs()) {
			String a = "(:action ambulance-at-accident-report-" + getId() + "-" + l + "\n";
			a += "\t:precondition  (and" + "\n";
			a += "\t\t(loc-" + getId() + "-" + l + ")\n";
			a += "\t\t(accident-" + l + ")\n";
			a += "\t\t(accident-reported-" + l + ")\n";
			a += "\t)\n";

			a += "\t:effect  (and" + "\n";
			a += "\t\t(ambulance-at-accident)\n";
			a += "\t)\n";
			a += ")";
			result.add(a);

			ObjectGenerator.predicates.add(("\t\t(loc-" + getId() + "-" + l + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(accident-" + l + ")\n").trim());
			ObjectGenerator.predicates.add(("\t\t(accident-reported-" + l + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(ambulance-at-accident)\n").trim());

		}
		return result;
	}
	
	@Override
	public Set<String> getParentActions1() {
		Set<String> result = super.getParentActions1();
		for (String l : getLocs()) {
			String a = "(:action ambulance-at-accident-report-" + getId() + "-" + l + "\n";
			a += "\t:precondition  (and" + "\n";
			a += "\t\t(loc-" + getId() + "-" + l + ")\n";
			a += "\t\t(accident-" + l + ")\n";
			a += "\t\t(accident-reported-" + l + ")\n";
			a += "\t)\n";

			a += "\t:effect  (and" + "\n";
			a += "\t\t(ambulance-at-accident)\n";
			a += "\t)\n";
			a += ")";
			result.add(a);

			ObjectGenerator.predicates.add(("\t\t(loc-" + getId() + "-" + l + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(accident-" + l + ")\n").trim());
			ObjectGenerator.predicates.add(("\t\t(accident-reported-" + l + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(ambulance-at-accident)\n").trim());

		}
		return result;
	}
	
	@Override
	public Set<String> getParentActions2() {
		Set<String> result = super.getParentActions2();
		for (String l : getLocs()) {
			String a = "(:action ambulance-at-accident-report-" + getId() + "-" + l + "\n";
			a += "\t:precondition  (and" + "\n";
			a += "\t\t(loc-" + getId() + "-" + l + ")\n";
			a += "\t\t(accident-" + l + ")\n";
			a += "\t\t(accident-reported-" + l + ")\n";
			a += "\t)\n";

			a += "\t:effect  (and" + "\n";
			a += "\t\t(ambulance-at-accident)\n";
			a += "\t)\n";
			a += ")";
			result.add(a);

			ObjectGenerator.predicates.add(("\t\t(loc-" + getId() + "-" + l + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(accident-" + l + ")\n").trim());
			ObjectGenerator.predicates.add(("\t\t(accident-reported-" + l + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(ambulance-at-accident)\n").trim());

		}
		return result;
	}
}

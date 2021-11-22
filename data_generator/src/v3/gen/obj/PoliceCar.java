package v3.gen.obj;

import java.util.Collection;
import java.util.Set;

import v3.gen.generator.ObjectGenerator;

public class PoliceCar extends Car{
	public PoliceCar(String loc, Collection<Route> routes, Collection<String> locs, Collection<ParkingLot> lots) {
		super(loc, routes, locs, lots);
	}

	@Override
	public Set<String> getActions() {
		Set<String> result = super.getActions();
		for (String l : locs) {
			String a = "(:action police-at-accident-report-" + getId() + "-" + l + "\n";
			a += "\t:precondition  (and" + "\n";
			a += "\t\t(loc-" + getId() + "-" + l + ")\n";
			a += "\t\t(accident-" + l + ")\n";
			a += "\t\t(accident-reported-" + l + ")\n";
			a += "\t)\n";

			a += "\t:effect  (and" + "\n";
			a += "\t\t(police-at-accident)\n";
			a += "\t)\n";
			a += ")";
			result.add(a);
			
			ObjectGenerator.predicates.add(("\t\t(loc-" + getId() + "-" + l + ")\n").trim());
			ObjectGenerator.predicates.add(("\t\t(accident-" + l + ")\n").trim());
			ObjectGenerator.predicates.add(("\t\t(accident-reported-" + l + ")\n").trim());
			ObjectGenerator.predicates.add(("\t\t(police-at-accident)\n").trim());
		}
		return result;
	}
	
	@Override
	public Set<String> getParentActions1() {
		Set<String> result = super.getParentActions1();
		for (String l : locs) {
			String a = "(:action police-at-accident-report-" + getId() + "-" + l + "\n";
			a += "\t:precondition  (and" + "\n";
			a += "\t\t(loc-" + getId() + "-" + l + ")\n";
			a += "\t\t(accident-" + l + ")\n";
			a += "\t\t(accident-reported-" + l + ")\n";
			a += "\t)\n";

			a += "\t:effect  (and" + "\n";
			a += "\t\t(police-at-accident)\n";
			a += "\t)\n";
			a += ")";
			result.add(a);
			
			ObjectGenerator.predicates.add(("\t\t(loc-" + getId() + "-" + l + ")\n").trim());
			ObjectGenerator.predicates.add(("\t\t(accident-" + l + ")\n").trim());
			ObjectGenerator.predicates.add(("\t\t(accident-reported-" + l + ")\n").trim());
			ObjectGenerator.predicates.add(("\t\t(police-at-accident)\n").trim());
		}
		return result;
	}
	
	@Override
	public Set<String> getParentActions2() {
		Set<String> result = super.getParentActions2();
		for (String l : locs) {
			String a = "(:action police-at-accident-report-" + getId() + "-" + l + "\n";
			a += "\t:precondition  (and" + "\n";
			a += "\t\t(loc-" + getId() + "-" + l + ")\n";
			a += "\t\t(accident-" + l + ")\n";
			a += "\t\t(accident-reported-" + l + ")\n";
			a += "\t)\n";

			a += "\t:effect  (and" + "\n";
			a += "\t\t(police-at-accident)\n";
			a += "\t)\n";
			a += ")";
			result.add(a);
			
			ObjectGenerator.predicates.add(("\t\t(loc-" + getId() + "-" + l + ")\n").trim());
			ObjectGenerator.predicates.add(("\t\t(accident-" + l + ")\n").trim());
			ObjectGenerator.predicates.add(("\t\t(accident-reported-" + l + ")\n").trim());
			ObjectGenerator.predicates.add(("\t\t(police-at-accident)\n").trim());
		}
		return result;
	}
}

package v3.gen.obj;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import v3.gen.generator.ObjectGenerator;

public class Drone extends PT {
	String loc;
	Set<String> locs;

	private Drone() {
	}

	public Drone(Collection<String> locs) {
		this.locs = new HashSet<String>(locs);
	}

	public void setLoc(String loc) {
		this.loc = loc;
	}

	public String getLoc() {
		return loc;
	}

	@Override
	public Set<String> getActions() {
		Set<String> result = new HashSet<String>();
		// flight
		result.addAll(genFlight());
		// collect-accident-info
		result.addAll(genCollectAccidentInfo());
		// report-accident-info
//		result.addAll(genReportAccidentInfo());

		// send current location
//		result.addAll(genSendCurrLoc());
		return result;
	}

//	private Collection<String> genReportAccidentInfo() {
//		Set<String> result = new HashSet<>();
//		for (String l : locs) {
//			String a = "(:action report-accident-info-" + getId() + "-" + l + "\n";
//			a += "\t:precondition  (and" + "\n";
//			a += "\t\t(accident-info-collected-" + getId() + "-" + l + ")" + "\n";
//			a += "\t)\n";
//
//			a += "\t:effect  (and" + "\n";
//			a += "\t\t(accident-reported-" + l + ")" + "\n";
//			a += "\t)\n";
//			a += ")";
//			result.add(a);
//
//			ObjectGenerator.predicates.add(("\t\t(accident-info-collected-" + getId() + "-" + l + ")").trim());
//			ObjectGenerator.predicates.add(("\t\t(accident-reported-" + l + ")").trim());
//		}
//		return result;
//	}

	private Collection<String> genCollectAccidentInfo() {
		Set<String> result = new HashSet<>();
		for (String l : locs) {
			String a = "(:action collect-accident-info-" + getId() + "-" + l + "\n";
			a += "\t:precondition  (and" + "\n";
			a += "\t\t(loc-" + getId() + "-" + l + ")" + "\n";
			a += "\t\t(accident-" + l + ")" + "\n";
			a += "\t)\n";

			a += "\t:effect  (and" + "\n";
			a += "\t\t(accident-info-collected-" + getId() + "-" + l + ")" + "\n";
			a += "\t\t(accident-reported-" + l + ")" + "\n";
			a += "\t)\n";
			a += ")";
			result.add(a);

			ObjectGenerator.predicates.add(("\t\t(loc-" + getId() + "-" + l + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(accident-" + l + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(accident-reported-" + l + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(accident-info-collected-" + getId() + "-" + l + ")").trim());
		}
		return result;
	}

	@Override
	public String getState() {
		ObjectGenerator.predicates.add("(loc-" + getId() + "-" + loc + ")");
		return "(loc-" + getId() + "-" + loc + ")";
	}

	private Set<String> genFlight() {
		Set<String> result = new HashSet<>();
		for (String l1 : locs) {
			for (String l2 : locs) {
				if (!l1.equals(l2)) {
					result.add(createAMove(l1, l2));
				}
			}
		}
		return result;
	}

	private String createAMove(String l1, String l2) {
		String result = "(:action flight-" + getId() + "-" + l1 + "-" + l2 + "\n";
		result += "\t:precondition  (and\n";
		result += "\t\t(loc-" + getId() + "-" + l1 + ")\n";
//		result += "\t\t(current-loc-sent-" + getId() + "-" + l1 + ")\n";
		result += "\t)\n";

		result += "\t:effect  (and\n";
		result += "\t\t(not (loc-" + getId() + "-" + l1 + "))\n";
		result += "\t\t(loc-" + getId() + "-" + l2 + ")" + "\n";
//		result += "\t\t(movement-reported-" + getId() + "-" + l1 + "-" + l2 + ")\n";
		result += "\t)\n";
		result += ")";

		ObjectGenerator.predicates.add(("\t\t(loc-" + getId() + "-" + l1 + ")\n").trim());
		ObjectGenerator.predicates.add(("\t\t(loc-" + getId() + "-" + l2 + ")").trim());
//		ObjectGenerator.predicates.add(("\t\t(current-loc-sent-" + getId() + "-" + l1 + ")\n").trim());
//		ObjectGenerator.predicates.add(("\t\t(movement-reported-" + getId() + "-" + l1 + "-" + l2 + ")\n").trim());
		return result;
	}

//	private Collection<String> genSendCurrLoc() {
//		Set<String> result = new HashSet<>();
//		for (String l : locs) {
//			String a = "(:action send-curr-location-" + getId() + "-" + l + "\n";
//			a += "\t:precondition  (and" + "\n";
//			a += "\t\t(loc-" + getId() + "-" + l + ")" + "\n";
//			a += "\t)\n";
//
//			a += "\t:effect  (and" + "\n";
//			a += "\t\t(current-loc-sent-" + getId() + "-" + l + ")" + "\n";
//			a += "\t)\n";
//			a += ")";
//			
//			ObjectGenerator.predicates.add(("\t\t(loc-" + getId() + "-" + l + ")").trim());
//			ObjectGenerator.predicates.add(("\t\t(current-loc-sent-" + getId() + "-" + l + ")").trim());
//			result.add(a);
//		}
//		return result;
//	}

	@Override
	public Set<String> getParentActions1() {
		return getActions();
	}

	@Override
	public Set<String> getParentActions2() {
		Set<String> result = new HashSet<String>();
		// move
		for (String l1 : locs) {
			Set<String> tos = new HashSet<>(locs);
			tos.remove(l1);
			result.add(createA21(l1, tos));
		}
		result.addAll(genCollectAccidentInfo());
		return result;
	}

	private String createA21(String from, Set<String> tos) {
		String result = "(:action a21-" + getId() + "-" + from + "\n";
		result += "\t:precondition  (and\n";
		result += "\t\t(loc-" + getId() + "-" + from + ")\n";
		result += "\t)\n";

		result += "\t:effect  (and\n";
		result += "\t\t(not (loc-" + getId() + "-" + from + "))\n";
		for (String to : tos) {
			result += "\t\t(loc-" + getId() + "-" + to + ")" + "\n";
			ObjectGenerator.predicates.add(("\t\t(loc-" + getId() + "-" + to + ")").trim());
		}
//		result += "\t\t(movement-reported-" + getId() + "-" + l1 + "-" + l2 + ")\n";
		result += "\t)\n";
		result += ")";

		ObjectGenerator.predicates.add(("\t\t(loc-" + getId() + "-" + from + ")\n").trim());
//		ObjectGenerator.predicates.add(("\t\t(current-loc-sent-" + getId() + "-" + l1 + ")\n").trim());
//		ObjectGenerator.predicates.add(("\t\t(movement-reported-" + getId() + "-" + l1 + "-" + l2 + ")\n").trim());

		return result;
	}

}

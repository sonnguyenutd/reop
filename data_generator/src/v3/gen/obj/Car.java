package v3.gen.obj;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import v3.gen.generator.ObjectGenerator;

public class Car extends PT {
	String loc;
	Set<Route> routes;
	Set<String> locs;
	Set<ParkingLot> lots;

	public Car(String loc, Collection<Route> routes, Collection<String> locs, Collection<ParkingLot> lots) {
		this.loc = loc;
		this.routes = new HashSet<Route>(routes);
		this.locs = new HashSet<String>(locs);
		this.lots = new HashSet<ParkingLot>(lots);
	}

	public void setLoc(String loc) {
		this.loc = loc;
	}

	public Set<String> getLocs() {
		return locs;
	}

	public void setLocs(Set<String> locs) {
		this.locs = locs;
	}

	@Override
	public Set<String> getActions() {
		Set<String> result = new HashSet<String>();
		// move
		Set<String> moves = genMove();
		result.addAll(moves);
		// request-traffic-info
		result.addAll(genRequestTrafficInfo());
		// report-movement
		result.addAll(genReportMovement());
		// send-curr-location
		result.addAll(genSendCurrLoc());
		// request-parking-info
		result.addAll(genRequestParkingInfo());
		// reserve-parking-slot
		result.addAll(genReserveParkingInfo());
		// send-vehicle-info
		result.addAll(genSendVihicleInfo());
		// park
		result.addAll(genPark());

		return result;
	}

	public Set<String> getParentActions1() {
		Set<String> result = new HashSet<String>();
		// move
		Set<String> a1s = genA1();
		result.addAll(a1s);
		// request-traffic-info
		result.addAll(genA2());
		// park
		result.addAll(genPark());

		return result;
	}

	@Override
	public Set<String> getParentActions2() {
		Set<String> result = new HashSet<String>();
		// move
		Set<String> a2s = genA21();
		result.addAll(a2s);
		// request-traffic-info
		result.addAll(genA2());
		// park
		result.addAll(genPark());

		return result;
	}

	private Collection<String> genSendVihicleInfo() {
		Set<String> result = new HashSet<>();
		for (ParkingLot lot : lots) {
			String a = "(:action send-vehicle-info-" + getId() + "-" + lot.getId() + "\n";
			a += "\t:precondition  (and" + "\n";
			a += "\t\t(parking-slot-confirmed-" + getId() + "-" + lot.getId() + ")" + "\n";
			a += "\t)\n";

			a += "\t:effect  (and" + "\n";
			a += "\t\t(vehicle-info-sent-" + getId() + "-" + lot.getId() + ")" + "\n";
			a += "\t)\n";
			a += ")";
			result.add(a);

			ObjectGenerator.predicates.add(("\t\t(parking-slot-confirmed-" + getId() + "-" + lot.getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(vehicle-info-sent-" + getId() + "-" + lot.getId() + ")").trim());
		}
		return result;
	}

	private Collection<String> genPark() {
		Set<String> result = new HashSet<>();
		for (ParkingLot lot : lots) {
			for (String l : locs) {
				String p = createPark(l, lot);
				result.add(p);
			}
		}
		return result;
	}

	private String createPark(String l, ParkingLot lot) {
		String a = "(:action send-curr-location-" + getId() + "-" + l + "\n";
		a += "\t:precondition  (and" + "\n";
		a += "\t\t(loc-" + getId() + "-" + l + ")" + "\n";
		a += "\t\t(loc-" + lot.getId() + "-" + l + ")" + "\n";
		a += "\t\t(parking-slot-confirmed-" + getId() + "-" + lot.getId() + ")" + "\n";
		a += "\t)\n";

		a += "\t:effect  (and" + "\n";
		a += "\t\t(parked-" + getId() + ")" + "\n";
		a += "\t)\n";
		a += ")";

		ObjectGenerator.predicates.add(("\t\t(loc-" + getId() + "-" + l + ")").trim());
		ObjectGenerator.predicates.add(("\t\t(loc-" + lot.getId() + "-" + l + ")").trim());
		ObjectGenerator.predicates.add(("\t\t(parking-slot-confirmed-" + getId() + "-" + lot.getId() + ")").trim());
		ObjectGenerator.predicates.add(("\t\t(parked-" + getId() + ")").trim());

		return a;
	}

	private Collection<String> genReserveParkingInfo() {
		Set<String> result = new HashSet<>();
		for (ParkingLot lot : lots) {
			String a = "(:action reserve-parking-slot-" + getId() + "-" + lot.getId() + "\n";
			a += "\t:precondition  (and" + "\n";
			a += "\t\t(parking-info-sent-" + getId() + "-" + lot.getId() + ")" + "\n";
			a += "\t)\n";

			a += "\t:effect  (and" + "\n";
			a += "\t\t(parking-slot-reserved-" + getId() + "-" + lot.getId() + ")" + "\n";
			a += "\t)\n";
			a += ")";

			ObjectGenerator.predicates.add(("\t\t(parking-info-sent-" + getId() + "-" + lot.getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(parking-slot-reserved-" + getId() + "-" + lot.getId() + ")").trim());
			result.add(a);
		}
		return result;
	}

	private Collection<String> genRequestParkingInfo() {
		Set<String> result = new HashSet<>();
		for (ParkingLot lot : lots) {
			String a = "(:action request-parking-info-" + getId() + "-" + lot.getId() + "\n";
			a += "\t:precondition  (and" + "\n";

			a += "\t)\n";

			a += "\t:effect  (and" + "\n";
			a += "\t\t(parking-info-requested-" + getId() + "-" + lot.getId() + ")" + "\n";
			a += "\t)\n";
			a += ")";

			ObjectGenerator.predicates.add(("\t\t(parking-info-requested-" + getId() + "-" + lot.getId() + ")").trim());
			result.add(a);
		}
		return result;
	}

	private Collection<String> genSendCurrLoc() {
		Set<String> result = new HashSet<>();
		for (String l : locs) {
			String a = "(:action send-curr-location-" + getId() + "-" + l + "\n";
			a += "\t:precondition  (and" + "\n";
			a += "\t\t(loc-" + getId() + "-" + l + ")" + "\n";
			a += "\t)\n";

			a += "\t:effect  (and" + "\n";
			a += "\t\t(current-loc-sent-" + getId() + "-" + l + ")" + "\n";
			a += "\t)\n";
			a += ")";

			ObjectGenerator.predicates.add(("\t\t(loc-" + getId() + "-" + l + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(current-loc-sent-" + getId() + "-" + l + ")").trim());
			result.add(a);
		}
		return result;
	}

	private Collection<String> genReportMovement() {
		Set<String> result = new HashSet<>();
		for (Route r : routes) {
			String a = "(:action report-movement-" + getId() + "-" + r.getFrom() + "-" + r.getTo() + "\n";
			a += "\t:precondition  (and" + "\n";
			a += "\t\t(loc-" + getId() + "-" + r.getFrom() + ")" + "\n";
			a += "\t)\n";

			a += "\t:effect  (and" + "\n";
			a += "\t\t(movement-reported-" + getId() + "-" + r.getFrom() + "-" + r.getTo() + ")\n";
			a += "\t)\n";
			a += ")";

			ObjectGenerator.predicates.add(("\t\t(loc-" + getId() + "-" + r.getFrom() + ")").trim());
			ObjectGenerator.predicates
					.add(("\t\t(movement-reported-" + getId() + "-" + r.getFrom() + "-" + r.getTo() + ")\n").trim());
			result.add(a);
		}
		return result;
	}

	private Collection<String> genRequestTrafficInfo() {
		Set<String> result = new HashSet<>();
		for (String l : locs) {
			String a = "(:action request-traffic-info-" + getId() + "-" + l + "\n";
			a += "\t:precondition  (and" + "\n";
			a += "\t\t(loc-" + getId() + "-" + l + ")" + "\n";
			a += "\t)\n";

			a += "\t:effect  (and" + "\n";
			a += "\t\t(traffic-info-requested-" + getId() + "-" + l + ")" + "\n";
			a += "\t)\n";
			a += ")";

			ObjectGenerator.predicates.add(("\t\t(loc-" + getId() + "-" + l + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(traffic-info-requested-" + getId() + "-" + l + ")").trim());
			result.add(a);
		}
		return result;
	}

	private Set<String> genMove() {
		Set<String> result = new HashSet<>();
		for (Route r : routes) {
			result.add(createAMove(r));
		}
		return result;
	}

	private String createAMove(Route r) {
		String result = "(:action move-" + getId() + "-" + r.getFrom() + "-" + r.getTo() + "\n";
		result += "\t:precondition  (and\n";
		result += "\t\t(loc-" + getId() + "-" + r.getFrom() + ")\n";
		result += "\t\t(traffic-info-sent-" + getId() + "-" + r.getFrom() + ")\n";
		result += "\t\t(current-loc-sent-" + getId() + "-" + r.getFrom() + ")" + "\n";
		result += "\t)\n";

		result += "\t:effect  (and\n";
		result += "\t\t(not (loc-" + getId() + "-" + r.getFrom() + "))" + "\n";
		result += "\t\t(loc-" + getId() + "-" + r.getTo() + ")" + "\n";
		result += "\t\t(movement-reported-" + getId() + "-" + r.getFrom() + "-" + r.getTo() + ")" + "\n";
		result += "\t)\n";
		result += ")";

		ObjectGenerator.predicates.add(("\t\t(loc-" + getId() + "-" + r.getFrom() + ")\n").trim());
		ObjectGenerator.predicates.add(("\t\t(traffic-info-sent-" + getId() + "-" + r.getFrom() + ")\n").trim());
		ObjectGenerator.predicates.add(("\t\t(current-loc-sent-" + getId() + "-" + r.getFrom() + ")").trim());
		ObjectGenerator.predicates.add(("\t\t(loc-" + getId() + "-" + r.getTo() + ")\n").trim());
		ObjectGenerator.predicates
				.add(("\t\t(movement-reported-" + getId() + "-" + r.getFrom() + "-" + r.getTo() + ")").trim());
		return result;
	}

	@Override
	public String getState() {
		ObjectGenerator.predicates.add("(loc-" + getId() + "-" + loc + ")");
		return "(loc-" + getId() + "-" + loc + ")";
	}

	private Collection<String> genA2() {
		Set<String> result = new HashSet<>();
		for (ParkingLot lot : lots) {
			String a = "(:action a2-" + getId() + "-" + lot.getId() + "\n";
			a += "\t:precondition  (and" + "\n";
			a += "\t)\n";

			a += "\t:effect  (and" + "\n";
			a += "\t\t(parking-slot-reserved-" + getId() + "-" + lot.getId() + ")" + "\n";
			a += "\t\t(parking-info-requested-" + getId() + "-" + lot.getId() + ")" + "\n";
			a += "\t\t(parking-info-sent-" + getId() + "-" + lot.getId() + ")" + "\n";
			a += "\t\t(parking-slot-confirmed-" + getId() + ")" + "\n";
			a += "\t\t(parking-info-updated-" + lot.getId() + ")" + "\n";
			a += "\t\t(vehicle-info-sent-" + getId() + "-" + lot.getId() + ")" + "\n";
			a += "\t)\n";
			a += ")";

			ObjectGenerator.predicates.add(("\t\t(parking-slot-reserved-" + getId() + "-" + lot.getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(parking-info-requested-" + getId() + "-" + lot.getId() + ")").trim());

			ObjectGenerator.predicates.add(("\t\t(parking-info-sent-" + getId() + "-" + lot.getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(parking-slot-confirmed-" + getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(parking-info-updated-" + lot.getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(vehicle-info-sent-" + getId() + "-" + lot.getId() + ")").trim());
			result.add(a);
		}
		return result;
	}

	private Set<String> genA1() {
		Set<String> result = new HashSet<>();
		for (Route r : routes) {
			result.add(createA1(r));
		}
		return result;
	}

	private String createA1(Route r) {
		String result = "(:action a1-" + getId() + "-" + r.getFrom() + "-" + r.getTo() + "\n";
		result += "\t:precondition  (and\n";
		result += "\t\t(loc-" + getId() + "-" + r.getFrom() + ")\n";
		result += "\t)\n";

		result += "\t:effect  (and\n";

		result += "\t\t(traffic-info-requested-" + getId() + "-" + r.getFrom() + ")\n";
		result += "\t\t(traffic-info-sent-" + getId() + "-" + r.getFrom() + ")\n";
		result += "\t\t(current-loc-sent-" + getId() + "-" + r.getFrom() + ")" + "\n";

		result += "\t\t(not (loc-" + getId() + "-" + r.getFrom() + "))" + "\n";
		result += "\t\t(loc-" + getId() + "-" + r.getTo() + ")" + "\n";
		result += "\t\t(movement-reported-" + getId() + "-" + r.getFrom() + "-" + r.getTo() + ")" + "\n";

		result += "\t)\n";
		result += ")";

		ObjectGenerator.predicates.add(("\t\t(loc-" + getId() + "-" + r.getFrom() + ")\n").trim());
		ObjectGenerator.predicates.add(("\t\t(traffic-info-requested-" + getId() + "-" + r.getFrom() + ")\n").trim());
		ObjectGenerator.predicates.add(("\t\t(traffic-info-sent-" + getId() + "-" + r.getFrom() + ")\n").trim());
		ObjectGenerator.predicates.add(("\t\t(current-loc-sent-" + getId() + "-" + r.getFrom() + ")").trim());
		ObjectGenerator.predicates.add(("\t\t(loc-" + getId() + "-" + r.getTo() + ")\n").trim());
		ObjectGenerator.predicates
				.add(("\t\t(movement-reported-" + getId() + "-" + r.getFrom() + "-" + r.getTo() + ")").trim());

		return result;
	}

	private Set<String> genA21() {
		Set<String> result = new HashSet<>();
		Map<String, Set<String>> from_tos = new HashMap<>();
		for (Route r : routes) {
			Set<String> tos = from_tos.get(r.from);
			if (tos == null)
				tos = new HashSet<String>();
			tos.add(r.to);
			from_tos.put(r.from, tos);
		}

		for (String from : from_tos.keySet()) {
			Set<String> tos = from_tos.get(from);
			String a = createA21(from, tos);
			result.add(a);
		}
		return result;
	}

	private String createA21(String from, Set<String> tos) {
		String result = "(:action a21-" + getId() + "-" + from + "\n";
		result += "\t:precondition  (and\n";
		result += "\t\t(loc-" + getId() + "-" + from + ")\n";
		result += "\t)\n";

		result += "\t:effect  (and\n";
		
		result += "\t\t(traffic-info-requested-" + getId() + "-" + from + ")\n";
		result += "\t\t(traffic-info-sent-" + getId() + "-" + from + ")\n";
		result += "\t\t(current-loc-sent-" + getId() + "-" + from + ")" + "\n";
		result += "\t\t(not (loc-" + getId() + "-" + from + "))" + "\n";
		for (String to : tos) {
			result += "\t\t(loc-" + getId() + "-" + to + ")" + "\n";
			result += "\t\t(movement-reported-" + getId() + "-" + from + "-" + to + ")" + "\n";
			ObjectGenerator.predicates.add(("\t\t(loc-" + getId() + "-" + to + ")\n").trim());
			ObjectGenerator.predicates.add(("\t\t(movement-reported-" + getId() + "-" + from + "-" + to + ")").trim());
		}
		ObjectGenerator.predicates.add(("\t\t(loc-" + getId() + "-" + from + ")\n").trim());
		ObjectGenerator.predicates.add(("\t\t(traffic-info-requested-" + getId() + "-" + from + ")\n").trim());
		ObjectGenerator.predicates.add(("\t\t(traffic-info-sent-" + getId() + "-" + from + ")\n").trim());
		ObjectGenerator.predicates.add(("\t\t(current-loc-sent-" + getId() + "-" + from + ")").trim());
		
		result += "\t)\n";
		result += ")";

		return result;
	}
}

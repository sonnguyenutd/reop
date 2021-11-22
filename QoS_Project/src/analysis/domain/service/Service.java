package analysis.domain.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import analysis.domain.service.grounding.GroundedFunct;
import analysis.domain.service.grounding.GroundedPredicate;
import analysis.domain.service.obj.Obj;

public class Service {
	public Provider provider;
	public String name;
	public List<Predicate> preconds;
	public List<Predicate> pEffects;
	public List<Predicate> nEffects;
	public List<NumericCondition> conditions;
	public List<NumericChange> changes;
	public Set<Function> functs;

	public List<Obj> paras;
	public Map<String, Obj> paras_map;

	public static void main(String[] args) {
		List<Service> ops = allServices();
		for (Service service : ops) {
			System.out.println(service);
		}
	}

	public void addCond(NumericCondition pre) {
		this.functs.addAll(pre.participants);
		this.conditions.add(pre);
	}

	public void addChange(NumericChange eff) {
		this.functs.addAll(eff.participants);
		this.changes.add(eff);
	}

	public boolean canProduce(GroundedFunct p) {
//		System.out.println("===>" + p);
		// TODO check if the service can really produce p by check the
		// precondition/condition also.
		if (p instanceof GroundedPredicate) {
			for (Predicate eff : pEffects) {
//				System.out.println("-->" + eff);
				if (eff.canBeGroundedTo(p)) {
//					System.out.println("HERE");
					return true;
				}
			}
		} else {
			for (NumericChange change : changes) {
				// Just need to check the first participant
				if (!change.participants.isEmpty()) {
					Function f = change.participants.get(0);
					if (f.canBeGroundedTo(p))
						return true;
				}
			}
		}
		return false;
	}

	public void setParas(ArrayList<Obj> paras) {
		paras_map = new HashMap<>();
		for (Obj obj : paras) {
			String n = obj.name.trim();
			if (!n.startsWith("?"))
				n = "?" + n;
			Obj variable = new Obj(obj.type, n);
			paras_map.put(n, variable);
			this.paras.add(variable);
		}
	}

	public Service(Provider provider, String name) {
		this.name = name;
		preconds = new ArrayList<Predicate>();
		pEffects = new ArrayList<Predicate>();
		nEffects = new ArrayList<Predicate>();
		paras = new ArrayList<>();
		conditions = new ArrayList<>();
		changes = new ArrayList<>();
		this.provider = provider;
		functs = new HashSet<>();
	}

	public void addPrecond(Predicate pre) {
		this.preconds.add(pre);
	}

	public void addPEffect(Predicate eff) {
		this.pEffects.add(eff);
	}

	public void addNEffect(Predicate eff) {
		this.nEffects.add(eff);
	}

	public Service(String name, List<Predicate> preconds, List<Predicate> pEffects, List<Predicate> nEffects) {
		this.name = name;
		this.preconds = preconds;
		this.pEffects = pEffects;
		this.nEffects = nEffects;
		conditions = new ArrayList<>();
		changes = new ArrayList<>();
		functs = new HashSet<>();
	}

	public Service(String name) {
		this.name = name;
		preconds = new ArrayList<Predicate>();
		pEffects = new ArrayList<Predicate>();
		nEffects = new ArrayList<Predicate>();
		paras = new ArrayList<>();
		conditions = new ArrayList<>();
		changes = new ArrayList<>();
		functs = new HashSet<>();
	}

	public boolean isDependentOf(Service other) {
		List<Predicate> othersEffects = new ArrayList<>(other.pEffects);
		othersEffects.retainAll(this.preconds);
		return !othersEffects.isEmpty();
	}

	public List<Predicate> getConnectors(Service dependent) {
		List<Predicate> result = new ArrayList<Predicate>();
		for (Predicate ef : this.pEffects) {
			for (Predicate pre : dependent.preconds) {
				if (ef.compareType(pre))
					result.add(pre);
			}
		}
		return result;
	}

	@Override
	public String toString() {
		String result = name + "\n\t@" + paras + "\n\t*" + preconds + "\n\t#" + conditions + "\n\t+" + pEffects
				+ "\n\t^" + changes + "\n\t-" + nEffects;
		return result;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	// FOR TESTING
	public static String TYPE_CAR = "car";
	public static String TYPE_ROBOT = "robot";
	public static String TYPE_LOC = "location";
	public static String TYPE_PARKING_LOT = "parking-lot";
	public static String TYPE_DRONE = "drone";

	public static String PRED_LOC = "loc";
	public static String PRED_LOC_COVERED = "loc-covered";
	public static String PRED_ON = "on";
	public static String SEPARATOR = "--";
	public static String PRED_TRAFF_REQ = "traffic-info-requested";
	public static String PRED_LOC_SENT = "current-loc-sent";
	public static String PRED_PARK_REQ = "parking-info-requested";
	public static String PRED_RESERVE = "parking-slot-reserved ";
	public static String PRED_PARKING_SENT = "parking-info-sent";
	public static String PRED_PARKING_CONFIRM = "parking-slot-confirmed";
	public static String PRED_PARK = "parked";
	public static String PRED_V_INFO_SENT = "vehicle-info-sent";
	public static String PRED_MOVE_UPDATE = "movement-reported";
	public static String PRED_TRAFFIC_INFO_UPDATE = "traffic-info-updated";
	public static String PRED_TRAFFIC_INFO_SENT = "traffic-info-sent";
	public static String PRED_ACCIDENT_COLLECTED = "accident-info-collected";
	public static String PRED_ACCICENT = "accident";
	public static String PRED_ACCIDENT_REPORTED = "accident-reported";
	public static String PRED_PARKING_UPDATE = "parking-info-updated";
	public static String PRED_WIFI = "wifi";

	public static String FUNCT_FUEL = "fuel";

	public static List<Service> allServices() {
		List<Service> ops = new ArrayList<>();

		Provider car = new Provider(TYPE_CAR, "c");
		Service move = new Service(car, "move");
		move.setParas(new ArrayList<Obj>(Arrays.asList(car, new Obj(TYPE_LOC, "from"), new Obj(TYPE_LOC, "to"))));

		Predicate from_c = new Predicate("(loc ?c ?from)", move.paras);
		Predicate to_c = new Predicate("(loc ?c ?to)", move.paras);
		move.addPrecond(from_c);
		move.addPEffect(to_c);
		move.addNEffect(from_c);

		NumericCondition fuel_cond = new NumericCondition("(> (fuel ?c) (consump-rate ?from ?to))", move.paras);
		move.addCond(fuel_cond);

		NumericChange fuel_change = new NumericChange("(decrease (fuel ?c) (consump-rate ?from ?to))", move.paras);
		move.addChange(fuel_change);

		ops.add(move);

		Service refill = new Service(car, "refill");
		refill.setParas(new ArrayList<Obj>(Arrays.asList(car)));
		NumericChange increase_fuel = new NumericChange("(increase (fuel ?c) 100)", move.paras);
		refill.addChange(increase_fuel);

		ops.add(refill);

		Service load = new Service(car, "load");
		load.setParas(new ArrayList<Obj>(Arrays.asList(car, new Obj(TYPE_ROBOT, "t"), new Obj(TYPE_LOC, "l"))));
		Predicate car_at_loc = new Predicate("(loc ?c ?l)", load.paras);
		Predicate thing_at_loc = new Predicate("(loc ?t ?l)", load.paras);
		Predicate thing_on_car = new Predicate("(on ?t ?c)", load.paras);
		load.addPrecond(car_at_loc);
		load.addPrecond(thing_at_loc);
		load.addPEffect(thing_on_car);
		load.addNEffect(thing_at_loc);
		ops.add(load);

		Service unload = new Service(car, "unload");
		unload.setParas(new ArrayList<Obj>(Arrays.asList(car, new Obj(TYPE_ROBOT, "t"), new Obj(TYPE_LOC, "l"))));
		unload.addPrecond(car_at_loc);
		unload.addPrecond(thing_on_car);
		unload.addPEffect(thing_at_loc);
		unload.addNEffect(thing_on_car);
		ops.add(unload);

		Provider drone = new Provider(TYPE_DRONE, "d");
		Service flight = new Service(drone, "flight");
		flight.setParas(new ArrayList<Obj>(Arrays.asList(drone, new Obj(TYPE_LOC, "from"), new Obj(TYPE_LOC, "to"))));

		Predicate from_d = new Predicate("(loc ?d ?from)", flight.paras);
		Predicate to_d = new Predicate("(loc ?d ?to)", flight.paras);
		flight.addPrecond(from_d);
		flight.addPEffect(to_d);
		flight.addNEffect(from_d);
		ops.add(flight);

		Service wifi = new Service(drone, "enable-wifi");
		wifi.setParas(new ArrayList<Obj>(Arrays.asList(drone, new Obj(TYPE_LOC, "l"))));
		Predicate loc_d = new Predicate("(loc ?d ?l)", wifi.paras);
		Predicate loc_wifi = new Predicate("wifi ?l", wifi.paras);
		wifi.addPrecond(loc_d);
		wifi.addPEffect(loc_wifi);
		ops.add(wifi);

		Provider robot = new Provider(TYPE_ROBOT, "r");
		Service discover = new Service(robot, "discover");
		discover.setParas(new ArrayList<Obj>(Arrays.asList(robot, new Obj(TYPE_LOC, "l"))));
		Predicate loc_r = new Predicate("(loc ?r ?l)", discover.paras);
		Predicate loc_covered = new Predicate("(loc-covered ?l)", discover.paras);
		discover.addPrecond(loc_r);
		discover.addPrecond(loc_wifi);
		discover.addPEffect(loc_covered);
		ops.add(discover);
		return ops;

	}

	public void setParas(String parasTxt) {
		paras_map = new HashMap<>();
//		(?c - car ?from ?to - location)
		parasTxt = parasTxt.trim();
		if (parasTxt.isBlank())
			return;
		parasTxt = parasTxt.trim().replace("(", "").replace(")", "");
		String[] parts = parasTxt.split(" ");
		Set<String> currParaSet = new HashSet<>();
		String currType = "";
		for (int i = 0; i < parts.length; i++) {
			if (parts[i].equals("-")) {
				currType = parts[i + 1];
				for (String pn : currParaSet) {
					Obj variable = new Obj(currType, pn);
					paras_map.put(pn, variable);
					this.paras.add(variable);
				}
				currParaSet = new HashSet<>();
			} else if (parts[i].startsWith("?")) {
				currParaSet.add(parts[i]);
			}
		}
		if (!this.paras.isEmpty())
			this.provider = new Provider(this.paras.get(0));
	}
}

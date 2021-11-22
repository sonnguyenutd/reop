package v6.gen.analysis.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import v5.gen.supply_chain.Action;

public class Operator {
	public String name;
	public List<Predicate> preconds;
	public List<Predicate> pEffects;
	public List<Predicate> nEffects;

	public Set<Action> groundedActs;

	public DependentCombo combo;

	public static String TYPE_CAR = "car";
	public static String TYPE_THING = "thing";
	public static String TYPE_LOC = "location";
	public static String TYPE_PARKING_LOT = "parking-lot";
	public static String TYPE_DRONE = "drone";
//	public static String TYPE_LOC = "location";
//	public static String TYPE_LOC = "location";
//	public static String TYPE_LOC = "location";

	public static String PRED_LOC = "loc";
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

	public static void main(String[] args) {
		List<Operator> ops = new ArrayList<>();
		Operator move = new Operator("move");
		Predicate from_c = new Predicate(PRED_LOC, TYPE_CAR + SEPARATOR + TYPE_LOC, "c" + SEPARATOR + "from");
		Predicate to_c = new Predicate(PRED_LOC, TYPE_CAR + SEPARATOR + TYPE_LOC, "c" + SEPARATOR + "to");
		move.addPrecond(from_c);
		move.addPEffect(to_c);
		move.addNEffect(from_c);
		System.out.println(move);
		ops.add(move);

		Operator load = new Operator("load");
		Predicate car_at_loc = new Predicate(PRED_LOC, TYPE_CAR + SEPARATOR + TYPE_LOC, "c" + SEPARATOR + "l");
		Predicate thing_at_loc = new Predicate(PRED_LOC, TYPE_THING + SEPARATOR + TYPE_LOC, "t" + SEPARATOR + "l");
		Predicate thing_on_car = new Predicate(PRED_ON, TYPE_THING + SEPARATOR + TYPE_CAR, "t" + SEPARATOR + "c");
		load.addPrecond(car_at_loc);
		load.addPrecond(thing_at_loc);
		load.addPEffect(thing_on_car);
		load.addNEffect(thing_at_loc);
		System.out.println(load);
		ops.add(load);

		Operator req = new Operator("request-traffic-info");
		Predicate loc_sent = new Predicate(PRED_LOC_SENT, TYPE_CAR + SEPARATOR + TYPE_LOC, "c" + SEPARATOR + "l");
		req.addPrecond(car_at_loc);
		req.addPEffect(loc_sent);
		System.out.println(req);
		ops.add(req);

		Operator send = new Operator("request-traffic-info");
		Predicate traffict_request = new Predicate(PRED_TRAFF_REQ, TYPE_CAR + SEPARATOR + TYPE_LOC,
				"c" + SEPARATOR + "l");
		send.addPrecond(car_at_loc);
		send.addPEffect(traffict_request);
		System.out.println(send);
		ops.add(send);

		Operator req_parking = new Operator("request-traffic-info");
		Predicate park_request = new Predicate(PRED_PARK_REQ, TYPE_CAR + SEPARATOR + TYPE_PARKING_LOT,
				"c" + SEPARATOR + "p");
		req_parking.addPEffect(park_request);
		System.out.println(req_parking);
		ops.add(req_parking);

		Operator reserve = new Operator("reserve-parking-slot");
		Predicate reserve_parking = new Predicate(PRED_RESERVE, TYPE_CAR + SEPARATOR + TYPE_PARKING_LOT,
				"c" + SEPARATOR + "p");
		Predicate sent_parking = new Predicate(PRED_PARKING_SENT, TYPE_CAR + SEPARATOR + TYPE_PARKING_LOT,
				"c" + SEPARATOR + "p");
		reserve.addPrecond(sent_parking);
		reserve.addPEffect(reserve_parking);
		System.out.println(reserve);
		ops.add(reserve);

		Operator send_v_info = new Operator("send-vehicle-info");
		Predicate confrim_parking = new Predicate(PRED_PARKING_CONFIRM, TYPE_CAR + SEPARATOR + TYPE_PARKING_LOT,
				"c" + SEPARATOR + "p");
		Predicate sent_v_info_predicate = new Predicate(PRED_V_INFO_SENT, TYPE_CAR + SEPARATOR + TYPE_PARKING_LOT,
				"c" + SEPARATOR + "p");
		send_v_info.addPrecond(confrim_parking);
		send_v_info.addPEffect(sent_v_info_predicate);
		System.out.println(send_v_info);
		ops.add(send_v_info);

		Operator park = new Operator("park");
		Predicate park_at_loc = new Predicate(PRED_LOC, TYPE_PARKING_LOT + SEPARATOR + TYPE_LOC, "p" + SEPARATOR + "l");
		Predicate parked = new Predicate(PRED_PARK, TYPE_CAR, "c");
		park.addPrecond(confrim_parking);
		park.addPrecond(car_at_loc);
		park.addPrecond(park_at_loc);
		park.addPEffect(parked);
		System.out.println(park);
		ops.add(park);

		Operator update = new Operator("update-traffic-info");
		Predicate move_update = new Predicate(PRED_MOVE_UPDATE, TYPE_CAR + SEPARATOR + TYPE_LOC + SEPARATOR + TYPE_LOC,
				"c" + SEPARATOR + "from" + SEPARATOR + "to");
		Predicate traffic_info_update = new Predicate(PRED_TRAFFIC_INFO_UPDATE, TYPE_CAR + SEPARATOR + TYPE_LOC,
				"c" + SEPARATOR + "from");
		update.addPrecond(move_update);
		update.addPEffect(traffic_info_update);
		System.out.println(update);
		ops.add(update);

		Operator send_traffict_info = new Operator("send-traffic-info");
		Predicate traffic_info_sent = new Predicate(PRED_TRAFFIC_INFO_SENT, TYPE_CAR + SEPARATOR + TYPE_LOC,
				"c" + SEPARATOR + "l");
		send_traffict_info.addPrecond(traffict_request);
		send_traffict_info.addPEffect(car_at_loc);
		send_traffict_info.addPEffect(traffic_info_sent);
		System.out.println(send_traffict_info);
		ops.add(send_traffict_info);

		Operator flight = new Operator("flight");
		Predicate from_d = new Predicate(PRED_LOC, TYPE_DRONE + SEPARATOR + TYPE_LOC, "d" + SEPARATOR + "from");
		Predicate to_d = new Predicate(PRED_LOC, TYPE_DRONE + SEPARATOR + TYPE_LOC, "d" + SEPARATOR + "to");
		flight.addPrecond(from_d);
		flight.addPEffect(to_d);
		flight.addNEffect(from_d);
		System.out.println(flight);
		ops.add(flight);

		Operator collect_accident_info = new Operator("collect-accident-info");
		Predicate d_at_loc = new Predicate(PRED_LOC, TYPE_DRONE + SEPARATOR + TYPE_LOC, "d" + SEPARATOR + "l");
		Predicate accident_at_loc = new Predicate(PRED_ACCICENT, TYPE_LOC, "l");
		Predicate accident_collected = new Predicate(PRED_ACCIDENT_COLLECTED, TYPE_DRONE + SEPARATOR + TYPE_LOC,
				"d" + SEPARATOR + "l");
		collect_accident_info.addPrecond(d_at_loc);
		collect_accident_info.addPrecond(accident_at_loc);
		collect_accident_info.addPEffect(accident_collected);
		System.out.println(collect_accident_info);
		ops.add(collect_accident_info);

		Operator report_accident_info = new Operator("report-accident-info");
		Predicate accident_reported = new Predicate(PRED_ACCIDENT_REPORTED, TYPE_LOC, "l");
		report_accident_info.addPrecond(accident_collected);
		report_accident_info.addPEffect(accident_reported);
		System.out.println(report_accident_info);
		ops.add(report_accident_info);

		Operator send_parking = new Operator("send-parking-info");
		send_parking.addPrecond(park_request);
		send_parking.addPEffect(sent_parking);
		System.out.println(send_parking);
		ops.add(send_parking);

		Operator confirm_parking = new Operator("confirm-parking-reservation");
		confirm_parking.addPrecond(reserve_parking);
		confirm_parking.addPEffect(confrim_parking);
		System.out.println(confirm_parking);
		ops.add(confirm_parking);

		Operator update_parking_info = new Operator("update-parking-info");
		update_parking_info.addPrecond(reserve_parking);
		Predicate parking_update = new Predicate(PRED_PARKING_UPDATE, TYPE_PARKING_LOT, "p");
		update_parking_info.addPEffect(parking_update);
		System.out.println(update_parking_info);
		ops.add(update_parking_info);
	}

	public static List<Operator> allOps() {
		List<Operator> ops = new ArrayList<>();
		Operator move = new Operator("move");
		Predicate from_c = new Predicate(PRED_LOC, TYPE_CAR + SEPARATOR + TYPE_LOC, "c" + SEPARATOR + "from");
		Predicate to_c = new Predicate(PRED_LOC, TYPE_CAR + SEPARATOR + TYPE_LOC, "c" + SEPARATOR + "to");
		move.addPrecond(from_c);
		move.addPEffect(to_c);
		move.addNEffect(from_c);
		ops.add(move);

		Operator load = new Operator("load");
		Predicate car_at_loc = new Predicate(PRED_LOC, TYPE_CAR + SEPARATOR + TYPE_LOC, "c" + SEPARATOR + "l");
		Predicate thing_at_loc = new Predicate(PRED_LOC, TYPE_THING + SEPARATOR + TYPE_LOC, "t" + SEPARATOR + "l");
		Predicate thing_on_car = new Predicate(PRED_ON, TYPE_THING + SEPARATOR + TYPE_CAR, "t" + SEPARATOR + "c");
		load.addPrecond(car_at_loc);
		load.addPrecond(thing_at_loc);
		load.addPEffect(thing_on_car);
		load.addNEffect(thing_at_loc);
		ops.add(load);

		Operator req = new Operator("send-curr-location");
		Predicate loc_sent = new Predicate(PRED_LOC_SENT, TYPE_CAR + SEPARATOR + TYPE_LOC, "c" + SEPARATOR + "l");
		req.addPrecond(car_at_loc);
		req.addPEffect(loc_sent);
		ops.add(req);

		Operator send = new Operator("request-traffic-info");
		Predicate traffict_request = new Predicate(PRED_TRAFF_REQ, TYPE_CAR + SEPARATOR + TYPE_LOC,
				"c" + SEPARATOR + "l");
		send.addPrecond(car_at_loc);
		send.addPEffect(traffict_request);
		ops.add(send);

		Operator req_parking = new Operator("request-parking-info");
		Predicate park_request = new Predicate(PRED_PARK_REQ, TYPE_CAR + SEPARATOR + TYPE_PARKING_LOT,
				"c" + SEPARATOR + "p");
		req_parking.addPEffect(park_request);
		ops.add(req_parking);

		Operator reserve = new Operator("reserve-parking-slot");
		Predicate reserve_parking = new Predicate(PRED_RESERVE, TYPE_CAR + SEPARATOR + TYPE_PARKING_LOT,
				"c" + SEPARATOR + "p");
		Predicate sent_parking = new Predicate(PRED_PARKING_SENT, TYPE_CAR + SEPARATOR + TYPE_PARKING_LOT,
				"c" + SEPARATOR + "p");
		reserve.addPrecond(sent_parking);
		reserve.addPEffect(reserve_parking);
		ops.add(reserve);

		Operator send_v_info = new Operator("send-vehicle-info");
		Predicate confrim_parking = new Predicate(PRED_PARKING_CONFIRM, TYPE_CAR + SEPARATOR + TYPE_PARKING_LOT,
				"c" + SEPARATOR + "p");
		Predicate sent_v_info_predicate = new Predicate(PRED_V_INFO_SENT, TYPE_CAR + SEPARATOR + TYPE_PARKING_LOT,
				"c" + SEPARATOR + "p");
		send_v_info.addPrecond(confrim_parking);
		send_v_info.addPEffect(sent_v_info_predicate);
		ops.add(send_v_info);

		Operator park = new Operator("park");
		Predicate park_at_loc = new Predicate(PRED_LOC, TYPE_PARKING_LOT + SEPARATOR + TYPE_LOC, "p" + SEPARATOR + "l");
		Predicate parked = new Predicate(PRED_PARK, TYPE_CAR, "c");
		park.addPrecond(confrim_parking);
		park.addPrecond(car_at_loc);
		park.addPrecond(park_at_loc);
		park.addPEffect(parked);
		ops.add(park);

		Operator update = new Operator("update-traffic-info");
		Predicate move_update = new Predicate(PRED_MOVE_UPDATE, TYPE_CAR + SEPARATOR + TYPE_LOC + SEPARATOR + TYPE_LOC,
				"c" + SEPARATOR + "from" + SEPARATOR + "to");
		Predicate traffic_info_update = new Predicate(PRED_TRAFFIC_INFO_UPDATE, TYPE_CAR + SEPARATOR + TYPE_LOC,
				"c" + SEPARATOR + "from");
		update.addPrecond(move_update);
		update.addPEffect(traffic_info_update);
		ops.add(update);

		Operator send_traffict_info = new Operator("send-traffic-info");
		Predicate traffic_info_sent = new Predicate(PRED_TRAFFIC_INFO_SENT, TYPE_CAR + SEPARATOR + TYPE_LOC,
				"c" + SEPARATOR + "l");
		send_traffict_info.addPrecond(traffict_request);
		send_traffict_info.addPEffect(car_at_loc);
		send_traffict_info.addPEffect(traffic_info_sent);
		ops.add(send_traffict_info);

		Operator flight = new Operator("flight");
		Predicate from_d = new Predicate(PRED_LOC, TYPE_DRONE + SEPARATOR + TYPE_LOC, "d" + SEPARATOR + "from");
		Predicate to_d = new Predicate(PRED_LOC, TYPE_DRONE + SEPARATOR + TYPE_LOC, "d" + SEPARATOR + "to");
		flight.addPrecond(from_d);
		flight.addPEffect(to_d);
		flight.addNEffect(from_d);
		ops.add(flight);

		Operator collect_accident_info = new Operator("collect-accident-info");
		Predicate d_at_loc = new Predicate(PRED_LOC, TYPE_DRONE + SEPARATOR + TYPE_LOC, "d" + SEPARATOR + "l");
		Predicate accident_at_loc = new Predicate(PRED_ACCICENT, TYPE_LOC, "l");
		Predicate accident_collected = new Predicate(PRED_ACCIDENT_COLLECTED, TYPE_DRONE + SEPARATOR + TYPE_LOC,
				"d" + SEPARATOR + "l");
		collect_accident_info.addPrecond(d_at_loc);
		collect_accident_info.addPrecond(accident_at_loc);
		collect_accident_info.addPEffect(accident_collected);
		ops.add(collect_accident_info);

		Operator report_accident_info = new Operator("report-accident-info");
		Predicate accident_reported = new Predicate(PRED_ACCIDENT_REPORTED, TYPE_LOC, "l");
		report_accident_info.addPrecond(accident_collected);
		report_accident_info.addPEffect(accident_reported);
		ops.add(report_accident_info);

		Operator send_parking = new Operator("send-parking-info");
		send_parking.addPrecond(park_request);
		send_parking.addPEffect(sent_parking);
		ops.add(send_parking);

		Operator confirm_parking = new Operator("confirm-parking-reservation");
		confirm_parking.addPrecond(reserve_parking);
		confirm_parking.addPEffect(confrim_parking);
		ops.add(confirm_parking);

		Operator update_parking_info = new Operator("update-parking-info");
		update_parking_info.addPrecond(reserve_parking);
		Predicate parking_update = new Predicate(PRED_PARKING_UPDATE, TYPE_PARKING_LOT, "p");
		update_parking_info.addPEffect(parking_update);
		ops.add(update_parking_info);

		return ops;
	}

	public Operator(String name) {
		this.name = name;
		preconds = new ArrayList<Predicate>();
		pEffects = new ArrayList<Predicate>();
		nEffects = new ArrayList<Predicate>();
	}
	
	public Set<Action> getGroundedActs() {
		return groundedActs;
	}

	public void setCombo(DependentCombo combo) {
		this.combo = combo;
	}

	public DependentCombo getCombo() {
		return combo;
	}

	public Collection<Operator> getDependents() {
		if (combo != null)
			return combo.dependents;
		return null;
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

	public Operator(String name, List<Predicate> preconds, List<Predicate> pEffects, List<Predicate> nEffects) {
		this.name = name;
		this.preconds = preconds;
		this.pEffects = pEffects;
		this.nEffects = nEffects;
	}

	public boolean isDependentOf(Operator other) {
		List<Predicate> othersEffects = new ArrayList<>(other.pEffects);
		othersEffects.retainAll(this.preconds);
		return !othersEffects.isEmpty();
	}

	public List<Predicate> getConnectors(Operator dependent) {
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
		String result = name + "\n\t*" + preconds + "\n\t+" + pEffects + "\n\t-" + nEffects;
		return result;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
}

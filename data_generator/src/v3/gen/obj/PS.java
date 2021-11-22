package v3.gen.obj;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import v3.gen.generator.ObjectGenerator;

public class PS extends PT {
	Set<ParkingLot> lots;
	Set<Car> cars;

	public PS(Collection<ParkingLot> lots, Collection<Car> cars) {
		this.lots = new HashSet<ParkingLot>(lots);
		this.cars = new HashSet<Car>(cars);
	}

	@Override
	public Set<String> getActions() {
		Set<String> result = new HashSet<>();
//		send-parking-info
		result.addAll(genSendParkingInfo());
//		confirm-parking-reservation
		result.addAll(genConfirmParkingReservation());
//		update-parking-info
		result.addAll(genUpdateParkingInfo());
		return result;
	}

	private Collection<String> genUpdateParkingInfo() {
		Set<String> result = new HashSet<>();
		for (Car c : cars) {
			for (ParkingLot lot : lots) {
				String a = "(:action confirm-parking-reservation-" + getId() + "-" + c.getId() + "-" + lot.getId()
						+ "\n";
				a += "\t:precondition  (and" + "\n";
				a += "\t\t(parking-slot-reserved-" + c.getId() + "-" + lot.getId() + ")\n";
				a += "\t)\n";

				a += "\t:effect  (and" + "\n";
				a += "\t\t(parking-info-updated-" + lot.getId() + ")\n";
				a += "\t)\n";
				a += ")";
				result.add(a);

				ObjectGenerator.predicates
						.add(("\t\t(parking-slot-reserved-" + c.getId() + "-" + lot.getId() + ")\n").trim());
				ObjectGenerator.predicates.add(("\t\t(parking-info-updated-" + lot.getId() + ")\n").trim());

			}
		}
		return result;
	}

	private Collection<String> genConfirmParkingReservation() {
		Set<String> result = new HashSet<>();
		for (Car c : cars) {
			for (ParkingLot lot : lots) {
				String a = "(:action confirm-parking-reservation-" + getId() + "-" + c.getId() + "-" + lot.getId()
						+ "\n";
				a += "\t:precondition  (and" + "\n";
				a += "\t\t(parking-slot-reserved-" + c.getId() + "-" + lot.getId() + ")\n";
				a += "\t)\n";

				a += "\t:effect  (and" + "\n";
				a += "\t\t(parking-slot-confirmed-" + c.getId() + "-" + lot.getId() + ")\n";
				a += "\t)\n";
				a += ")";
				result.add(a);

				ObjectGenerator.predicates
						.add(("\t\t(parking-slot-reserved-" + c.getId() + "-" + lot.getId() + ")\n").trim());
				ObjectGenerator.predicates
						.add(("\t\t(parking-slot-confirmed-" + c.getId() + "-" + lot.getId() + ")\n").trim());

			}
		}
		return result;
	}

	private Collection<String> genSendParkingInfo() {
		Set<String> result = new HashSet<>();
		for (Car c : cars) {
			for (ParkingLot lot : lots) {
				String a = "(:action send-parking-info-" + getId() + "-" + c.getId() + "-" + lot.getId() + "\n";
				a += "\t:precondition  (and" + "\n";
				a += "\t\t(parking-info-requested-" + c.getId() + "-" + lot.getId() + ")\n";
				a += "\t)\n";

				a += "\t:effect  (and" + "\n";
				a += "\t\t(parking-info-sent-" + c.getId() + "-" + lot.getId() + ")\n";
				a += "\t)\n";
				a += ")";
				result.add(a);

				ObjectGenerator.predicates
						.add(("\t\t(parking-info-requested-" + c.getId() + "-" + lot.getId() + ")\n").trim());
				ObjectGenerator.predicates
						.add(("\t\t(parking-info-sent-" + c.getId() + "-" + lot.getId() + ")\n").trim());
			}
		}
		return result;
	}

	@Override
	public String getState() {
		return null;
	}

	@Override
	public Set<String> getParentActions1() {
		return new HashSet<>();
	}

	@Override
	public Set<String> getParentActions2() {
		return new HashSet<>();
	}
}

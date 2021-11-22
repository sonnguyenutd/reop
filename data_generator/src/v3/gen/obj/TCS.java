package v3.gen.obj;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import v3.gen.generator.ObjectGenerator;

public class TCS extends PT {
	Set<Route> routes;
	Set<String> locs;
	Set<Car> cars;
	Set<Displayer> displayers;
	Set<Light> lights;

	public TCS(Collection<Route> routes, Collection<String> locs, Collection<Car> cars,
			Collection<Displayer> displayers, Collection<Light> lights) {
		this.routes = new HashSet<>(routes);
		this.locs = new HashSet<>(locs);
		this.cars = new HashSet<>(cars);
		this.displayers = new HashSet<>(displayers);
		if (lights != null)
			this.lights = new HashSet<>(lights);
		else
			this.lights = new HashSet<>();
	}
	private Collection<String> genSendTrafficInfo() {
		Set<String> result = new HashSet<>();
		for (Car c : cars) {
			for (String l : locs) {
				String a = "(:action send-traffic-info-" + getId() + "-" + c.getId() + "-" + l + "\n";
				a += "\t:precondition  (and" + "\n";
				a += "\t\t(traffic-info-requested-" + c.getId() + "-" + l + ")\n";
				a += "\t)\n";

				a += "\t:effect  (and" + "\n";
				a += "\t\t(traffic-info-sent-" + c.getId() + "-" + l + ")\n";
				a += "\t)\n";
				a += ")";
				result.add(a);

				ObjectGenerator.predicates.add(("\t\t(traffic-info-requested-" + c.getId() + "-" + l + ")\n").trim());
				ObjectGenerator.predicates.add(("\t\t(traffic-info-sent-" + c.getId() + "-" + l + ")\n").trim());
			}
		}
		return result;
	}

	@Override
	public Set<String> getActions() {
		Set<String> result = new HashSet<>();
//		update-traffic-info
//		result.addAll(genUpdateTrafficInfo());
//		send-traffic-info
		result.addAll(genSendTrafficInfo());
//		send-accident-info-displayer
//		result.addAll(genSendAccidentInfoDisplayer());
//		change-traffic-light
//		result.addAll(genChangeTrafficLight());
//		signal-accident
//		result.addAll(genSignalAccident());

		for (String l : locs) {
			String a = "(:action system-accident-handle-signal-" + getId() + "-" + l + "\n";
			a += "\t:precondition  (and" + "\n";
			a += "\t\t(ambulance-at-accident)\n";
			a += "\t\t(police-at-accident)\n";
//			a += "\t\t(accident-message-displayed-" + l + ")\n";
			a += "\t)\n";

			a += "\t:effect  (and" + "\n";
			a += "\t\t(accident-handled)\n";
			a += "\t)\n";
			a += ")";
			result.add(a);
			ObjectGenerator.predicates.add(("\t\t(ambulance-at-accident)\n").trim());
			ObjectGenerator.predicates.add(("\t\t(police-at-accident)\n").trim());
//			ObjectGenerator.predicates.add(("\t\t(accident-message-displayed-" + l + ")\n").trim());
			ObjectGenerator.predicates.add(("\t\t(accident-handled)\n").trim());
		}
		return result;
	}
	
	public Set<String> getParentActions1() {
		Set<String> result = new HashSet<>();
//		update-traffic-info
//		result.addAll(genUpdateTrafficInfo());

		result.addAll(genA3());

		for (String l : locs) {
			String a = "(:action system-accident-handle-signal-" + getId() + "-" + l + "\n";
			a += "\t:precondition  (and" + "\n";
			a += "\t\t(ambulance-at-accident)\n";
			a += "\t\t(police-at-accident)\n";
			a += "\t\t(accident-message-displayed-" + l + ")\n";
			a += "\t)\n";

			a += "\t:effect  (and" + "\n";
			a += "\t\t(accident-handled)\n";
			a += "\t)\n";
			a += ")";
			result.add(a);
			ObjectGenerator.predicates.add(("\t\t(ambulance-at-accident)\n").trim());
			ObjectGenerator.predicates.add(("\t\t(police-at-accident)\n").trim());
			ObjectGenerator.predicates.add(("\t\t(accident-message-displayed-" + l + ")\n").trim());
			ObjectGenerator.predicates.add(("\t\t(accident-handled)\n").trim());
		}
		return result;
	}

	private Collection<String> genChangeTrafficLight() {
		Set<String> result = new HashSet<>();
		for (Light l : lights) {
			String a = "(:action change-traffic-light-" + getId() + "-" + l.getId() + "\n";
			a += "\t:precondition  (and" + "\n";
			a += "\t)\n";

			a += "\t:effect  (and" + "\n";
			a += "\t\t(control-taken-" + l.getId() + ")\n";
			a += "\t)\n";
			a += ")";
			result.add(a);

			ObjectGenerator.predicates.add(("\t\t(control-taken-" + l.getId() + ")\n").trim());
		}
		return result;
	}

	private Collection<String> genSendAccidentInfoDisplayer() {
		Set<String> result = new HashSet<>();
		for (Displayer dis : displayers) {
			for (String l : locs) {
				String a = "(:action send-accident-info-displayer-" + getId() + "-" + dis.getId() + "-" + l + "\n";
				a += "\t:precondition  (and" + "\n";
				a += "\t\t(accident-reported-" + l + ")\n";
				a += "\t)\n";

				a += "\t:effect  (and" + "\n";
				a += "\t\t(assident-message-sent-" + dis.getId() + "-" + l + ")\n";
				a += "\t)\n";
				a += ")";
				result.add(a);

				ObjectGenerator.predicates.add(("\t\t(accident-reported-" + l + ")\n").trim());
				ObjectGenerator.predicates.add(("\t\t(assident-message-sent-" + dis.getId() + "-" + l + ")\n").trim());
			}
		}
		return result;
	}

	
	private Collection<String> genUpdateTrafficInfo() {
		Set<String> result = new HashSet<>();
		for (Car c : cars) {
			for (Route r : routes) {
				String a = "(:action update-traffic-info-" + getId() + "-" + c.getId() + "-" + r.getFrom() + "-"
						+ r.getTo() + "\n";
				a += "\t:precondition  (and" + "\n";
				a += "\t\t(movement-reported-" + c.getId() + "-" + r.getFrom() + "-" + r.getTo() + ")\n";
				a += "\t)\n";

				a += "\t:effect  (and" + "\n";
				a += "\t\t(traffic-info-updated-" + c.getId() + "-" + r.getFrom() + "-" + r.getTo() + ")\n";
				a += "\t)\n";
				a += ")";
				result.add(a);

				ObjectGenerator.predicates.add(
						("\t\t(movement-reported-" + c.getId() + "-" + r.getFrom() + "-" + r.getTo() + ")\n").trim());
				ObjectGenerator.predicates
						.add(("\t\t(traffic-info-updated-" + c.getId() + "-" + r.getFrom() + "-" + r.getTo() + ")\n")
								.trim());
			}
		}
		return result;
	}

	@Override
	public String getState() {
		return null;
	}
	
	private Collection<String> genA3() {
		Set<String> result = new HashSet<>();
		for (String l : locs) {
			String a = "(:action a3-" + getId() + "-" + l + "\n";
			a += "\t:precondition  (and" + "\n";
			a += "\t\t(accident-reported-" + l + ")\n";
			a += "\t)\n";

			a += "\t:effect  (and" + "\n";

//			for (Car c : cars) {
//				a += "\t\t(accident-signaled-" + c.getId() + "-" + l + ")\n";
//				ObjectGenerator.predicates.add(("\t\t(accident-signaled-" + c.getId() + "-" + l + ")\n").trim());
//			}
			for (Displayer dis : displayers) {
				a += "\t\t(assident-message-sent-" + dis.getId() + "-" + l + ")\n";
				ObjectGenerator.predicates.add(("\t\t(assident-message-sent-" + dis.getId() + "-" + l + ")\n").trim());
			}
			a += "\t\t(light-control-taken-" + l + ")\n";
			a += "\t)\n";
			a += ")";

			result.add(a);
			ObjectGenerator.predicates.add(("\t\t(accident-reported-" + l + ")\n").trim());
			ObjectGenerator.predicates.add(("\t\t(light-control-taken-" + l + ")\n").trim());
		}
		return result;
	}
	
	@Override
	public Set<String> getParentActions2() {
		return getParentActions1();
	}

}

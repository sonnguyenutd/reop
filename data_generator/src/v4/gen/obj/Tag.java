package v4.gen.obj;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import v3.gen.obj.PT;
import v3.gen.obj.Route;
import v4.gen.generator.ObjectGenerator;

public class Tag extends PT {
	Collection<WareHouse> warehouses;
	Collection<Route> routes;
	Collection<String> locs;
	Collection<Order> orders;

	public Tag() {
	}

	public void setWarehouses(Collection<WareHouse> warehouses) {
		this.warehouses = warehouses;
	}

	public void setRoutes(Collection<Route> routes) {
		this.routes = routes;
	}

	public void setLocs(Collection<String> locs) {
		this.locs = locs;
	}

	public void setOrders(Collection<Order> orders) {
		this.orders = orders;
	}

	public Tag(Collection<WareHouse> warehouses, Collection<Route> routes, Collection<String> locs,
			Collection<Order> orders) {
		this.warehouses = warehouses;
		this.routes = routes;
		this.locs = locs;
		this.orders = orders;
	}

	@Override
	public Set<String> getActions() {
		Set<String> result = new HashSet<String>();
		result.addAll(genReportTemp());
		result.addAll(genReportHumidity());
		result.addAll(genStorageStatusUpdate());
		result.addAll(genDistributionStatusUpdate());
		result.addAll(genMove());
		result.addAll(genDistributionStatusUpdate());
		return result;
	}

	@Override
	public Set<String> getParentActions1() {
		Set<String> result = new HashSet<String>();
		result.addAll(genA2());
		result.addAll(genMove());
		result.addAll(genDistributionStatusUpdate());
		return result;
	}

	private Collection<String> genA2() {
		Set<String> result = new HashSet<String>();
		for (WareHouse w : warehouses) {
			String a = "(:action report-temp-" + getId() + "-" + w.getId() + "\n";
			a += "\t:precondition  (and" + "\n";
			a += "\t\t(status-stored-" + getId() + ")" + "\n";
			a += "\t)\n";

			a += "\t:effect  (and" + "\n";
			a += "\t\t(temp-reported-" + getId() + "-" + w.getId() + ")" + "\n";
			a += "\t\t(humidity-reported-" + getId() + "-" + w.getId() + ")" + "\n";
			a += "\t\t(storage-status-updated-" + getId() + ")" + "\n";
			a += "\t\t(loc-" + getId() + "-" + w.getId() + ")" + "\n";
			a += "\t)\n";
			a += ")";

			ObjectGenerator.predicates.add(("\t\t(status-stored-" + getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(temp-reported-" + getId() + "-" + w.getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(humidity-reported-" + getId() + "-" + w.getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(storage-status-updated-" + getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(loc-" + getId() + "-" + w.getId() + ")").trim());

			result.add(a);
		}
		return result;
	}

	private Collection<String> genDistributionStatusUpdate() {
		Set<String> result = new HashSet<String>();
		for (String l : locs) {
			for (Order o : orders) {
				String a = "(:action distribution-status-update-" + getId() + "-" + l + "-" + o.getId() + "\n";
				a += "\t:precondition  (and" + "\n";
				a += "\t\t(destination-" + l + "-" + o.getId() + ")" + "\n";
				a += "\t\t(loc-" + getId() + "-" + l + ")\n";
				a += "\t\t(accociated-" + getId() + "-" + o.getId() + ")" + "\n";
				a += "\t)\n";

				a += "\t:effect  (and" + "\n";
				a += "\t\t(order-done-" + o.getId() + ")" + "\n";
				a += "\t)\n";
				a += ")";

				ObjectGenerator.predicates.add(("\t\t(destination-" + l + "-" + o.getId() + ")").trim());
				ObjectGenerator.predicates.add(("\t\t(loc-" + getId() + "-" + l + ")\n").trim());
				ObjectGenerator.predicates.add(("\t\t(accociated-" + getId() + "-" + o.getId() + ")").trim());
				ObjectGenerator.predicates.add(("\t\t(order-done-" + o.getId() + ")").trim());
				result.add(a);
			}
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

	private Collection<String> genStorageStatusUpdate() {
		Set<String> result = new HashSet<String>();
		for (WareHouse w : warehouses) {
			String a = "(:action storage-status-update-" + getId() + "-" + w.getId() + "\n";
			a += "\t:precondition  (and" + "\n";
			a += "\t\t(status-stored-" + getId() + ")" + "\n";
			a += "\t\t(temp-reported-" + getId() + "-" + w.getId() + ")" + "\n";
			a += "\t\t(humidity-reported-" + getId() + "-" + w.getId() + ")" + "\n";
			a += "\t)\n";

			a += "\t:effect  (and" + "\n";
			a += "\t\t(storage-status-updated-" + getId() + ")" + "\n";
			a += "\t\t(loc-" + getId() + "-" + w.getId() + ")" + "\n";
			a += "\t)\n";
			a += ")";

			ObjectGenerator.predicates.add(("\t\t(status-stored-" + getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(temp-reported-" + getId() + "-" + w.getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(humidity-reported-" + getId() + "-" + w.getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(loc-" + getId() + "-" + w.getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(storage-status-updated-" + getId() + ")").trim());
			result.add(a);
		}
		return result;
	}

	private Collection<String> genReportTemp() {
		Set<String> result = new HashSet<String>();
		for (WareHouse w : warehouses) {
			String a = "(:action report-temp-" + getId() + "-" + w.getId() + "\n";
			a += "\t:precondition  (and" + "\n";
			a += "\t\t(status-stored-" + getId() + ")" + "\n";
			a += "\t)\n";

			a += "\t:effect  (and" + "\n";
			a += "\t\t(temp-reported-" + getId() + "-" + w.getId() + ")" + "\n";
			a += "\t)\n";
			a += ")";

			ObjectGenerator.predicates.add(("\t\t(status-stored-" + getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(temp-reported-" + getId() + "-" + w.getId() + ")").trim());

			result.add(a);
		}
		return result;
	}

	private Collection<String> genReportHumidity() {
		Set<String> result = new HashSet<String>();
		for (WareHouse w : warehouses) {
			String a = "(:action report-temp-" + getId() + "-" + w.getId() + "\n";
			a += "\t:precondition  (and" + "\n";
			a += "\t\t(status-stored-" + getId() + ")" + "\n";
			a += "\t)\n";

			a += "\t:effect  (and" + "\n";
			a += "\t\t(humidity-reported-" + getId() + "-" + w.getId() + ")" + "\n";
			a += "\t)\n";
			a += ")";

			ObjectGenerator.predicates.add(("\t\t(status-stored-" + getId() + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(humidity-reported-" + getId() + "-" + w.getId() + ")").trim());

			result.add(a);
		}
		return result;
	}

	@Override
	public Set<String> getParentActions2() {
		Set<String> result = new HashSet<String>();
		result.addAll(genA2());
		result.addAll(genA21());
		result.addAll(genDistributionStatusUpdate());
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
		result += "\t\t(storage-status-updated-" + getId() + ")\n";
		result += "\t)\n";

		result += "\t:effect  (and\n";

		result += "\t\t(not (loc-" + getId() + "-" + from + "))" + "\n";

		for (String to : tos) {
			result += "\t\t(loc-" + getId() + "-" + to + ")" + "\n";
			result += "\t\t(curr-loc-reported-" + getId() + "-" + to + ")" + "\n";
			result += "\t\t(temp-reported-" + getId() + "-" + to + ")" + "\n";
			result += "\t\t(humidity-reported-" + getId() + "-" + to + ")" + "\n";

			ObjectGenerator.predicates.add(("\t\t(loc-" + getId() + "-" + to + ")\n").trim());
			ObjectGenerator.predicates.add(("\t\t(curr-loc-reported-" + getId() + "-" + to + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(humidity-reported-" + getId() + "-" + to + ")").trim());
			ObjectGenerator.predicates.add(("\t\t(temp-reported-" + getId() + "-" + to + ")").trim());
		}
		ObjectGenerator.predicates.add(("\t\t(loc-" + getId() + "-" + from + ")\n").trim());
		ObjectGenerator.predicates.add(("\t\t(storage-status-updated-" + getId() + ")\n").trim());

		result += "\t)\n";
		result += ")";

		return result;
	}

	private String createAMove(Route r) {
		String result = "(:action move-" + getId() + "-" + r.getFrom() + "-" + r.getTo() + "\n";
		result += "\t:precondition  (and\n";
		result += "\t\t(loc-" + getId() + "-" + r.getFrom() + ")\n";
		result += "\t\t(storage-status-updated-" + getId() + ")\n";
		result += "\t)\n";

		result += "\t:effect  (and\n";
		result += "\t\t(not (loc-" + getId() + "-" + r.getFrom() + "))" + "\n";
		result += "\t\t(loc-" + getId() + "-" + r.getTo() + ")" + "\n";

		result += "\t\t(curr-loc-reported-" + getId() + "-" + r.getTo() + ")" + "\n";
		result += "\t\t(temp-reported-" + getId() + "-" + r.getTo() + ")" + "\n";
		result += "\t\t(humidity-reported-" + getId() + "-" + r.getTo() + ")" + "\n";
		result += "\t)\n";
		result += ")";

		ObjectGenerator.predicates.add(("\t\t(loc-" + getId() + "-" + r.getFrom() + ")\n").trim());
		ObjectGenerator.predicates.add(("\t\t(loc-" + getId() + "-" + r.getTo() + ")\n").trim());
		ObjectGenerator.predicates.add(("\t\t(storage-status-updated-" + getId() + ")\n").trim());
		ObjectGenerator.predicates.add(("\t\t(curr-loc-reported-" + getId() + "-" + r.getTo() + ")").trim());
		ObjectGenerator.predicates.add(("\t\t(humidity-reported-" + getId() + "-" + r.getTo() + ")").trim());
		ObjectGenerator.predicates.add(("\t\t(temp-reported-" + getId() + "-" + r.getTo() + ")").trim());
		return result;
	}

	@Override
	public String getState() {
		return null;
	}

}

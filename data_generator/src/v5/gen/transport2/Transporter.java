package v5.gen.transport2;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import gen.utils.Utils;
import v5.gen.supply_chain.Action;
import v5.gen.supply_chain.Part;

public class Transporter {
	String name;
	Set<Action> actions;

	public Transporter(String name) {
		this.name = name;
		this.actions = new HashSet<>();
	}

	public void generateActions(Collection<Route> routes, Collection<Package> packs) {
		for (Package p : packs) {
			for (Route r : routes) {
				String packName = p.toString();
				String from = r.from.toString();
				String to = r.to.toString();
				Action a = new Action();
//				String actName = "MOVE-" + name + "-" + packName + "-" + from + "-" + to;
				String actName = Utils.getAlphaNumericString(3) + "-" + packName + "-" + from + "-" + to;
				if (r.isHubConnector()) {
					actName += "-HUB";
				}
				a.setName(actName);

				Set<String> pres = new HashSet<>();
				pres.add("CLOC-" + packName + "-" + from);
				pres.add("AVAILABLE-" + packName);
				a.setPreconds(pres);

				Set<String> neff = new HashSet<>();
				neff.add("CLOC-" + packName + "-" + from);
				a.setNeffects(neff);

				Set<String> peff = new HashSet<>();
				peff.add("CLOC-" + packName + "-" + to);
				a.setEffects(peff);
				actions.add(a);
			}
		}
	}

	public Set<Action> getActions() {
		return actions;
	}

	@Override
	public boolean equals(Object obj) {
		return this.toString().equals(obj.toString());
	}

	@Override
	public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}

	public void generateActions(Set<Route> routes, Set<Part> allParts) {
		for (Part p : allParts) {
			for (Route r : routes) {
				String packName = p.toString();
				String from = r.from.toString();
				String to = r.to.toString();
				Action a = new Action();
//				String actName = "MOVE-" + name + "-" + packName + "-" + from + "-" + to;
				String actName = Utils.getAlphaNumericString(1) + "-" + packName + "-" + from + "-" + to;
				if (r.isHubConnector()) {
					actName += "-HUB";
				}
				a.setName(actName);

				Set<String> pres = new HashSet<>();
				pres.add("CLOC-" + packName + "-" + from);
				pres.add("AVAILABLE-" + packName);
				a.setPreconds(pres);

				Set<String> neff = new HashSet<>();
				neff.add("CLOC-" + packName + "-" + from);
				a.setNeffects(neff);

				Set<String> peff = new HashSet<>();
				peff.add("CLOC-" + packName + "-" + to);
				a.setEffects(peff);
				actions.add(a);
			}
		}
	}
}

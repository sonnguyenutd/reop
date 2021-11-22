package v5.gen.supply_chain;

import java.util.ArrayList;
import java.util.List;

import gen.utils.Utils;
import v5.gen.transport2.Route;

public class Transportation {
	Supplier from;
	List<Route> fragments;
	Supplier to;
	

	public Transportation(Supplier from, Supplier to) {
		this.from = from;
		this.to = to;
		createRoutes();
	}

	private void createRoutes() {
		fragments = new ArrayList<Route>();
		int numOfMoves = Utils.randomNumber(1, ChainGenerator.MAX_MOVES_LEN);
		String f = from.getLoc();
		String t = to.getLoc();
		for (int i = 0; i < numOfMoves - 1; i++) {
			String af = Utils.generateLoc();
			if (!f.equals(af)) {
				Route r = new Route(f, af);
				fragments.add(r);
			}
			f = af;
		}
		Route r = new Route(f, t);
		fragments.add(r);
	}

//	public List<Action> getActions(List<Transporter> transporters, Part p) {
//		List<Action> actions = new ArrayList<>();
//		for (Transporter t : transporters) {
//			for (Route r : fragments) {
//				String from = r.from.toString();
//				String to = r.to.toString();
//				Action a = new Action();
//				String actName = "MOVE-" + t.getName() + "-" + p.getName() + "-" + from + "-" + to;
//				if (r.isHubConnector()) {
//					actName += "-HUB";
//				}
//				a.setName(actName);
//
//				Set<String> pres = new HashSet<>();
//				pres.add("CLOC-" + p.getName() + "-" + from);
//				pres.add("AVAILABLE-" + p.getName());
//				a.setPreconds(pres);
//
//				Set<String> neff = new HashSet<>();
//				neff.add("CLOC-" + p.getName() + "-" + from);
//				a.setNeffects(neff);
//
//				Set<String> peff = new HashSet<>();
//				peff.add("CLOC-" + p.getName() + "-" + to);
//				a.setEffects(peff);
//				actions.add(a);
//			}
//		}
//		return actions;
//	}

	public List<Route> getFragments() {
		return fragments;
	}

	@Override
	public String toString() {
		String result = from.getSupplierName() + "-->" + to.getSupplierName() + ":" + fragments.size();
		return result;
	}
}

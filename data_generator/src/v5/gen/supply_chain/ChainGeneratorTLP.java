package v5.gen.supply_chain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gen.utils.Utils;
import v5.gen.transport2.Loc;
import v5.gen.transport2.Route;
import v5.gen.transport2.Transporter;

public class ChainGeneratorTLP {
	public static void main(String[] args) {
		int numOfProbs = 25;
		int maxDeep = 10;
		int maxBrand = 5;
		while (true) {
			System.out.println("--------------------------------------------");
			String partName = Utils.getAlphaNumericString(Config.MAX_PART_LEN);
			Part finalProduct = generate(partName, maxDeep, maxBrand);
			Set<Part> allParts = getAllParts(finalProduct);
			System.out.println(allParts.size());

			if (allParts.size() > 30)
				continue;

			List<Action> acts = getAllSteps(allParts);
			System.out.println("STEPS: " + acts.size());

			List<Transportation> allTrans = getAllTransportations(allParts);
			List<Transporter> transporters = generateTransporters();
			List<Action> allMoves = getAllMoves(transporters, allTrans, allParts);
			acts.addAll(allMoves);
			System.out.println("ALL ACTIONS: " + acts.size());

			if (Config.size * 1000 * 1.1 > acts.size() && acts.size() > (Config.size * 1000) / 1.02) {
				String originalDomain = Utils.toDomain(acts, "test");
				Utils.write(Config.path + Config.size + "/domain2.pddl", originalDomain );

				Set<Action> repMoves = getREPMoves(allMoves);
				Set<Action> repSteps = getREPSteps(allParts);

				Set<Action> reps = new HashSet<Action>();
				reps.addAll(repSteps);
				reps.addAll(repMoves);
				
				String repDomain = Utils.toDomain(reps, "test");
				Utils.write(Config.path + Config.size + "/domain_rep.pddl", repDomain);
				System.out.println(acts.size() + "--" + reps.size());

				generateProblems(numOfProbs, allParts, Config.path + Config.size);
				System.out.println("DONE!");
				break;
			}
		}

	}

	private static Set<Action> getREPMoves(List<Action> allMoves) {
		Map<Set<String>, Set<Action>> map = new HashMap<>();
		for (Action a : allMoves) {
			Set<Action> group = map.get(a.getPreconds());
			if (group == null)
				group = new HashSet<>();
			group.add(a);
			map.put(a.getPreconds(), group);
		}
		Map<Action, Collection<Action>> repMap = new HashMap<>();
		int counter = 1;
		for (Set<String> pres : map.keySet()) {
			String repName = "REP-MOVE-" + (counter++);
			Action a = new Action(repName);
			a.setPreconds(pres);
			Set<Action> children = map.get(pres);
			Set<String> pEffects = new HashSet<>();
			Set<String> nEffect = new HashSet<>();
			for (Action child : children) {
				pEffects.addAll(child.getEffects());
				nEffect.addAll(child.getNeffects());
			}
			a.setNeffects(nEffect);
			a.setEffects(pEffects);
			repMap.put(a, children);
		}
		Utils.toREP(Config.path, repMap, Config.size, "MOVE");
		return repMap.keySet();
	}

	private static Set<Action> getREPSteps(Set<Part> allParts) {
		Set<Action> result = new HashSet<>();
		Map<Action, Collection<Action>> repMap = new HashMap<>();
		for (Part part : allParts) {
			Set<Supplier> suppliers = part.getSuppliers();
			for (Supplier s : suppliers) {
//				result.addAll(s.getRandomActions());
				result.addAll(s.getREPActions());

				repMap.putAll(s.getGroupedActions());
			}
		}
		Utils.toREP(Config.path, repMap, Config.size, "STEP");
		return result;
	}

	private static void generateProblems(int numOfProbs, Set<Part> allParts, String path) {
		List<Part> parts = new ArrayList<>(allParts);
		String init = "(" + parts.get(0).getAvailable() + ")";
		for (int i = 0; i < numOfProbs; i++) {
			int index = Utils.randomNumber(1, parts.size());
			Part p = parts.get(index);
			String goal = "(" + p.getAvailable() + ")";
			String prob = Utils.toProblem(init, goal);
			Utils.write(path + "/prob" + i + ".pddl", prob);
		}
	}

	public static List<Action> getAllMoves(List<Transporter> transporters, List<Transportation> allTrans,
			Set<Part> allParts) {
		List<Action> result = new ArrayList<>();
		Set<Route> allRoutes = new HashSet<>();
		for (Transportation tran : allTrans)
			allRoutes.addAll(tran.getFragments());
		Set<Loc> allLocs = new HashSet<>();
		for (Route r : allRoutes)
			allLocs.addAll(r.getAllLocs());

		Set<Route> additionalRoutes = generateAdditionalRoutes(allLocs);
		allRoutes.addAll(additionalRoutes);
		for (Transporter t : transporters) {
			t.generateActions(allRoutes, allParts);
			result.addAll(t.getActions());
		}
		return result;
	}

	private static Set<Route> generateAdditionalRoutes(Set<Loc> allLocs) {
		Set<Route> result = new HashSet<>();
		for (Loc l1 : allLocs) {
			for (Loc l2 : allLocs) {
				if (!l1.equals(l2) && Utils.randomNumber(0, Config.ROUTE_FACTOR) % Config.ROUTE_FACTOR == 0) {
					Route r = new Route(l1, l2);
					result.add(r);
				}
			}
		}
		return result;
	}

	public static List<Transporter> generateTransporters() {
		List<Transporter> trans = new ArrayList<>();
		int numOfTrans = Config.NUM_TRANSPORTER;
		while (trans.size() < numOfTrans) {
			String tranName = "TRAN-" + Utils.getAlphaNumericString(3);
			Transporter t = new Transporter(tranName);
			if (!trans.contains(t)) {
				trans.add(t);
			}
		}
		return trans;
	}

	public static List<Action> getAllSteps(Set<Part> allParts) {
		List<Action> result = new ArrayList<>();
		for (Part part : allParts) {
			Set<Supplier> suppliers = part.getSuppliers();
			for (Supplier s : suppliers) {
				result.addAll(s.getActions());
				result.addAll(s.getRandomActions());
			}

		}
		return result;
	}

	public static List<Transportation> getAllTransportations(Set<Part> allParts) {
		List<Transportation> allTransportations = new ArrayList<>();
		for (Part p1 : allParts) {
			for (Part p2 : allParts) {
				if (!p1.equals(p2) && p1.getRequiredParts().contains(p2)) {
					List<Transportation> trans = getAllTransportations(p2, p1);
					allTransportations.addAll(trans);
				}
			}
		}
		return allTransportations;
	}

	private static List<Transportation> getAllTransportations(Part p2, Part p1) {
		Set<Supplier> froms = p2.getSuppliers();
		Set<Supplier> tos = p1.getSuppliers();
		List<Transportation> result = new ArrayList<Transportation>();
		int r = 1;
		for (Supplier from : froms) {
			for (Supplier to : tos) {
				if (r == 1) {
					Transportation t = new Transportation(from, to);
					result.add(t);
				}
				r = Utils.randomNumber(1, 2);
			}
		}
		return result;
	}

	public static Set<Part> getAllParts(Part part) {
		Set<Part> result = new HashSet<>();
		result.add(part);
		if (!part.getRequiredParts().isEmpty())
			for (Part p : part.getRequiredParts()) {
				result.addAll(getAllParts(p));
			}
		return result;
	}

	public static Part generate(String partName, int maxDeep, int maxBrand) {
		Part part = new Part(partName, maxDeep);
		if (maxDeep > 0) {
			int numOfRequiredParts = Utils.randomNumber(1, maxBrand);
//			System.out.println(partName + "--" + maxDeep + "--" + numOfRequiredParts);
//			System.out.print("--> REQUIRES: ");
			Set<String> requiredPartsName = new HashSet<String>();
			while (requiredPartsName.size() < numOfRequiredParts) {
				String pName = generatePartName();
				if (!part.containsPart(pName)) {
//					System.out.print(pName + "  ");
					requiredPartsName.add(pName);
				}
			}
//			System.out.println("\n");
			for (String pName : requiredPartsName) {
				// generate Part
				Part p = generate(pName, maxDeep - Utils.randomNumber(1, maxDeep), maxBrand);
				part.addRequiredAPart(p);
			}
		}
		part.createSuppliers();
		return part;
	}

	private static String generatePartName() {
		return "P" + Utils.getAlphaNumericString(Config.MAX_PART_LEN);
	}

}

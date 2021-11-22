package v5.gen.supply_chain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gen.utils.Utils;
import v5.gen.transport2.Loc;
import v5.gen.transport2.Route;
import v5.gen.transport2.Transporter;

public class ChainGenerator {
	public static final int MAX_NUM_PREDICATES = 4;
	public static final int MAX_PART_LEN = 3;
	public static final int MAX_PROCESS_CHAIN_LEN = 5;
	public static final int MAX_PREDICATE_LEN = 5;
	public static final int MAX_ACT_NAME_LEN = 6;
	public static final int MAX_SUPPLIERS_PER_PART = 5;
	public static final int MAX_MOVES_LEN = 5;
	private static final int NUM_TRANSPORTER = 4;
	private static final int ROUTE_FACTOR = 11;
	public static final int MAX_RANDOM_ACTS_PER_SUPPLIER = 50;

	public static void main(String[] args) {
		int size = 1000;
		int numOfProbs = 20;
//		int maxDeep = Utils.randomNumber(5, 8);
//		int maxBrand = Utils.randomNumber(5, 8);
		int maxDeep = 6;
		int maxBrand = 6;
		String partName = Utils.getAlphaNumericString(MAX_PART_LEN);
		Part finalProduct = generate(partName, maxDeep, maxBrand);
		Set<Part> allParts = getAllParts(finalProduct);
		System.out.println(allParts.size());

		if (allParts.size() > 35)
			return;

		List<Action> acts = getAllSteps(allParts);
		System.out.println("STEPS: " + acts.size());

		List<Transportation> allTrans = getAllTransportations(allParts);
		List<Transporter> transporters = generateTransporters();
		List<Action> allMoves = getAllMoves(transporters, allTrans, allParts);

		acts.addAll(allMoves);
		System.out.println("ALL ACTIONS: " + acts.size());
		if (acts.size() > size * 1.5 * 1000)
			return;

		Map<Set<String>, Set<Action>> groups = Utils.cluster(acts);
		System.out.println("GROUP: " + groups.size());
		
		String domain = Utils.toDomain(acts, "test");
		Utils.write("/Users/sonnguyen/Desktop/test_2/" + size + "/domain2.pddl", domain);
		generateProblems(numOfProbs, allParts, "/Users/sonnguyen/Desktop/test_2/" + size);
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

	private static List<Action> getAllMoves(List<Transporter> transporters, List<Transportation> allTrans,
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
				if (!l1.equals(l2) && Utils.randomNumber(0, ROUTE_FACTOR) % ROUTE_FACTOR == 0) {
					Route r = new Route(l1, l2);
					result.add(r);
				}
			}
		}
		return result;
	}

	private static List<Transporter> generateTransporters() {
		List<Transporter> trans = new ArrayList<>();
		int numOfTrans = NUM_TRANSPORTER;
		while (trans.size() < numOfTrans) {
			String tranName = "TRAN-" + Utils.getAlphaNumericString(3);
			Transporter t = new Transporter(tranName);
			if (!trans.contains(t)) {
				trans.add(t);
			}
		}
		return trans;
	}

	private static List<Action> getAllSteps(Set<Part> allParts) {
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

	private static List<Transportation> getAllTransportations(Set<Part> allParts) {
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

	private static Set<Part> getAllParts(Part part) {
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
		return "P" + Utils.getAlphaNumericString(MAX_PART_LEN);
	}

}

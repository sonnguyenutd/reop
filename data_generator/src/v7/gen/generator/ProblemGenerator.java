package v7.gen.generator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gen.utils.Utils;
import v3.gen.obj.Car;
import v3.gen.obj.PT;

public class ProblemGenerator {
	public static int ROUTING = 1;
	public static int PARKING = 2;
	public static int ACCIDENT_RESPONSE = 3;

	public static Set<String> generate(int numOfProblem, Set<String> state, Collection<String> locs, Car c, int kind) {
		Set<String> result = new HashSet<String>();
		List<String> ls = new ArrayList<>(locs);
		while (result.size() < numOfProblem) {
			int j = Utils.randomNumber(0, ls.size() - 1);
			String randomLoc1 = ls.get(j);
			String init = getInitState(state, randomLoc1);

			int k = Utils.randomNumber(0, ls.size() - 1);
			String randomLoc2 = ls.get(k);

			String goal = "";
			if (kind == ROUTING) {
				goal = "(loc-" + c.getId() + "-" + randomLoc1 + ")\n";
			} else if (kind == PARKING) {
				goal = "(parked-" + c.getId() + ")\n";
			} else {
				goal = "(loc-" + c.getId() + "-" + randomLoc2 + ")\n";
				goal += "\t(accident-handled)\n";
			}
			String prob = "(define (problem p-" + result.size() + ")\n" + "	(:domain test )\n" + "	(:init \n" + ""
					+ init + " \n" + "	)\n" + "	(:goal (and \n" + "		" + goal + " \n" + "	))\n" + ")";
			result.add(prob);
		}
		return result;
	}

	public static Set<String> generate2(int numOfProblem, Set<String> state, Collection<String> locs, Collection<PT> cs,
			int kind) {
		Set<String> result = new HashSet<String>();
		List<String> ls = new ArrayList<>(locs);
		while (result.size() < numOfProblem) {
			int j = Utils.randomNumber(0, ls.size() - 1);
			String randomLoc1 = ls.get(j);
			String init = getInitState(state, randomLoc1);

			int numOfRandomGoals = Utils.randomNumber(0, 10);
			String destGoals = generateRoutingGoals(cs, ls, numOfRandomGoals);
			String goal = destGoals + "\n";
			goal += "\t(accident-handled)\n";
			String prob = "(define (problem p-" + result.size() + ")\n" + "	(:domain test )\n" + "	(:init \n" + ""
					+ init + " \n" + "	)\n" + "	(:goal (and \n" + "		" + goal + " \n" + "	))\n" + ")";
			result.add(prob);
			System.out.println(prob);
		}
		System.out.println("HERE");
		return result;
	}

	private static String generateRoutingGoals(Collection<PT> cs, List<String> ls, int numOfRandomDes) {
		String destGoals = "";
		Set<Car> selectedCars = new HashSet<Car>();
		List<PT> cars = new ArrayList<>(cs);
		while (selectedCars.size() < numOfRandomDes) {
			int i = Utils.randomNumber(0, cars.size() - 1);
//			int i = ls.size()/2;
			PT c = cars.get(i);
			if (c instanceof Car) {
				selectedCars.add((Car) cars.get(i));
			}
		}
		for (Car c : selectedCars) {
			int j = Utils.randomNumber(0, ls.size() - 1);
			String randomLoc = ls.get(j);
			destGoals += "\t(loc-" + c.getId() + "-" + randomLoc + ")\n";
		}
		return destGoals;
	}

	//
	private static String getInitState(Set<String> state, String randomLoc) {
		StringBuffer init = new StringBuffer();
		for (String s : state) {
			init.append("\t\t" + s + "\n");
		}
		init.append("\n\t\t(accident-" + randomLoc + ")");
		return init.toString();
	}
//
//	public static Set<String> generateRaw(int numOfProblem, Collection<String> locs) {
//		Set<String> result = new HashSet<String>();
//		List<String> ls = new ArrayList<String>(locs);
//		while (result.size() < numOfProblem) {
//			int i = Utils.randomNumber(0, ls.size() - 1);
//			int j = Utils.randomNumber(0, ls.size() - 1);
//			String from = ls.get(i);
//			String to = ls.get(j);
//
//			result.add(from + "-" + to);
//		}
//		return result;
//	}
//	
//	public static String toPDDL(String from, String to) {
//		String prob = "(define (problem p-x)\n" + "	(:domain dr )\n" + "	(:init \n"
//				+ "		(" + from + ") \n" + "	)\n" + "	(:goal (and \n" + "		(" + to + ") \n" + "	))\n"
//				+ ")";
//		return prob;
//	}
}

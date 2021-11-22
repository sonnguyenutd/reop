package v7.gen.generator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gen.utils.Utils;
import v3.gen.generator.DomainGenerator;
import v3.gen.generator.ProblemGenerator;
import v3.gen.obj.Ambulance;
import v3.gen.obj.Car;
import v3.gen.obj.Displayer;
import v3.gen.obj.Drone;
import v3.gen.obj.PS;
import v3.gen.obj.PT;
import v3.gen.obj.ParkingLot;
import v3.gen.obj.PoliceCar;
import v3.gen.obj.Route;
import v3.gen.obj.TCS;

public class ObjectGenerator {
	private static final int MAX_OBJ_PER_CAT = 18;
	public static Set<String> predicates;
	public static int NUM_PARENT_ACTS = 0;
	public static int NUM_ACTS = 0;
	public static int seedNum = 13;

	public static void main(String[] args) throws FileNotFoundException, IOException {
		int size = 10;
		int NUM_OF_LOCS = 50;
		String folder = "/Users/sonnguyen/Desktop/test";
		int NUM_OF_LOTS = 2;
		predicates = new HashSet<>();
		Set<String> acts = new HashSet<>();
		Set<String> parentActs2 = new HashSet<>();
		Set<String> parentActs1 = new HashSet<>();
		Set<String> states = new HashSet<>();
		// For routing
		Car c = null;

		Set<String> locations = Utils.generateStringSet(NUM_OF_LOCS);
		Set<Route> routes = createRoutes(locations);
		Set<ParkingLot> lots = createParkingLots(locations, NUM_OF_LOTS);

		System.out.println(routes.size());
		long start = System.currentTimeMillis();
		Set<PT> objs = generate(MAX_OBJ_PER_CAT, locations, lots, routes);

		objs.addAll(lots);

		for (PT pt : objs) {
			acts.addAll(pt.getActions());
			Set<String> ptActs1 = pt.getParentActions1();
			Set<String> ptActs2 = pt.getParentActions2();
			if (ptActs2 != null)
				parentActs2.addAll(ptActs2);
			if (ptActs1 != null)
				parentActs1.addAll(ptActs1);
			if (pt.getState() != null)
				states.add(pt.getState());
		}
		
		System.out.println("ACTIONS: \t\t" + acts.size());
//		System.out.println("PARENT ACTIONS 1: \t" + parentActs1.size());
		System.out.println("PARENT ACTIONS 2: \t" + parentActs2.size());
		System.out.println("-->" + acts.size() / parentActs2.size());

		if (acts.size() < size * 1000 / 1.2 || acts.size() > size * 1000 * 1.2)
			return;

		System.out.println("HERE: " + (System.currentTimeMillis() - start));

		String originalDomain = DomainGenerator.generate(predicates, acts);
//		String l1Domain = DomainGenerator.generate(predicates, parentActs1);
		String l2Domain = DomainGenerator.generate(predicates, parentActs2);

		int kind = ProblemGenerator.ACCIDENT_RESPONSE;
		Utils.write(folder + "/" + size + "/l0_domain.pddl", originalDomain);
//		Utils.write(folder + "/" + size + "/l1_domain.pddl", l1Domain);
		Utils.write(folder + "/" + size + "/l2_domain.pddl", l2Domain);
		c = selectACar(objs);

		Set<String> probs_3 = ProblemGenerator.generate2(10, states, locations, objs, kind);
		int counter = 1;
		for (String prob : probs_3) {
			Utils.write(folder + "/" + size + "/" + kind + "_prob_" + (counter++) + ".pddl", prob);
		}

	}

	private static Car selectACar(Set<PT> objs) {
		List<PT> pts = new ArrayList<>(objs);
		Collections.shuffle(pts);
		for (PT pt : pts) {
			if (pt instanceof Car)
				return (Car) pt;
		}
		return null;
	}

	private static Set<ParkingLot> createParkingLots(Set<String> locations, int n) {
		List<String> locs = new ArrayList<>(locations);
		Set<ParkingLot> result = new HashSet<>();
		while (result.size() < n) {
			int i = Utils.randomNumber(0, locations.size() - 1);
			result.add(new ParkingLot(locs.get(i)));
		}
		return result;
	}

	public static Set<PT> generate(int maxObjPerCat, Collection<String> ls, Set<ParkingLot> lots, Set<Route> routes) {
		List<String> locs = new ArrayList<>(ls);
		Set<PT> result = new HashSet<PT>();
		Set<Car> allCars = new HashSet<Car>();
		// Ambulance
		int numOfAmbulances = Utils.randomNumber(1, maxObjPerCat);
//		int numOfAmbulances = 1;
		for (int i = 1; i <= numOfAmbulances; i++) {
			int k = Utils.randomNumber(0, locs.size() - 1);
			String loc = locs.get(k);
			Ambulance am = new Ambulance(loc, routes, locs, lots);
			am.setId(i);
			result.add(am);
			allCars.add(am);
		}

		// Car
		int numOfCars = Utils.randomNumber(1, maxObjPerCat);
//		int numOfCars = 5;
		for (int i = 1; i <= numOfCars; i++) {
			int k = Utils.randomNumber(0, locs.size() - 1);
			String loc = locs.get(k);
			Car c = new Car(loc, routes, locs, lots);
			c.setId(i);
			result.add(c);
			allCars.add(c);
		}

		// Displayer
		int numOfDisplayers = Utils.randomNumber(1, maxObjPerCat);
//		int numOfDisplayers = 3;
		Set<Displayer> displayers = new HashSet<Displayer>();
		for (int i = 1; i < numOfDisplayers + 1; i++) {
			displayers.add(new Displayer(locs, i));
		}
		result.addAll(displayers);

		// Drone
		int numOfDrones = Utils.randomNumber(1, maxObjPerCat);
//		int numOfDrones = 1;
		for (int i = 1; i <= numOfDrones; i++) {
			Drone d = new Drone(ls);
			d.setId(i);
			int k = Utils.randomNumber(0, locs.size() - 1);
			d.setLoc(locs.get(k));
			result.add(d);
		}

		// Light

		// Police Car
		int numOfPoliceCars = Utils.randomNumber(1, maxObjPerCat);
//		int numOfPoliceCars = 2;
		for (int i = 1; i <= numOfPoliceCars; i++) {
			int k = Utils.randomNumber(0, locs.size() - 1);
			String loc = locs.get(k);
			PoliceCar am = new PoliceCar(loc, routes, locs, lots);
			am.setId(i);
			result.add(am);
			allCars.add(am);
		}

		// ParkingSystem
		int numOfParkingSystem = 1;
		for (int i = 1; i <= numOfParkingSystem; i++) {
			PS system = new PS(lots, allCars);
			system.setId(i);
			result.add(system);
		}
		// TrafficControlSystem
		int numOfTrafficControlSystem = 1;
		for (int i = 1; i <= numOfTrafficControlSystem; i++) {
			TCS system = new TCS(routes, locs, allCars, displayers, null);
			system.setId(i);
			result.add(system);
		}

		return result;
	}

	private static Set<Route> createRoutes(Set<String> ls) {
		Set<Route> result = new HashSet<>();
		for (String l1 : ls) {
			for (String l2 : ls) {
				if (!l1.equals(l2)) {
					int r = Utils.randomNumber(0, seedNum);
					if (r % seedNum != 0) {
						Route route = new Route(l1, l2);
						result.add(route);
					}
				}
			}
		}
		return result;
	}
}

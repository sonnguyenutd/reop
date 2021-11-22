package v4.gen.generator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gen.utils.Utils;
import v3.gen.generator.DomainGenerator;
import v3.gen.obj.PT;
import v3.gen.obj.Route;
import v4.gen.obj.Frame;
import v4.gen.obj.Order;
import v4.gen.obj.Tag;
import v4.gen.obj.WareHouse;

public class ObjectGenerator {
	public static Set<String> predicates;
	public static int NUM_PARENT_ACTS = 0;
	public static int NUM_ACTS = 0;
	public static int seedNum = 5;

	public static void main(String[] args) throws FileNotFoundException, IOException {
		int NUM_OF_LOCS = 5;
		predicates = new HashSet<>();

		List<String> locations = new ArrayList<>(Utils.generateStringSet(NUM_OF_LOCS));
		Set<Route> routes = createRoutes(locations);
		Set<WareHouse> whs = new HashSet<>();
		int numOfWarehouses = 1;
		for (int i = 1; i <= numOfWarehouses; i++) {
			int r = Utils.randomNumber(0, locations.size() - 1);
			WareHouse wh = new WareHouse(locations.get(r));
			locations.remove(r);
			whs.add(wh);
		}

		Set<PT> objs = generate(10, locations, routes, whs);

		objs.addAll(whs);

		Set<String> acts = new HashSet<>();
		Set<String> parentActs2 = new HashSet<>();
		Set<String> parentActs1 = new HashSet<>();
		Set<String> states = new HashSet<>();
		Set<Order> orders = new HashSet<>();

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
			if (pt instanceof Order)
				orders.add((Order) pt);
		}

		System.out.println("ACTIONS: \t\t" + acts.size());
		System.out.println("PARENT ACTIONS 1: \t" + parentActs1.size());
		System.out.println("PARENT ACTIONS 2: \t" + parentActs2.size());

		String domain = DomainGenerator.generate(predicates, acts);
//
		Set<String> probs = ProblemGenerator.generate(1, states, orders);
		for (String prob : probs) {
			System.out.println(prob);
		}
		System.out.println("=======");
		System.out.println(domain);

	}

	public static Set<PT> generate(int maxObjPerCat, Collection<String> ls, Set<Route> routes, Set<WareHouse> whs) {
		List<String> locs = new ArrayList<>(ls);
		Set<PT> result = new HashSet<PT>();

		int numOfTags = Utils.randomNumber(1, maxObjPerCat);
//		int numOfTags = 1;
		Set<Tag> tags = new HashSet<>();
		for (int i = 1; i <= numOfTags; i++) {
			Tag t = new Tag();
			t.setId(i);
			tags.add(t);
		}
		Set<Frame> frames = new HashSet<>();
		int numOfFrames = Utils.randomNumber(1, maxObjPerCat);
//		int numOfFrames = 1;
		for (int i = 1; i <= numOfFrames; i++) {
			Frame f = new Frame();
			f.setId(i);
			frames.add(f);
		}
		Set<Order> orders = new HashSet<>();
		int numOfOrders = 1;
//		int numOfOrders = Utils.randomNumber(1, maxObjPerCat);
		for (int i = 1; i <= numOfOrders; i++) {
			String des = locs.get(Utils.randomNumber(0, locs.size() - 1));
			Order o = new Order(des);
			o.setId(i);
			orders.add(o);
		}
		// SET THE REST
		for (Order order : orders) {
			order.setFrames(frames);
			result.add(order);
		}

		for (Frame f : frames) {
			f.setOrders(orders);
			f.setTags(tags);
			result.add(f);
		}

		for (Tag t : tags) {
			t.setLocs(locs);
			t.setOrders(orders);
			t.setRoutes(routes);
			t.setWarehouses(whs);
			result.add(t);
		}

		return result;
	}

	private static Set<Route> createRoutes(Collection<String> ls) {
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

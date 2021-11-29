package v8.gen.parser.logistics.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gen.utils.Utils;
import v5.gen.supply_chain.Action;
import v8.gen.parser.logistics.DRIVETRUCK;
import v8.gen.parser.logistics.FLYAIRPLANE;
import v8.gen.parser.logistics.LOADAIRPLANE;
import v8.gen.parser.logistics.LOADTRUCK;
import v8.gen.parser.logistics.UNLOADAIRPLANE;
import v8.gen.parser.logistics.UNLOADTRUCK;

public class ReducedProblemConstructor {
	private static final String SEPARATOR = ";;;";

	public static void main(String[] args) {
		String prob = "../benchmark-1/logistics-large-simple/goal-1/p-a1-c1-s1000-p10-t1-g1.txt";
		String mapFile = "../benchmark-1/logistics-large-simple/goal-1/p-a1-c1-s1000-p10-t1-g1_map.txt";
		String outFile_1 = "../benchmark-1/logistics-large-simple/goal-1/p-a1-c1-s1000-p10-t1-g1_p_prob.out-1";
		String outFile_2 = "../benchmark-1/logistics-large-simple/goal-1/p-a1-c1-s1000-p10-t1-g1_p_prob.out-2";

		Map<String, Set<Action>> map = parseMap(mapFile);

		List<String> steps = parseSteps(outFile_1);

		
		// the structure is just for this project
		int load = findStep("load-truck", steps, 0);
		String prefixLoad = extractPrefix(steps.get(load));
		if (load != -1 && prefixLoad != null) {
			int unload = findStep("unload-truck", steps, load);
			if (unload != -1) {
				String prefixUnload = extractPrefix(steps.get(load));
				if (prefixLoad.equals(prefixUnload)) {
					System.out.println(steps.get(load) + "-->" + steps.get(unload));
					Set<Action> l = map.get(steps.get(load));
					Set<Action> u = map.get(steps.get(unload));
					contructTempProb(Utils.selectAnElm(l), Utils.selectAnElm(u), prob);
				}
			}
		}
//		constructReducedDom(steps, prob, map);
	}

	private static void contructTempProb(Action l, Action u, String prob) {
		System.out.println(l.getPreconds());
		System.out.println(u.getPreconds());
	}

	private static String extractPrefix(String step) {
		int i = step.lastIndexOf("_");
		if (i != -1) {
			return step.substring(i);
		}
		return null;
	}

	private static int findStep(String startWith, List<String> steps, int start) {
		for (int i = start; i < steps.size(); i++) {
			if (steps.get(i).startsWith(startWith)) {
				return i;
			}
		}
		return -1;
	}

	private static void constructReducedDom(Set<String> steps, String prob, Map<String, Set<Action>> map) {
		Set<Action> acts = new HashSet<>();
		for (String parent : steps)
			acts.addAll(map.get(parent));
		String domain = Utils.toDomain(acts, "test");
		Utils.write(prob.replace(".pddl", "_c_domain.pddl"), domain);
//		System.out.println(domain);
	}

	private static List<String> parseSteps(String outFile) {
		String content = Utils.read(outFile);
		List<String> result = new ArrayList<>();
		String[] lines = content.split("\n");
		for (String l : lines) {
			l = l.trim();
			if (l.contains(": (")) {
				int s = l.indexOf(": (") + ": (".length();
				int e = l.indexOf(")");
				result.add(l.substring(s, e).trim().toLowerCase());
			} else if (l.contains(": ")) {
				int s = l.indexOf(": ") + ": ".length();
				result.add(l.substring(s, l.length()).trim().toLowerCase());
			}
		}
		return result;
	}

	public static Map<String, Set<Action>> parseMap(String mapFile) {
		Map<String, Set<Action>> result = new HashMap<>();
		String content = Utils.read(mapFile);
		Set<Action> currSet = new HashSet<>();
		int i, j = 0;
		while (true) {
			i = content.indexOf(SEPARATOR, j + 1);
			if (i == -1)
				break;
			String actsTxt = content.substring(j, i);
			j = content.indexOf("\n", i);
			if (j == -1)
				break;
			String parentAct = content.substring(i, j).replace(SEPARATOR, "").trim();
			currSet = parseActs(actsTxt);
			result.put(parentAct, currSet);
		}
		return result;
	}

	private static Set<Action> parseActs(String actsTxt) {
		Set<Action> result = new HashSet<>();
		String[] parts = actsTxt.split("\\(:action");
		for (String as : parts) {
			as = as.trim();
			if (as.isEmpty())
				continue;
			String[] lines = as.split("\n");
			Action act = null;
			if (lines[0].startsWith("drive-truck"))
				act = new DRIVETRUCK(lines[0]);
			else if (lines[0].startsWith("fly-airplane"))
				act = new FLYAIRPLANE(lines[0]);
			else if (lines[0].startsWith("load-truck"))
				act = new LOADTRUCK(lines[0]);
			else if (lines[0].startsWith("unload-truck"))
				act = new UNLOADTRUCK(lines[0]);
			else if (lines[0].startsWith("load-airplane"))
				act = new LOADAIRPLANE(lines[0]);
			else if (lines[0].startsWith("unload-airplane"))
				act = new UNLOADAIRPLANE(lines[0]);
			if (act != null)
				result.add(act);
		}
		return result;
	}
}

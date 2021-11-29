package v8.gen.parser.childsnack.group;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import gen.utils.Utils;
import v5.gen.supply_chain.Action;
import v8.gen.parser.visitall.Move;

public class ReducedProblemConstructor {

	private static final String SEPARATOR = ";;;";

	public static void main(String[] args) {
		
		String prob = "../benchmark-1/rovers-large-simple/goal-2/p-r1-w1500-o1-1-g2.pddl";
		String mapFile = "../benchmark-1/rovers-large-simple/goal-2/p-r1-w1500-o1-1-g2_map.txt";
		String outFile = "../benchmark-1/rovers-large-simple/goal-2/p-r1-w1500-o1-1-g2.out";
		
		Map<String, Set<Action>> map = parseMap(mapFile);
		Set<String> steps = parseSteps(outFile);
		constructReducedDom(steps, prob, map);
	}

	private static void constructReducedDom(Set<String> steps, String prob, Map<String, Set<Action>> map) {
		Set<Action> acts = new HashSet<>();
		for (String parent : steps)
			acts.addAll(map.get(parent));
		String domain = Utils.toDomain(acts, "test");
		Utils.write(prob.replace(".pddl", "_c_domain.pddl"), domain);
//		System.out.println(domain);
	}

	private static Set<String> parseSteps(String outFile) {
		String content = Utils.read(outFile);
		Set<String> result = new HashSet<>();
		String[] lines = content.split("\n");
		for (String l : lines) {
			if (l.contains(": (")) {
				int s = l.indexOf(": (") + ": (".length();
				int e = l.indexOf(")");
				result.add(l.substring(s, e).trim());
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
			Move m = new Move(lines[0]);
			result.add(m);
		}
		return result;
	}

}

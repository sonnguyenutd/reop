package v8.gen.parser.visitall.group;

import java.io.File;
import java.util.Collection;
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
		String dir = "../benchmark-1/visitall-multidimensional";
		Collection<File> allFiles = Utils.listFileTree(new File(dir));
		for (File opsFile : allFiles) {
			if (opsFile.getName().endsWith("ops.txt")) {
				String opsFilePath = opsFile.getAbsolutePath();
				System.out.println(opsFilePath);
				String probFile = opsFilePath.replace("_ops.txt", "_p_prob.pddl");
				String mapFile = opsFilePath.replace("_ops.txt", "_map.txt");
				String outFile = opsFilePath.replace("_ops.txt", ".out");

				Map<String, Set<Action>> map = parseMap(mapFile);
				Set<String> steps = parseSteps(outFile);
				constructReducedDom(steps, probFile, map);
				System.out.println("STEPS: " + steps.size());
				System.out.println("---------------------");
			}
		}
	}

	private static void constructReducedDom(Set<String> steps, String prob, Map<String, Set<Action>> map) {
		Set<Action> acts = new HashSet<>();
		for (String parent : steps)
			acts.addAll(map.get(parent));
		String domain = Utils.toDomain(acts, "test");
		Utils.write(prob.replace(".pddl", "_c_domain.pddl"), domain);
//		System.out.println(domain);
	}

	static Set<String> parseSteps(String outFile) {
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

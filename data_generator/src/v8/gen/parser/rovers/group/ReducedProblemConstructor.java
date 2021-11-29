package v8.gen.parser.rovers.group;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import gen.utils.Utils;
import v5.gen.supply_chain.Action;
import v8.gen.parser.rovers.Calibrate;
import v8.gen.parser.rovers.CommunicateImageData;
import v8.gen.parser.rovers.CommunicateRockData;
import v8.gen.parser.rovers.CommunicateSoilData;
import v8.gen.parser.rovers.Drop;
import v8.gen.parser.rovers.Naviage;
import v8.gen.parser.rovers.SampleRock;
import v8.gen.parser.rovers.SampleSoil;
import v8.gen.parser.rovers.TakeImage;

public class ReducedProblemConstructor {

	private static final String SEPARATOR = ";;;";

	public static void main(String[] args) {
//		String prob = "../benchmark-1/visitall-multidimensional/3-dim-visitall-CLOSE-g1/p3_m.pddl";
//		String mapFile = "../benchmark-1/visitall-multidimensional/3-dim-visitall-CLOSE-g1/p3_m_map.txt";
//		String outFile = "../benchmark-1/visitall-multidimensional/3-dim-visitall-CLOSE-g1/p3_m.out";

		String prob = "../benchmark-1/rovers-large-simple/goal-2/p-r1-w1500-o1-1-g2.pddl";
		String mapFile = "../benchmark-1/rovers-large-simple/goal-2/p-r1-w1500-o1-1-g2_map.txt";
		String outFile = "../benchmark-1/rovers-large-simple/goal-2/p-r1-w1500-o1-1-g2.out";

		Set<String> predicates = Utils.extractPredicates(prob.replace(".pddl", "_p_prob.pddl"));

		Map<String, Set<Action>> map = parseMap(mapFile);
		Set<String> steps = parseSteps(outFile);
		System.out.println(steps);
		constructReducedDom(steps, prob, map, predicates);
	}

	private static void constructReducedDom(Set<String> steps, String prob, Map<String, Set<Action>> map,
			Set<String> predicates) {
		Set<Action> acts = new HashSet<>();
		for (String parent : steps) {
			Set<Action> as = map.get(parent);
			if (as != null) {
				acts.addAll(as);
			} else
				System.out.println("---->" + parent);
		}
		String domain = Utils.toDomain(acts, "test", predicates);
		Utils.write(prob.replace(".pddl", "_c_domain.pddl"), domain);
//		System.out.println(domain);
	}

	private static Set<String> parseSteps(String outFile) {
		String content = Utils.read(outFile);
		Set<String> result = new HashSet<>();
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
			result.put(parentAct.toLowerCase(), currSet);
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
			if (lines[0].startsWith("navigate"))
				act = new Naviage(lines[0]);
			else if (lines[0].startsWith("sample_rock"))
				act = new SampleRock(lines[0]);
			else if (lines[0].startsWith("drop"))
				act = new Drop(lines[0]);
			else if (lines[0].startsWith("calibrate"))
				act = new Calibrate(lines[0]);
			else if (lines[0].startsWith("take_image"))
				act = new TakeImage(lines[0]);
			else if (lines[0].startsWith("communicate_image_data"))
				act = new CommunicateImageData(lines[0]);
			else if (lines[0].startsWith("sample_soil"))
				act = new SampleSoil(lines[0]);
			else if (lines[0].startsWith("communicate_soil_data"))
				act = new CommunicateSoilData(lines[0]);
			else if (lines[0].startsWith("communicate_rock_data"))
				act = new CommunicateRockData(lines[0]);
			if (act != null)
				result.add(act);
		}
		return result;
	}

}

package analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import analysis.domain.service.Predicate;
import analysis.domain.service.grounding.GroundedFunct;
import analysis.domain.service.obj.Obj;
import analysis.utils.Utils;

public class ProblemParser {
	public static Set<String> INIT_ALL = new HashSet<>();

	public static void main(String[] args) {
		String prob = Utils.readPDDL("/Users/sonnguyen/Desktop/test/p11.pddl");
//		System.out.println(prob);
		Map<String, Set<Obj>> objs_map = parseObjs(prob);
		Set<Obj> objs = new HashSet<>();
		for (String t : objs_map.keySet()) {
			objs.addAll(objs_map.get(t));
		}
		parseObjInit(prob, objs);

		for (Obj obj : objs) {
			System.out.println(obj.name);
			System.out.println(obj.properties);
			String dummyAction = convertObjToDummyAct(obj);
			System.out.println(dummyAction);
			System.out.println(";;------------");
		}

	}

	public static String convertObjToDummyAct(Obj obj) {
		if (obj.properties.isEmpty())
			return "";
		String result = "(:action dummy-" + obj.type + "-" + obj.name + "\n";
		result += "\t:parameters (?dummy - " + obj.type + " )\n";
		result += "\t:precondition  (and " + "(unselected-" + obj.type + " ?dummy" + " )" + " )\n";
		result += "\t:effect (and \n" + "\t\t(not (unselected-" + obj.type + " ?dummy" + " ))\n";
		String effs = "";
		for (String ef : obj.properties) {
			if (ef.contains("(= "))
				ef = ef.replace("(= ", "(assign ");
			ef = ef.replace(" " + obj.name, " ?dummy");
			effs += "\t\t" + ef + "\n";
		}
		result += effs;
		result += "\t)\n";
		result += " )\n";

		return result;
	}

	// Each prop is in a line
	public static Set<Obj> parseObjInit(String prob, Set<Obj> objs) {
		int start = prob.indexOf("(:init");
		if (start == -1)
			return null;
		int end = Utils.findClosingParen(prob.toCharArray(), start);
		if (end == -1)
			return null;
		String[] lines = prob.substring(start + "(:init".length(), end).trim().split("\n");
		for (String line : lines) {
			line = line.trim();
			if (!line.isEmpty()) {

				String name = null;
				String[] parts = null;
				if (!line.contains("=")) {
					parts = line.split(" ");
				} else {
					int i = line.indexOf("(= (") + "(= (".length();
					int j = Utils.findClosingParen(line.toCharArray(), i);
					parts = line.substring(i, j).split(" ");
				}
				if (parts.length > 1)
					name = parts[1].replace(")", "").trim();
				if (name != null) {
					Obj o = getObjByName(name, objs);
					objs.remove(o);
					o.addProperty(line);
					objs.add(o);
				} else {
					INIT_ALL.add(line);
				}
			}
		}
		return objs;
	}

	public static Obj getObjByName(String name, Collection<Obj> objs) {
		for (Obj o : objs) {
			if (o.name.equalsIgnoreCase(name))
				return o;
		}
		return null;
	}

	// Each type is in a line
	public static Map<String, Set<Obj>> parseObjs(String prob) {
		Map<String, Set<Obj>> result = new HashMap<>();
		int start = prob.indexOf("(:objects");
		if (start == -1)
			return null;
		int end = Utils.findClosingParen(prob.toCharArray(), start);
		if (end == -1)
			return null;
		String[] lines = prob.substring(start + "(:objects".length(), end).trim().split("\n");
		for (String line : lines) {
			line = line.trim();
			if (!line.isEmpty()) {
				String[] parts = line.split(" ");
				String type = parts[parts.length - 1];
				Set<Obj> objs = new HashSet<>();
				for (int i = 0; i < parts.length - 2; i++) {
					String name = parts[i];
					Obj o = new Obj(type, name);
					objs.add(o);
				}
				result.put(type, objs);
			}
		}
		return result;
	}

	public static List<GroundedFunct> extractGoals(String prob, Set<Obj> objs) {
		List<GroundedFunct> goals = new ArrayList<>();
		int start = prob.indexOf("(:goal (and");
		if (start < 0)
			return null;
		start = start + "(:goal (and".length();
		int end = Utils.findClosingParen(prob.toCharArray(), start);
		String goalsTxt = prob.substring(start, end - 1).trim();
		String[] lines = goalsTxt.split("\n");
		for (String l : lines) {
			GroundedFunct g = parseProposition(l, objs);
			System.out.println(l);
			System.out.println(g);
			System.out.println("----------");
			goals.add(g);
		}
		return goals;
	}

	public static GroundedFunct parseProposition(String propTxt, Set<Obj> objs) {
//		(loc ?p - pt ?l - location)
		propTxt = propTxt.trim();
		if (propTxt.isEmpty())
			return null;
		propTxt = propTxt.trim().replace("(", "").replace(")", "");
		int endingName = propTxt.indexOf(" ");
		String name = propTxt.substring(0, endingName).trim();

		Predicate pred = new Predicate(name);
		GroundedFunct result = new GroundedFunct(pred);

		String[] parts = propTxt.split(" ");
		if (parts.length > 1)
			for (int i = 1; i < parts.length; i++) {
				Obj o = getObjByName(parts[i], objs);
				result.addObjs(o);
			}
		return result;
	}
}

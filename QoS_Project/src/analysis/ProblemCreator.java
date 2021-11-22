package analysis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import analysis.domain.service.Provider;
import analysis.domain.service.obj.Obj;
import analysis.utils.Utils;

public class ProblemCreator {

	public static String generateUnselectedPredicates(Map<String, Set<Provider>> reqiredProviders) {
		String result = "";
		for (String type : reqiredProviders.keySet()) {
			result += "(unselected-" + type + " ?dummy)\n";
		}
		return result;
	}

	public static String generateInitStateOfRequiredProviders(Map<String, Set<Provider>> reqiredProviders) {
		String result = "";
		for (String type : reqiredProviders.keySet()) {
			for (Provider pro : reqiredProviders.get(type)) {
				result += "\t(unselected-" + type + " " + pro.name + ")\n";
			}
		}
		return result;
	}

	public static String createRequiredProviders(Map<String, Set<Provider>> reqiredProviders) {
		String result = "(:constants \n";
		for (String type : reqiredProviders.keySet()) {
			String providers = "";
			for (Provider pro : reqiredProviders.get(type)) {
				providers += pro.name + " ";
			}
			result += "\t" + providers + "- " + type + "\n";

		}
		result += "\n)";
		return result;
	}

	public static String createDummyActions(String prob, Set<Obj> objs, Set<String> requiredProviderTypes) {
		String result = ";;----------DUMMY ACTIONS-----------\n";
		for (Obj obj : objs) {
			if (requiredProviderTypes.contains(obj.type)) {
				String dummyAction = ProblemParser.convertObjToDummyAct(obj);
				result += dummyAction + "\n";
			}
		}
		return result;
	}

	public static void createDomain(String dummyActions, String newPredicates, String domain, String newDomainFile) {
		String newDomain = domain.trim();
		int endDomain = newDomain.lastIndexOf(")");
		newDomain = newDomain.substring(0, endDomain);
//		System.out.println(newDomain);
		newDomain += dummyActions;
		newDomain = newDomain + "\n)";

		newPredicates = "(:predicates\n\t\t" + newPredicates.replace("\n", "\n\t\t");
		newDomain = newDomain.replace("(:predicates", newPredicates);

		Utils.write(newDomainFile, newDomain);
	}

	public static void createProblem(Map<String, Set<Provider>> reqiredProviders, Map<String, Set<Obj>> objs_map,
			String oldProb, String newProbFile) {
		String rquiredObjs = "(:objects \n";
		String newProb = oldProb.trim();
		for (String type : reqiredProviders.keySet()) {
			String providers = "";
			for (Provider pro : reqiredProviders.get(type)) {
				providers += pro.name + " ";
			}
			newProb = removeAllObjsByType(newProb, type);
			rquiredObjs += "\t" + providers + "- " + type + "\n";
			newProb = removeObjState(newProb, objs_map.get(type));
		}
		newProb = newProb.replace("(:objects", rquiredObjs);

		String unselectedState = ProblemCreator.generateInitStateOfRequiredProviders(reqiredProviders);
		newProb = newProb.replace("(:init", "(:init\n" + unselectedState + "\n");
		
		newProb = removeUnusedProviders(reqiredProviders, objs_map, newProb);
		
		Utils.write(newProbFile, newProb);
	}

	private static String removeUnusedProviders(Map<String, Set<Provider>> reqiredProviders,
			Map<String, Set<Obj>> objs_map, String newProb) {
		//TODO: Remove unused providers.
		return newProb;
	}

	private static String removeObjState(String newProb, Set<Obj> requiredObjs) {
		for (Obj o : requiredObjs) {
			List<String> state = o.properties;
			for (String s : state) {
				newProb = newProb.replace(s, "");
			}
		}
		return newProb;
	}

	private static String removeAllObjsByType(String prob, String type) {
		String[] lines = prob.trim().split("\n");
		String result = "";
		for (String line : lines) {
			if (!line.contains(" - " + type) && !line.trim().isEmpty()) {
				result += line + "\n";
			}
//			else {
//				System.out.println(line);
//			}
//			if(line.trim().startsWith("(:init"))
//				break;

		}
		return result;
	}

}

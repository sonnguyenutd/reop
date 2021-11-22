package v2.gen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gen.utils.Utils;

public class Generator {
	static int NUM_OF_PREDICATES = 100;
	static int MAX_PRECONDS = 3;
	static int MAX_P_EFFECTS = 5;
	static int MAX_N_EFFECTS = MAX_PRECONDS;
	static int NUM_OF_ACTIONS = 100000;

	public static void main(String[] args) {
		List<Action> actions = generateActions();
		System.out.println(actions.size());
		Map<Set<String>, List<Action>> groups = new HashMap<>();
		for (Action a : actions) {
			List<Action> acts = groups.get(a.getPreconds());
			if (acts == null)
				acts = new ArrayList<>();
			acts.add(a);
			groups.put(a.getPreconds(), acts);
		}
		System.out.println(groups.size());
		System.out.println(actions.size() / (double) groups.size());
	}

	private static List<Action> generateActions() {
		List<Action> result = new ArrayList<>();
		int totalSize = 0;
		int counter = NUM_OF_PREDICATES;
		while (result.size() < NUM_OF_ACTIONS) {
			List<String> allPredicate = generateAllPredicates(NUM_OF_PREDICATES);
			int numPre = Utils.randomNumber(2, MAX_PRECONDS);
			Set<String> preconds = selectRandomSet(allPredicate, numPre, null);
//		
			int numEff = Utils.randomNumber(1, MAX_P_EFFECTS);
			Set<String> pEffects = selectRandomSet(allPredicate, numEff, preconds);

			int numNEff = Utils.randomNumber(0, MAX_N_EFFECTS);
			Set<String> nEffects = selectRandomSet(allPredicate, numNEff, pEffects);
			Action a = new Action("act-" + (counter++), preconds, pEffects, nEffects);
//			if (!result.contains(a))
			result.add(a);
		}
		System.out.println(totalSize / (double) result.size());
		return result;
	}

	private static List<String> generateAllPredicates(int numOfPredicate) {
		List<String> result = new ArrayList<>();
		List<String> parts = Arrays.asList("A B C D E F G H I J K M L N O P Q R S T U V W X Y Z".split(" "));
		for (String p1 : parts) {
			for (String p2 : parts) {
				for (String p3 : parts) {
					result.add(p1 + p2 + p3);
					if (result.size() > numOfPredicate)
						return result;
				}
			}
		}
		return result;
	}

	private static Set<String> selectRandomSet(List<String> allPredicate, int numOfPredicates, Set<String> exclusives) {
		Set<String> result = new HashSet<>();
		int counter = 0;
		List<String> allCands = new ArrayList<>(allPredicate);
		if (exclusives != null)
			allCands.removeAll(exclusives);
		while (result.size() < numOfPredicates && counter++ < 300) {
			int i = Utils.randomNumber(0, allCands.size());
			result.add(allCands.get(i));
		}
		return result;
	}

}

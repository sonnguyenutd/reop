package v5.gen.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import gen.utils.Utils;
import v5.gen.supply_chain.Action;
import v5.gen.supply_chain.ChainGeneratorTLP;
import v5.gen.supply_chain.Config;
import v5.gen.supply_chain.Part;
import v5.gen.supply_chain.Transportation;
import v5.gen.transport2.Transporter;

public class Analyzer {

	public static final int MAX_LEVEL = 5;
	private static final int MAX_T = 4;
	private static final int MAX_ANALYSIS_TASKS = 10 * 1000;

	public static void main(String[] args) throws InterruptedException {
		int maxDeep = 5;
		int maxBrand = 5;
		int size = 500000;
		while (true) {
			System.out.println("-----------------");
			String partName = Utils.getAlphaNumericString(Config.MAX_PART_LEN);
			Part finalProduct = ChainGeneratorTLP.generate(partName, maxDeep, maxBrand);
			Set<Part> allParts = ChainGeneratorTLP.getAllParts(finalProduct);
			System.out.println(allParts.size());

			if (allParts.size() > 30)
				continue;
			Collection<Action> steps = ChainGeneratorTLP.getAllSteps(allParts);
			List<Transportation> allTrans = ChainGeneratorTLP.getAllTransportations(allParts);
			List<Transporter> transporters = ChainGeneratorTLP.generateTransporters();
			List<Action> allMoves = ChainGeneratorTLP.getAllMoves(transporters, allTrans, allParts);

			System.out.println(allMoves.size());

			if (allMoves.size() < size / 1.1 || allMoves.size() > size * 1.1)
				continue;

			long start = System.currentTimeMillis();

			ExecutorService pool2 = Executors.newFixedThreadPool(MAX_T);
			ExecutorService pool1 = Executors.newFixedThreadPool(MAX_T);

			List<AnalysisThread> threads = new ArrayList<AnalysisThread>();
			int counter = 0;
			AnalysisThread t = new AnalysisThread(counter++);
			for (int i = 0; i < allMoves.size(); i++) {
				t.addAction(allMoves.get(i));
				if (t != null && t.getActions().size() == MAX_ANALYSIS_TASKS) {
					threads.add(t);
					t = new AnalysisThread(counter++);
				}
			}
			threads.add(t);

			for (AnalysisThread thread : threads)
				pool1.execute(thread);
			pool1.shutdown();
			pool1.awaitTermination(20, TimeUnit.MINUTES);
			System.out.println("TIME1: " + (System.currentTimeMillis() - start));

			start = System.currentTimeMillis();
			List<CombiningThread> combiningThreads = combineThreads(threads);
			System.out.println("combiningThreads Size: " + combiningThreads.size());
			for (CombiningThread combiningThread : combiningThreads)
				pool2.execute(combiningThread);
			pool2.shutdown();
			pool2.awaitTermination(10, TimeUnit.MINUTES);
			System.out.println("TIME2: " + (System.currentTimeMillis() - start));

			break;

// start = System.currentTimeMillis();
//		Action act = allMoves.get(0);
//		System.out.println(act.getDependents().size());
//		Set<Action> allDependents = act.getAllDependents(Analyzer.MAX_LEVEL);
//		System.out.println(allDependents.size());
//		System.out.println("TIME2: " + (System.currentTimeMillis() - start));
		}
	}

	private static List<CombiningThread> combineThreads(List<AnalysisThread> threads) {
		List<CombiningThread> result = new ArrayList<>();
		int counter = 0;
		for (int i = 0; i < threads.size(); i++) {
			AnalysisThread t1 = threads.get(i);
			for (int j = i + 1; j < threads.size() - 1; j++) {
				AnalysisThread t2 = threads.get(j);
				List<Action> acts1 = t1.getActions();
				List<Action> acts2 = t2.getActions();
				CombiningThread thread = new CombiningThread(acts1, acts2, counter++);
				result.add(thread);
			}
		}
		return result;
	}

	public static void combineLists(List<Action> acts1, List<Action> acts2) {
		for (Action a1 : acts1) {
			for (Action a2 : acts2) {
				if (isIntersected(a1.getEffects(), a2.getPreconds()))
					a1.addDependent(a2);
				else if (isIntersected(a2.getEffects(), a1.getPreconds()))
					a2.addDependent(a1);
			}
		}
	}

	private static List<AnalysisThread> createThreads(int numOfThreads) {
		List<AnalysisThread> result = new ArrayList<>();
		int counter = 0;
		while (result.size() < numOfThreads)
			result.add(new AnalysisThread(counter++));
		return result;
	}

	public static void identifyDirectDependents(List<Action> acts) {
		for (int i = 0; i < acts.size(); i++) {
			Action a1 = acts.get(i);
			for (int j = i + 1; j < acts.size() - 1; j++) {
				Action a2 = acts.get(j);
				if (isIntersected(a1.getEffects(), a2.getPreconds()))
					a1.addDependent(a2);
				else if (isIntersected(a2.getEffects(), a1.getPreconds()))
					a2.addDependent(a1);
			}

		}
	}

	private static Set<Action> identifyDependentsBackup(Action act, List<Action> acts, int level) {
		Set<Action> result = new HashSet<>();
//		System.out.println(level + "--" + acts.size());
		if (level > MAX_LEVEL)
			return result;
		List<Action> tempActs = new ArrayList<>(acts);
		tempActs.remove(act);
		List<Action> remainingActs = new ArrayList<>();
		for (Action other : tempActs) {
			if (isIntersected(act.getEffects(), other.getPreconds()))
				result.add(other);
			else
				remainingActs.add(other);
		}
		Set<Action> indirectDependents = new HashSet<>();
		System.out.println("------>" + result.size());
		for (Action child : result) {
			Set<Action> dependents = identifyDependentsBackup(child, remainingActs, level + 1);
			indirectDependents.addAll(dependents);
		}
		result.addAll(indirectDependents);
		act.setCurrentLevel(MAX_LEVEL - level);
		return result;
	}

	public static boolean isIntersected(Set<String> set1, Set<String> set2) {
		Set<String> temp = new HashSet<String>(set1);
		temp.retainAll(set2);
		return !temp.isEmpty();
	}

}

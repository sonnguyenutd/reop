package v5.gen.transport2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gen.utils.Utils;
import v5.gen.supply_chain.Action;

public class Generator {
	public static final int LOC_IN_LEVEL_BASE = 4;
	public static final int TRANS_BASE = 4;
	public static final int BRANCHING_FACTOR = 2;
	public static final int MAX_TRANS_FACTOR = 2;

	private static final int MAX_PACKAGES = 3;

	public static void main(String[] args) {
		int maxLevel = 4;
		List<Level> ls = generateLevels(maxLevel);

		Set<Package> packs = generatePackages(ls.get(maxLevel - 1).getLocs());
		System.out.println("packs: " + packs.size());

		List<Transporter> trans = generateTransporters(ls, packs);

		List<Action> acts = new ArrayList<>();
		for (Transporter t : trans)
			acts.addAll(t.getActions());
		System.out.println(acts.size());

//		String domain = Utils.toDomain(acts, "test");
//		Utils.write("/Users/sonnguyen/Desktop/test_2/domain1.pddl", domain);
	}

	private static List<Transporter> generateTransporters(List<Level> ls, Set<Package> packs) {
		List<Transporter> trans = new ArrayList<>();
		for (int i = 0; i < ls.size(); i++) {
			Level l = ls.get(i);
			l.connect();
			int maxTrans = (int) Math.pow(TRANS_BASE, (ls.size() - i)) * Utils.randomNumber(1, MAX_TRANS_FACTOR);
			int numOfTrans = Utils.randomNumber(1, maxTrans);
			System.out.println("numOfTrans at LEVEL " + i + ": " + numOfTrans + "\tlocs: " + l.getLocs().size());
			l.generateTransporters(numOfTrans, packs);
			trans.addAll(l.getTrans());
		}
		return trans;
	}

	private static List<Level> generateLevels(int maxLevel) {
		List<Level> ls = new ArrayList<>();
		for (int i = 0; i < maxLevel; i++) {
			Level currentLevel = new Level(i);
			int max = (int) Math.pow(LOC_IN_LEVEL_BASE, (maxLevel - i));
			int numOfLocs = Utils.randomNumber(1, max);
			System.out.println("LEVEL-" + i + ":" + numOfLocs);
			while (currentLevel.size() < numOfLocs) {
				String locName = "l" + i + "-" + Utils.generateLoc();
				Loc l = new Loc(locName);
				currentLevel.addLoc(l);
			}
			ls.add(currentLevel);
		}
		// Connect higher level to lower level
		for (int i = maxLevel - 1; i >= 1; i--) {
			List<Loc> currLevLocs = ls.get(i).getLocs();
			List<Loc> prevLevLocs = ls.get(i - 1).getLocs();
			for (Loc curLoc : currLevLocs) {
				List<Loc> availableLocs = getAvailableLocs(prevLevLocs, i);
				if (!availableLocs.isEmpty())
					curLoc.setChildren(availableLocs);
			}
			removeUnusedLocs(prevLevLocs);
		}

		return ls;
	}

	private static Set<Package> generatePackages(List<Loc> locs) {
		int numOfPackages = Utils.randomNumber(1, MAX_PACKAGES);
		Set<Package> result = new HashSet<>();
		while (result.size() < numOfPackages) {
			String packName = "PACK-" + Utils.getAlphaNumericString(3);
			Loc rCurrLoc = locs.get(Utils.randomNumber(0, locs.size() - 1));
			Loc desLoc = locs.get(Utils.randomNumber(0, locs.size() - 1));
			Package p = new Package(packName, rCurrLoc, desLoc);
			result.add(p);
		}
		return result;
	}

	private static void removeUnusedLocs(List<Loc> prevLocs) {
		List<Loc> result = new ArrayList<>();
		for (Loc loc : prevLocs) {
			if (loc.getLevel() == -1) {
				result.add(loc);
			}
		}
		prevLocs.removeAll(result);
	}

	// The locs which haven't assigned to any parent
	private static List<Loc> getAvailableLocs(List<Loc> prevLocs, int i) {
		List<Loc> result = new ArrayList<>();
		int numOfChildren = Utils.randomNumber(2 * Generator.BRANCHING_FACTOR / 3, Generator.BRANCHING_FACTOR) + 1;
		for (Loc loc : prevLocs) {
			if (result.size() < numOfChildren && loc.getLevel() == -1) {
				loc.setLevel(i);
				result.add(loc);
			}
		}
		return result;
	}
}

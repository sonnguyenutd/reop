package v8.gen.parser.rovers.group;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import gen.utils.Utils;
import v5.gen.supply_chain.Action;
import v8.gen.parser.rovers.Naviage;
import v8.gen.parser.rovers.Parser;
import v8.gen.parser.rovers.SampleRock;
import v8.gen.parser.rovers.SampleSoil;

public class Relaxer {

	public static void main(String[] args) {
		String file = "../benchmark-1/rovers-large-simple/goal-2/p-r1-w1500-o1-1-g2_ops.txt";
		Set<Action> acts = Parser.parse(file);
		Set<Action> groupedActions = new HashSet<>();
		Set<Action> remainingActs = new HashSet<>();

		Map<Set<String>, Set<Naviage>> mapNavs = new HashMap<>();
		Map<Set<String>, Set<SampleRock>> mapRock = new HashMap<>();
		Map<Set<String>, Set<SampleSoil>> mapSoil = new HashMap<>();

		for (Action a : acts) {
			if (a instanceof Naviage) {
				Set<Naviage> navs = mapNavs.get(a.getPreconds());
				if (navs == null)
					navs = new HashSet<>();
				navs.add((Naviage) a);
				mapNavs.put(a.getPreconds(), navs);
			} else if (a instanceof SampleRock) {
				Set<SampleRock> rs = mapRock.get(a.getPreconds());
				if (rs == null)
					rs = new HashSet<>();
				rs.add((SampleRock) a);
				mapRock.put(a.getPreconds(), rs);
			} else if (a instanceof SampleSoil) {
				Set<SampleSoil> ss = mapSoil.get(a.getPreconds());
				if (ss == null)
					ss = new HashSet<>();
				ss.add((SampleSoil) a);
				mapSoil.put(a.getPreconds(), ss);
			} else {
				remainingActs.add(a);
			}
		}
		for (Set<String> pres : mapNavs.keySet()) {
			Set<Naviage> navs = mapNavs.get(pres);
			Naviage aNav = Utils.selectAnElm(navs);
			NavigateCalibrateTakeImageCommunicateImageData nctc = new NavigateCalibrateTakeImageCommunicateImageData(
					aNav);
			nctc.addActs(navs);
			groupedActions.add(nctc);
		}
		for (Set<String> pres : mapRock.keySet()) {
			Set<SampleRock> rs = mapRock.get(pres);
			SampleRock r = Utils.selectAnElm(rs);
			SampleRockCommunicateRockData sc = new SampleRockCommunicateRockData(r);
			sc.addActs(rs);
			groupedActions.add(sc);
		}
		for (Set<String> pres : mapSoil.keySet()) {
			Set<SampleSoil> ss = mapSoil.get(pres);
			SampleSoil s = Utils.selectAnElm(ss);
			SampleSoilCommunicateSoilData sc = new SampleSoilCommunicateSoilData(s);
			sc.addActs(ss);
			groupedActions.add(sc);
		}

		for (Action a : groupedActions) {
			a.takeMembers(remainingActs);
			remainingActs.removeAll(a.getAllMembers());
		}
		groupedActions.addAll(remainingActs);
		System.out.println(groupedActions.size() + "---" + acts.size());
		RParentDomainConstructor.construct(file, groupedActions);
	}

}

package v8.gen.parser.logistics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import gen.utils.Utils;
import v5.gen.supply_chain.Action;

public class Parser {

	public static void main(String[] args) throws InterruptedException {
		String file = "/Users/sonnguyen/Workspace/2021/reop/logistics-large-simple/goal-4/p-a1-c1-s1000-p10-t1-g4_ops.txt";
		String content = Utils.read(file);
		String[] parts = content.trim().split("-----");
		Set<Action> allActions = new HashSet<>();
		Map<Set<String>, Set<Action>> map = new HashMap<>();
		for (String a : parts) {
			if (a.isEmpty())
				continue;
			String[] lines = a.trim().split("\n");
			if (lines.length > 0) {
				Action act = null;
				if (lines[0].startsWith("(drive-truck"))
					act = new DRIVETRUCK(lines[0]);
				else if (lines[0].startsWith("(fly-airplane"))
					act = new FLYAIRPLANE(lines[0]);
				else if (lines[0].startsWith("(load-truck"))
					act = new LOADTRUCK(lines[0]);
				else if (lines[0].startsWith("(unload-truck"))
					act = new UNLOADTRUCK(lines[0]);
				else if (lines[0].startsWith("(load-airplane"))
					act = new LOADAIRPLANE(lines[0]);
				else if (lines[0].startsWith("(unload-airplane"))
					act = new UNLOADAIRPLANE(lines[0]);
				if (act != null) {
					allActions.add(act);
					Set<Action> acts = map.get(act.getPreconds());
					if (acts == null)
						acts = new HashSet<>();
					acts.add(act);
					map.put(act.getPreconds(), acts);
				}
			}
		}
//		for (Action act : allActions) {
//			System.out.println(act.toPDDL());
//			break;
//		}
		System.out.println(allActions.size());
		System.out.println(allActions.size() / (double) map.size());
		for (Set<String> pre : map.keySet()) {
			System.out.println(pre + "-->" + map.get(pre).size());
		}
//		ParentDomainConstructor.construct(file, map);
	}
}

package v8.gen.parser.logistics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import gen.utils.Utils;
import v5.gen.supply_chain.Action;

public class Parser {

	public static void main(String[] args) throws InterruptedException {
		String file = "../benchmark-1/logistics-large-simple/goal-1/p-a1-c1-s1000-p10-t1-g1_ops.txt";
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
		Map<Action, Set<Action>> dic = new HashMap<>();
		int counter = 1;
		for (Set<String> pre : map.keySet()) {
			Set<Action> child = map.get(pre);
			Action a = constructParentAct(pre, child, counter++);
			dic.put(a, child);
		}
		System.out.println(allActions.size());
		System.out.println(allActions.size() / (double) map.size());
	}

	public static Map<Action, Set<Action>> parse(String file) throws InterruptedException {
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
		Map<Action, Set<Action>> dic = new HashMap<>();
		int counter = 1;
		for (Set<String> pre : map.keySet()) {
			Set<Action> child = map.get(pre);
			Action a = constructParentAct(pre, child, counter++);
			dic.put(a, child);
		}
		System.out.println(allActions.size());
		System.out.println(allActions.size() / (double) map.size());
		return dic;
	}

	private static Action constructParentAct(Set<String> preconds, Set<Action> child, int counter) {
		String name = "act-" + counter;
		if (child.size() == 1) {
			Action a = Utils.selectAnElm(child);
			name = a.getName();
		}
		Action result = new Action(name);
		result.setPreconds(preconds);
		Set<String> pEffects = new HashSet<>();
		Set<String> nEffects = new HashSet<>();
		for (Action c : child) {
			pEffects.addAll(c.getEffects());
			nEffects.addAll(c.getNeffects());
		}
		result.setEffects(pEffects);
		result.setNeffects(nEffects);
		return result;
	}
}

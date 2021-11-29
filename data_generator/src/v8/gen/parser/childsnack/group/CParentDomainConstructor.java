package v8.gen.parser.childsnack.group;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import gen.utils.Utils;
import v5.gen.supply_chain.Action;
import v8.gen.parser.childsnack.MakeSandwich;
import v8.gen.parser.childsnack.MoveTray;
import v8.gen.parser.childsnack.PutOnTray;
import v8.gen.parser.childsnack.ServeSandwich;

public class CParentDomainConstructor {
	public static void main(String[] args) throws InterruptedException {
		String file = "../benchmark-1/childsnack-contents/parsize1-cham7/contentam3-p25_ops.txt";
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
				if (lines[0].startsWith("(move_tray"))
					act = new MoveTray(lines[0]);
				else if (lines[0].startsWith("(make_sandwich"))
					act = new MakeSandwich(lines[0]);
				else if (lines[0].startsWith("(put_on_tray"))
					act = new PutOnTray(lines[0]);
				else if (lines[0].startsWith("(serve_sandwich"))
					act = new ServeSandwich(lines[0]);

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
		System.out.println(allActions.size());
		System.out.println(allActions.size() / (double) map.size());

		construct(file, map);
	}

	public static void construct(String opsFile, Map<Set<String>, Set<Action>> map) {
		Set<String> predicateSet = handleProbleFile(opsFile);
		
		StringBuffer domain = new StringBuffer("(define (domain test)\n");
		StringBuffer predicates = new StringBuffer("(:predicates \n");
		StringBuffer actions = new StringBuffer();
		StringBuffer children_parentPDDL = new StringBuffer();

		for (Set<String> pre : map.keySet()) {
			Set<Action> children = map.get(pre);
			Set<String> pEffects = new HashSet<>();
			Set<String> nEffects = new HashSet<>();
			String name = "";
			for (Action a : children) {
				pEffects.addAll(a.getEffects());
				nEffects = a.getNeffects();
				name = a.getName();
				children_parentPDDL.append(a.toPDDL() + "\n");
			}
			int last = name.lastIndexOf("-");
			name = name.substring(0, last);
			Action parent = new Action(name, pre, pEffects, nEffects);
			predicateSet.addAll(parent.getAllPredicates());
			actions.append(parent.toPDDL() + "\n");

			children_parentPDDL.append(";;;" + name + "\n");
		}

		for (String pre : predicateSet)
			predicates.append("\t(" + pre.trim().replace(" ", "-") + ")" + "\n");

		predicates.append("\n)\n");

		domain.append(predicates);
		domain.append(actions);

		domain.append("\n)");
		Utils.write(opsFile.replace("_ops.txt", "_p_domain.pddl"), domain.toString());
		Utils.write(opsFile.replace("_ops.txt", "_map.txt"), children_parentPDDL.toString());

		// Handle problem file
		handleProbleFile(opsFile);

	}

	protected static Set<String> handleProbleFile(String opsFile) {
		Set<String> predicates = new HashSet<>();
		String newFile = opsFile.replace("_ops.txt", "_p_prob.pddl");
		String probFile = opsFile.replace("_ops.txt", ".pddl");
		StringBuffer content = new StringBuffer("(define (problem test)\n" + "(:domain test)");
		boolean start = false;
		try (BufferedReader br = new BufferedReader(new FileReader(probFile))) {
			String line;
			while (((line = br.readLine()) != null)) {
				line = line.trim();
				if (line.startsWith("(:init"))
					start = true;
				if (start && !line.startsWith("(likes") && !line.startsWith("(descr") && !line.startsWith("(waiting")) {
					if (!line.startsWith("(:goal")) {
						content.append(line.replace(" ", "-") + "\n");
						if (!line.contains(":"))
							predicates.add(line.replace(" ", "-").replace("(", "").replace(")", "").trim());
					} else
						content.append(line + "\n");
				}
			}
		} catch (IOException e) {
		}
		Utils.write(newFile, content.toString());
		return predicates;
	}
}

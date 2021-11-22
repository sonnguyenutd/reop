package v8.gen.parser.visitall.group;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import gen.utils.Utils;
import v5.gen.supply_chain.Action;

public class ParentDomainConstructor {
	public static void construct(String opsFile, Map<Set<String>, Set<Action>> map) {
		StringBuffer output = new StringBuffer("(define (domain test)\n");
		StringBuffer predicates = new StringBuffer("(:predicates \n");
		Set<String> predicateSet = new HashSet<>();
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

		output.append(predicates);
		output.append(actions);

		output.append("\n)");
		Utils.write(opsFile.replace("_ops.txt", "_p.pddl"), output.toString());
		Utils.write(opsFile.replace("_ops.txt", "_map.txt"), children_parentPDDL.toString());

		// Handle problem file
		handleProbleFile(opsFile);

	}

	private static void handleProbleFile(String opsFile) {
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
				if (start && !line.startsWith("(neighbor")) {
					content.append(line.replace(" ", "-") + "\n");
				}
			}
		} catch (IOException e) {
		}
		Utils.write(newFile, content.toString());
	}
}

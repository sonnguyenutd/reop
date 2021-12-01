package v8.gen.parser.visitall.group;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import gen.utils.Utils;
import v5.gen.supply_chain.Action;
import v8.gen.parser.visitall.Parser;

public class VParentDomainConstructor {
	public static void main(String[] args) {
		String dir = "../benchmark-1/visitall-multidimensional";
		Collection<File> allFiles = Utils.listFileTree(new File(dir));
		for (File opsFile : allFiles) {
			if (opsFile.getName().endsWith("ops.txt")) {
				String opsFilePath = opsFile.getAbsolutePath();
				Map<Set<String>, Set<Action>> map = Parser.parse(opsFilePath);
				construct(opsFilePath, map);
			}
		}
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
	}

	protected static Set<String> handleProbleFile(String opsFile) {
		Set<String> predicateSet = new HashSet<>();
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
					if (line.startsWith("(visited"))
						predicateSet.add(line.replace("(", "").replace(")", ""));
				}
			}
		} catch (IOException e) {
		}
		Utils.write(newFile, content.toString());
		return predicateSet;
	}
}

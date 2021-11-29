package v8.gen.parser.logistics.group;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import gen.utils.Utils;
import v5.gen.supply_chain.Action;
import v8.gen.parser.logistics.Parser;

public class LParentDomainConstructor {
	public static void main(String[] args) throws InterruptedException {
		String file = "../benchmark-1/logistics-large-simple/goal-1/p-a1-c1-s1000-p10-t1-g1_ops.txt";
		Map<Action, Set<Action>> dic = Parser.parse(file);
		construct(file, dic);
	}

	public static void construct(String opsFile, Map<Action, Set<Action>> map) {
		// Handle problem file
		Set<String> predicateSet = handleProbleFile(opsFile);

		StringBuffer domain = new StringBuffer("(define (domain test)\n");
		StringBuffer predicates = new StringBuffer("(:predicates \n");
		StringBuffer actions = new StringBuffer();
		StringBuffer children_parentPDDL = new StringBuffer();

		for (Action parent : map.keySet()) {
			Set<Action> children = map.get(parent);
			String name = parent.getName();
			for (Action a : children)
				children_parentPDDL.append(a.toPDDL() + "\n");

			predicateSet.addAll(parent.getAllPredicates());
			actions.append(parent.toPDDL() + "\n");

			children_parentPDDL.append(";;;" + name + "\n");
		}

		for (String pre : predicateSet) {
			pre = pre.trim().replace(" ", "-");
			if (!pre.isEmpty())
				predicates.append("\t(" + pre + ")" + "\n");
		}

		predicates.append("\n)\n");

		domain.append(predicates);
		domain.append(actions);

		domain.append("\n)");
		Utils.write(opsFile.replace("_ops.txt", "_p_domain.pddl"), domain.toString());
		Utils.write(opsFile.replace("_ops.txt", "_map.txt"), children_parentPDDL.toString());

	}

	protected static Set<String> handleProbleFile(String opsFile) {
		Set<String> addPredicates = new HashSet<>();

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
				if (start && !line.startsWith("(OBJ") && !line.startsWith("(AIRPORT") && !line.startsWith("(in-city")
						&& !line.startsWith("(LOCATION") && !line.startsWith("(TRUCK") && !line.startsWith("(AIRPLANE")
						&& !line.startsWith("(CITY")) {
					if (!line.startsWith("(:goal") && !line.startsWith("(and")) {
						content.append(line.replace(" ", "-") + "\n");
						if (!line.contains(":"))
							addPredicates.add(line.replace(" ", "-").replace("(", "").replace(")", "").trim());
					} else
						content.append(line + "\n");
				}
			}
		} catch (IOException e) {
		}
		Utils.write(newFile, content.toString());
		return addPredicates;
	}
}

package v8.gen.parser.rovers.group;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import gen.utils.Utils;
import v5.gen.supply_chain.Action;

public class RParentDomainConstructor {
	public static void main(String[] args) {
		String file = "../benchmark-1/rovers-large-simple/goal-6/p-r1-w1500-o1-1-g6_ops.txt";
		handleProbleFile(file);
	}

	public static void construct(String opsFile, Set<Action> acts) {
//		Handle prob first to take all predicates
		Set<String> predicateSet = handleProbleFile(opsFile);

		StringBuffer domain = new StringBuffer("(define (domain test)\n");
		StringBuffer predicates = new StringBuffer("(:predicates \n");
		StringBuffer actions = new StringBuffer();
		StringBuffer children_parentPDDL = new StringBuffer();

		for (Action parent : acts) {
			Set<Action> children = parent.getAllMembers();
			String name = parent.getName();

			if (children != null) {
				for (Action a : children)
					children_parentPDDL.append(a.toPDDL() + "\n");
			} else
				children_parentPDDL.append(parent.toPDDL() + "\n");
			predicateSet.addAll(parent.getAllPredicates());
			actions.append(parent.toPDDL() + "\n");
			children_parentPDDL.append(";;;" + name + "\n");
		}

		for (String pre : predicateSet)
			if (!pre.trim().isEmpty())
				predicates.append("\t(" + pre.trim().replace(" ", "-") + ")" + "\n");

		predicates.append("\n)\n");

		domain.append(predicates);
		domain.append(actions);

		domain.append("\n)");
		Utils.write(opsFile.replace("_ops.txt", "_p_domain.pddl"), domain.toString());
		Utils.write(opsFile.replace("_ops.txt", "_map.txt"), children_parentPDDL.toString());

		// Handle problem file
	}

	protected static Set<String> handleProbleFile(String opsFile) {
		Set<String> predicates = new HashSet<>();
		String newFile = opsFile.replace("_ops.txt", "_p_prob.pddl");
		String probFile = opsFile.replace("_ops.txt", ".pddl");
		StringBuffer content = new StringBuffer("(define (problem test)\n" + "(:domain test)\n");
		boolean start = false;
		try (BufferedReader br = new BufferedReader(new FileReader(probFile))) {
			String line;
			while (((line = br.readLine()) != null)) {
				line = line.trim();
				if (line.startsWith("(:init"))
					start = true;
				if (start && !line.startsWith("(visible") && !line.startsWith("(store_of")
						&& !line.startsWith("(available") && !line.startsWith("(equipped_for_soil_analysis")
						&& !line.startsWith("(equipped_for_rock_analysis") && !line.startsWith("(equipped_for_imaging")
						&& !line.startsWith("(can_traverse") && !line.startsWith("(on_board")
						&& !line.startsWith("(calibration_target") && !line.startsWith("(supports")
						&& !line.startsWith("(channel_free") && !line.startsWith("(at_lander")) {
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
//		System.out.println(content.toString());
		Utils.write(newFile, content.toString());
		return predicates;
	}
}

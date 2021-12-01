package v8.gen.parser.visitall.group;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import gen.utils.Utils;
import v5.gen.supply_chain.Action;

public class ResultCollector {

	public static void main(String[] args) {
		String dir = "../benchmark-1/visitall-multidimensional";
		Collection<File> allFiles = Utils.listFileTree(new File(dir));
		for (File opsFile : allFiles) {
			if (opsFile.getName().endsWith("ops.txt")) {
				String opsFilePath = opsFile.getAbsolutePath();
				System.out.println(opsFilePath);
				String mapFile = opsFilePath.replace("_ops.txt", "_map.txt");
				String outFile = opsFilePath.replace("_ops.txt", ".out");
				String childFile = opsFilePath.replace("_ops.txt", "_p_prob_c_domain.pddl");

				Map<String, Set<Action>> map = ReducedProblemConstructor.parseMap(mapFile);
				Set<String> steps = ReducedProblemConstructor.parseSteps(outFile);

				int orginalSize = countOriginalSize(map);
				int parentSize = map.size();
				int parentSolutionSize = steps.size();

				int reducedSize = countReducedProblemSize(childFile);
				int reopReducedSize = 0;
				int cppdlReducedSize = 0;

				System.out.println(orginalSize);
				System.out.println(parentSize);
				System.out.println(parentSolutionSize);
				System.out.println(reducedSize);
				System.out.println(reopReducedSize);
				System.out.println(cppdlReducedSize);
				break;
			}
		}
	}

	private static int countReducedProblemSize(String childFile) {
		String content = Utils.read(childFile);
		String[] parts = content.split("\\(\\:action ");
		return parts.length - 1;
	}

	private static int countOriginalSize(Map<String, Set<Action>> map) {
		int result = 0;
		for (String parentName : map.keySet()) {
			result += map.get(parentName).size();
		}
		return result;
	}

}

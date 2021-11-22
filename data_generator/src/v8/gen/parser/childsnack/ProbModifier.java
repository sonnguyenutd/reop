package v8.gen.parser.childsnack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gen.utils.Utils;

public class ProbModifier {

	public static void main(String[] args) throws InterruptedException {
		for (int i = 0; i <= 9; i++) {
			System.out.println("--->" + i);
			String file = "/Users/sonnguyen/Downloads/benchmarks/visitall-multidimensional/3-dim-visitall-FAR-g3/p"
					+ i + ".pddl";
			String content = Utils.read(file);
			String[] lines = content.split("\n");
			StringBuffer newProb = new StringBuffer();
			Set<String> neighbors = new HashSet<>();
			for (String l : lines) {
				newProb.append(l + "\n");
				if (l.startsWith("(:init"))
					newProb.append(";;;;\n");
				
			}

			// Inserting new neighbors...
			int times = 0;
			if (i < 4)
				times = 2;
			else if (i < 6)
				times = 3;
			else if (i < 10)
				times = 5;
//			String generatedNeighbors = generateNeighbors(pos, neighbors, times);
			int insertingIdx = newProb.indexOf(";;;;");
//			newProb.insert(insertingIdx, generatedNeighbors + "\n");
//			System.out.println(newProb.toString());
			Utils.write(file.replace(".pddl", "_m.pddl"), newProb.toString());
		}
	}



}

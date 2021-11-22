package v8.gen.parser.visitall;

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
			List<Pos> pos = new ArrayList<>();
			StringBuffer newProb = new StringBuffer();
			Set<String> neighbors = new HashSet<>();
			for (String l : lines) {
				newProb.append(l + "\n");
				if (l.startsWith("(:init"))
					newProb.append(";;;;\n");
				if (l.contains(" - pos")) {
					String ps = l.replace(" - pos", "").trim();
					String[] ids = ps.split(" ");
					for (String id : ids) {
						Pos p = new Pos(id);
						pos.add(p);
					}
				}
				if (l.trim().startsWith("(neighbor")) {
					neighbors.add(l.trim());
					l = l.replace(")", "");
					String[] parts = l.trim().split(" ");
					Pos p1 = findPos(parts[1], pos);
					Pos p2 = findPos(parts[2], pos);
					if (p1 != null & p2 != null)
						p1.addNeighbor(p2);
				}
			}

			// Inserting new neighbors...
			int times = 0;
			if (i < 4)
				times = 2;
			else if (i < 6)
				times = 3;
			else if (i < 10)
				times = 5;
			String generatedNeighbors = generateNeighbors(pos, neighbors, times);
			int insertingIdx = newProb.indexOf(";;;;");
			newProb.insert(insertingIdx, generatedNeighbors + "\n");
//			System.out.println(newProb.toString());
			Utils.write(file.replace(".pddl", "_m.pddl"), newProb.toString());
		}
	}

	private static String generateNeighbors(List<Pos> pos, Set<String> neighbors, int times) {
		Set<String> ns = new HashSet<>(neighbors);
		while (ns.size() < neighbors.size() * times) {
			int f = Utils.randomNumber(0, pos.size() - 1);
			int t = Utils.randomNumber(0, pos.size() - 1);
			if (f != t) {
				String connector = "(neighbor " + pos.get(f).getId() + " " + pos.get(t).getId() + ")";
				ns.add(connector);
			}
		}
		StringBuffer result = new StringBuffer();
		ns.removeAll(neighbors);
		for (String connector : ns) {
			result.append(connector + "\n");
		}
		return result.toString();
	}

	static <E> Set<List<E>> combK(Collection<E> els, int k) {
		Set<List<E>> currCombs = new HashSet<List<E>>();
		for (E e : els) {
			List<E> com = new ArrayList<>();
			com.add(e);
			currCombs.add(com);
		}
		int i = 1;
		while (i < k) {
			Set<List<E>> temp = new HashSet<List<E>>();
			for (E e : els) {
				for (List<E> currCom : currCombs) {
					List<E> com = new ArrayList<>(currCom);
					com.add(e);
					temp.add(com);
				}
			}
			i++;
			currCombs = temp;
		}
		return currCombs;
	}

	private static Pos findPos(String id, List<Pos> pos) {
		for (Pos p : pos) {
			if (p.getId().equals(id))
				return p;
		}
		return null;
	}
}

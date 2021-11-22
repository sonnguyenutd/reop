package v8.gen.parser.visitall;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gen.utils.Utils;
import v5.gen.supply_chain.Action;
import v8.gen.parser.GroundingExecutor;
import v8.gen.parser.GroundingThread;
import v8.gen.parser.visitall.group.ParentDomainConstructor;

public class ParserBK {

	public static void main(String[] args) throws InterruptedException {
		String file = "/Users/sonnguyen/Downloads/benchmarks/visitall-multidimensional/3-dim-visitall-CLOSE-g1/p1.pddl";
		String content = Utils.read(file);
		String[] lines = content.split("\n");
		List<Pos> pos = new ArrayList<>();
		for (String l : lines) {
			if (l.contains(" - pos")) {
				String ps = l.replace(" - pos", "").trim();
				String[] ids = ps.split(" ");
				for (String id : ids) {
					Pos p = new Pos(id);
					pos.add(p);
				}
			}
			if (l.trim().startsWith("(neighbor")) {
				l = l.replace(")", "");
				String[] parts = l.trim().split(" ");
				Pos p1 = findPos(parts[1], pos);
				Pos p2 = findPos(parts[2], pos);
				if (p1 != null & p2 != null)
					p1.addNeighbor(p2);
			}
		}
		System.out.println("SIZE: " + pos.size());
		for (Pos p : pos) {
			System.out.println(p + "-->" + p.neighbors);
		}
		Set<List<Pos>> pairs = combK(pos, 4);

		System.out.println("SIZE: " + pairs.size());
		VisitalDistributor dis = new VisitalDistributor(pairs);
		List<GroundingThread> ths = dis.distribute(4);
		GroundingExecutor.execute(ths);
		List<Action> moves = new ArrayList<Action>();
		for (GroundingThread t : ths) {
			moves.addAll(t.getGroundedActions());
		}
		System.out.println("---------");
		System.out.println(moves.size());
		System.out.println(moves.get(0));
		System.out.println(moves.get(0).toPDDL());
		Map<Set<String>, Set<Action>> maps = new HashMap<>();
		for (Action a : moves) {
			Set<Action> acts = maps.get(a.getPreconds());
			if (acts == null)
				acts = new HashSet<>();
			acts.add(a);
			maps.put(a.getPreconds(), acts);
		}
		System.out.println(maps.size());
		ParentDomainConstructor.construct(file, maps);
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

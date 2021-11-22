package v8.gen.parser.visitall;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import gen.utils.Utils;
import v5.gen.supply_chain.Action;
import v8.gen.parser.visitall.group.ParentDomainConstructor;

public class Parser {

	public static void main(String[] args) throws InterruptedException {
		for (int i = 0; i < 1; i++) {
			String file = "/Users/sonnguyen/Downloads/benchmarks/visitall-multidimensional/3-dim-visitall-CLOSE-g1/p"
					+ i + "_ops.txt";
			String content = Utils.read(file);
			String[] parts = content.trim().split("-----");
			Set<Action> allActions = new HashSet<>();
			Map<Set<String>, Set<Action>> map = new HashMap<>();
			for (String a : parts) {
				if (a.isEmpty())
					continue;
				String[] lines = a.trim().split("\n");
				if (lines.length > 0) {
					Move m = new Move(lines[0]);
					Set<Action> acts = map.get(m.getPreconds());
					if (acts == null)
						acts = new HashSet<>();
					acts.add(m);
					map.put(m.getPreconds(), acts);
					allActions.add(m);
				}
			}
			System.out.println(allActions.size());
			System.out.println(allActions.size()/(double)map.size());
			ParentDomainConstructor.construct(file, map);
		}
	}
}

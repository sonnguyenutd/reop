package v8.gen.parser.childsnack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import gen.utils.Utils;
import v5.gen.supply_chain.Action;

public class Parser {

	public static void main(String[] args) throws InterruptedException {
		String file = "/Users/sonnguyen/Downloads/benchmarks/childsnack-contents/parsize1-cham7/contentam3-p25_ops.txt";
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
//		for (Action act : allActions) {
//			System.out.println(act.toPDDL());
//			break;
//		}
		System.out.println(allActions.size());
		System.out.println(allActions.size() / (double) map.size());
		for (Set<String> pre : map.keySet()) {
			System.out.println(pre + "-->" + map.get(pre).size());
		}
//		ParentDomainConstructor.construct(file, map);
	}
}

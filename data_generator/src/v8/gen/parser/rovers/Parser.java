package v8.gen.parser.rovers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import gen.utils.Utils;
import v5.gen.supply_chain.Action;

public class Parser {

	public static void main(String[] args) throws InterruptedException {
		String file = "/Users/sonnguyen/Workspace/2021/reop/rovers-large-simple/goal-6/p-r1-w1000-o1-1-g6_ops.txt";
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
				if (lines[0].startsWith("(navigate"))
					act = new Naviage(lines[0]);
				else if (lines[0].startsWith("(sample_rock"))
					act = new SampleRock(lines[0]);
				else if (lines[0].startsWith("(drop"))
					act = new Drop(lines[0]);
				else if (lines[0].startsWith("(calibrate"))
					act = new Calibrate(lines[0]);
				else if (lines[0].startsWith("(take_image"))
					act = new TakeImage(lines[0]);
				else if (lines[0].startsWith("(communicate_image_data"))
					act = new CommunicateImageData(lines[0]);
				else if (lines[0].startsWith("(sample_soil"))
					act = new SampleSoil(lines[0]);
				else if (lines[0].startsWith("(communicate_soil_data"))
					act = new CommunicateSoilData(lines[0]);
				else if (lines[0].startsWith("(communicate_rock_data"))
					act = new CommunicateRockData(lines[0]);
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

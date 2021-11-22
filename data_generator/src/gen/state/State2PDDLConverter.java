package gen.state;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gen.general.Concretizer;
import gen.general.Obj;
import gen.general.ObjTemplate;
import gen.utils.Utils;

public class State2PDDLConverter {
	public static void main(String[] args) {
		String tempFile = "/Users/sonnguyen/Desktop/objs.txt";
		List<ObjTemplate> temps = ObjTemplate.parse(tempFile);

		Obj o = Concretizer.randomlyConcritizeObj(temps.get(0));
		Set<String> pddlDescriptors = convert(o);
		System.out.println(pddlDescriptors);
	}

	public static Set<String> convert(Obj o) {
		State s = o.getState();
		Map<String, String> des = s.getDescriptor();
		Set<String> result = new HashSet<>();
		for (String att : des.keySet()) {
			String pddl = convert(o.getId(), att, des.get(att));
			result.add(pddl);
		}
		return result;
	}

	private static String convert(String id, String att, String value) {
		String result = "";
		if (Utils.isBool(value)) {
			if (value.equals("T"))
				result = "( " + att + " " + id + " )";
			else
				result = "(not ( " + att + " " + id + " ))";
		} else if (Utils.isNumeric(value)) {
			result = "(= ( " + att + " " + id + " ) " + value + ")";
		} else {// is an object
			result = "( " + att + " " + id + " " + value + " )";
		}
		return result;
	}

}

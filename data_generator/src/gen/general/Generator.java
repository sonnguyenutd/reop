package gen.general;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Generator {
	static Random rand = new Random();

	public static void main(String[] args) {
		String tempFile = "/Users/sonnguyen/Desktop/objs.txt";
		List<ObjTemplate> temps = ObjTemplate.parse(tempFile);
		System.out.println(temps.get(0));
//		String sTempFile = "/Users/sonnguyen/Desktop/services.txt";
//		List<ServiceTemplate> sTemps = ServiceTemplate.parse(sTempFile );
//		System.out.println(sTemps.get(0));
		Map<String, String> values = generate(temps.get(0).getTempState());
		System.out.println(values);
	}
	public static Set<Obj> generateObjects(ObjTemplate temp, int numObj) {
		Set<Obj> result = new HashSet<>();
		while (result.size() < numObj) {
			Obj o = Concretizer.randomlyConcritizeObj(temp);
			result.add(o);
		}
		return result;
	}

	public static String generate(String rule) {
		String value = null;
		if (isSetValue(rule)) {
			value = generateFromSet(rule);
		} else if (isRangeValue(rule)) {
			value = generateFromRange(rule);
		} else if (isBoolValue(rule)) {
			value = generateFromSet(rule);
		} else { // ID
			value = generateID(rule);
		}
		return value;
	}

	public static String generateID(String rule) {
		int r = rand.nextInt(9);
		return rule + r + "" + (System.currentTimeMillis() % 100000);
	}

	public static boolean isBoolValue(String rule) {
		return false;
	}

	public static String generateFromRange(String rule) {
		String value = null;
		List<String> vs = extractValues(rule);
		if (vs.size() == 2) {
			int min = Integer.parseInt(vs.get(0));
			int max = Integer.parseInt(vs.get(1));
			value = rand.nextInt(max - min) + min + "";
		}
		return value;
	}

	public static boolean isRangeValue(String rule) {
		return rule.trim().startsWith("[");
	}

	public static String generateFromSet(String rule) {
		String value = null;
		List<String> vs = extractValues(rule);
		if (!vs.isEmpty()) {
			int i = rand.nextInt(vs.size());
			value = vs.get(i);
		}
		return value;
	}

	public static List<String> extractValues(String rule) {
		String[] parts = rule.split("\\{|\\}| |\\[|\\]|,");
		List<String> vs = new ArrayList<>();
		for (String v : parts) {
			if (!v.trim().isEmpty())
				vs.add(v.trim());
		}
		return vs;
	}

	public static boolean isSetValue(String rule) {
		return rule.trim().startsWith("{");
	}

	public static Map<String, String> generate(Map<String, String> tempState) {
		Map<String, String> result = new HashMap<>();
		for (String att : tempState.keySet()) {
			String value = generate(tempState.get(att));
			result.put(att, value);
		}
		return result;
	}


}

package v0.icws;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Workflow {
	Map<String, Integer> minQoS;
	Map<String, Integer> maxQoS;
	

	public static Map<String, Double> getMin_backup(List<Cate> flow) {
		Map<String, Double> overallMinQoS = new HashMap<>();
		for (Cate c : flow) {
			Map<String, Double> minQoSCate = new HashMap<>();
			for (String att : minQoSCate.keySet()) {
				Double min = minQoSCate.get(att);
				for (Action alt : c.getActs()) {
					if (min == null)
						min = (double) Cate.MAX_ATT_VAL_EXPENSIVE;
					if (alt.QoS().get(att) < min)
						min = alt.QoS().get(att);
					minQoSCate.put(att, min);

				}
			}
			for (String att : minQoSCate.keySet()) {
				Double min = minQoSCate.get(att);
				Double overallMin = overallMinQoS.get(att);
				if (overallMin == null)
					overallMin = 0.0;
				overallMin += min;
				overallMinQoS.put(att, overallMin);
			}

		}
		return overallMinQoS;
	}

	static Map<String, Double> getMinQoSCate(Cate c) {
		Set<Action> as = c.getActs();
		Map<String, Double> min = new HashMap<>();
		for (Action a : as) {
			Map<String, Double> QoS = a.QoS();
			for (String att : QoS.keySet()) {
				Double val = QoS.get(att);
				Double m = min.get(att);
				if (m == null)
					m = val;
				else {
					if (m > val)
						m = val;
				}
				min.put(att, m);
			}
		}
		return min;
	}

	private Map<String, Double> getMaxQoSCate(Cate c) {
		Set<Action> as = c.getActs();
		Map<String, Double> max = new HashMap<>();
		for (Action a : as) {
			Map<String, Double> QoS = a.QoS();
			for (String att : QoS.keySet()) {
				Double val = QoS.get(att);
				Double m = max.get(att);
				if (m == null)
					m = val;
				else {
					if (m < val)
						m = val;
				}
				max.put(att, m);
			}
		}
		return max;
	}

	public static Map<String, Double> getMin(List<Cate> flow) {
		Map<String, Double> overallMinQoS = new HashMap<>();
		for (Cate c : flow) {
			Map<String, Double> minQoSCate = getMinQoSCate(c);
			for (String att : minQoSCate.keySet()) {
				Double min = minQoSCate.get(att);
				for (Action alt : c.getActs()) {
					if (min == null)
						min = (double) Cate.MAX_ATT_VAL_EXPENSIVE;
					if (alt.QoS().get(att) < min)
						min = alt.QoS().get(att);
					minQoSCate.put(att, min);
	
				}
			}
			for (String att : minQoSCate.keySet()) {
				Double min = minQoSCate.get(att);
				Double overallMin = overallMinQoS.get(att);
				if (overallMin == null)
					overallMin = 0.0;
				overallMin += min;
				overallMinQoS.put(att, overallMin);
			}
	
		}
		return overallMinQoS;
	}
}

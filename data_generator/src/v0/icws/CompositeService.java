package v0.icws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompositeService {
	List<Action> services;
	Map<String, Double> QoS;

	public CompositeService() {
		services = new ArrayList<Action>();
		QoS = new HashMap<String, Double>();
	}

	public CompositeService(List<Action> as) {
		services = new ArrayList<Action>();
		QoS = new HashMap<String, Double>();
		addServices(as);
	}

	public void addService(Action a) {
		if (!a.getName().startsWith("NOOP")) {
			this.services.add(a);
			for (String att : a.QoS().keySet()) {
				Double val = a.QoS().get(att);
				Double compositionVal = this.QoS.get(att);
				if (compositionVal == null)
					compositionVal = 0.0;
				compositionVal += val;
				this.QoS.put(att, compositionVal);
			}
		}
	}

	public void addServices(List<Action> as) {
		for (Action a : as) {
			addService(a);
		}
	}

	public static void main(String[] args) {

	}

	public Map<String, Double> QoS() {
		return QoS;
	}

	@Override
	public String toString() {
		String result = "";
		for (Action a : services) {
			result += a.toFormattedText("") + "\n";
		}
		return result;
	}
}

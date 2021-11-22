package gen.state;

import java.util.HashMap;
import java.util.Map;

public class State {
	Map<String, String> descriptor;

	public State() {
		descriptor = new HashMap<String, String>();
	}

	public void put(String key, String value) {
		descriptor.put(key, value);
	}

	public String get(String key) {
		return descriptor.get(key);
	}

	public Map<String, String> getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(Map<String, String> descriptor) {
		this.descriptor = descriptor;
	}
}

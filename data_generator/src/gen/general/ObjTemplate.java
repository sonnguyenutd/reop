package gen.general;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gen.utils.Utils;

public class ObjTemplate {
	final static String TEMP_SEPARATOR = "___";
	final static String RULE_SEPARATOR = ":";
	Map<String, String> tempState;
	String type;

	@Override
	public String toString() {
		String result = type;
		result += ("=" + tempState);
		return result;
	}

	public ObjTemplate() {
		tempState = new HashMap<String, String>();
	}

	public static List<ObjTemplate> parse(String path) {
		String content = Utils.read(path);
		List<ObjTemplate> temps = new ArrayList<ObjTemplate>();
		String[] parts = content.split(TEMP_SEPARATOR);
		for (String part : parts) {
			String[] lines = part.trim().split("\n");
			if (lines.length > 0) {
				String type = lines[0].trim();
				ObjTemplate temp = new ObjTemplate();
				temp.type = type;
				for (int i = 1; i < lines.length; i++) {
					String[] rule = lines[i].trim().split(RULE_SEPARATOR);
					if (rule.length == 2)
						temp.tempState.put(rule[0].trim(), rule[1].trim());
				}
				temps.add(temp);
			}
		}
		return temps;
	}

	public String getType() {
		return type;
	}

	public Map<String, String> getTempState() {
		return tempState;
	}
}

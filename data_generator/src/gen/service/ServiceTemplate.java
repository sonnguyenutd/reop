package gen.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gen.utils.Utils;

public class ServiceTemplate {
	final static String TEMP_SEPARATOR = "___";
	final static String TEMP_EMPTY = "---";
	final static String PREDICATE_SEPARATOR = "\\|\\|\\|";
	private String name;
	String pType;
	private List<String> paras;
	private List<String> preconds;
	private List<String> pEffects;
	private List<String> nEffects;

	public ServiceTemplate(String pType, String name, List<String> paras) {
		this.pType = pType;
		this.setName(name);
		this.setParas(paras);
	}
	public String getpType() {
		return pType;
	}

	@Override
	public String toString() {
		String result = pType + "-->" + getName() + ("(" + getParas() + ")");
		result += "\n\t" + this.getPreconds();
		result += "\n\t" + this.getpEffects();
		result += "\n\t" + this.getnEffects();
		return result;
	}

	public static List<ServiceTemplate> parse(String path) {
		String content = Utils.read(path);
		List<ServiceTemplate> temps = new ArrayList<ServiceTemplate>();
		String[] parts = content.split(TEMP_SEPARATOR);
		for (String part : parts) {
			String[] lines = part.trim().split("\n");
			if (lines.length == 6) {
				String pType = lines[0].trim();
				String name = lines[1].trim();
				String[] parasType = lines[2].trim().split(",");
				ServiceTemplate temp = new ServiceTemplate(pType, name, Arrays.asList(parasType));
				if (!lines[3].equals(TEMP_EMPTY)) {
					String[] ps = lines[3].split(PREDICATE_SEPARATOR);
					temp.setPreconds(Arrays.asList(ps));
				}
				if (!lines[4].equals(TEMP_EMPTY)) {
					String[] pes = lines[4].split(PREDICATE_SEPARATOR);
					temp.setpEffects(Arrays.asList(pes));
				}
				if (!lines[5].equals(TEMP_EMPTY)) {
					String[] nes = lines[5].split(PREDICATE_SEPARATOR);
					temp.setnEffects(Arrays.asList(nes));
				}
				temps.add(temp);
			}
		}
		return temps;
	}

	public List<String> getParas() {
		return paras;
	}

	public void setParas(List<String> paras) {
		this.paras = paras;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getPreconds() {
		return preconds;
	}

	public void setPreconds(List<String> preconds) {
		this.preconds = preconds;
	}

	public List<String> getpEffects() {
		return pEffects;
	}

	public void setpEffects(List<String> pEffects) {
		this.pEffects = pEffects;
	}

	public List<String> getnEffects() {
		return nEffects;
	}

	public void setnEffects(List<String> nEffects) {
		this.nEffects = nEffects;
	}

}

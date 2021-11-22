package analysis.domain.service;

import java.util.ArrayList;
import java.util.List;

import analysis.action.Operator;
import analysis.domain.service.grounding.GroundedFunct;
import analysis.domain.service.obj.Obj;

public class Funct {
	public String name;
	// ASSUME that the first para is the provider if applicable
	public List<Obj> paras;

	public Funct(String name) {
		this.name = name;
		this.paras = new ArrayList<>();
	}

	/**
	 * Starts and ends with ( and )
	 */
	public Funct(String f, List<Obj> serviceParas) {
		paras = new ArrayList<Obj>();
		String t = f.replace("(", "").replace(")", "").trim();
		String[] parts = t.split(" ");
		if (parts.length > 0) {
			name = parts[0];
			paras = new ArrayList<>();
			for (int i = 1; i < parts.length; i++) {
				String argName = parts[i].trim();
				if (argName.startsWith("?")) {
					Obj arg = findArg(argName, serviceParas);
					paras.add(arg);
				}
			}
		}
	}

	public void addPara(Obj var) {
		this.paras.add(var);
	}

	private Obj findArg(String argName, List<Obj> serviceParas) {
		for (Obj obj : serviceParas) {
			if (obj.name.equals(argName))
				return obj;
		}
		return null;
	}

	/***
	 * @param name
	 * @param paraTypes  separator --
	 * @param parasNames separator --
	 */
	public Funct(String name, String paraTypes, String parasNames) {
		this.name = name;
		this.paras = new ArrayList<>();
		String[] types = paraTypes.split(Operator.SEPARATOR);
		String[] names = parasNames.split(Operator.SEPARATOR);
		assert (types.length == names.length);
		for (int i = 0; i < types.length; i++) {
			if (!names[i].startsWith("?")) {
				names[i] = "?" + names[i];
			}
			Obj p = new Obj(types[i], names[i]);
			this.paras.add(p);
		}
	}

	@Override
	public String toString() {
		return name + "-" + paras.toString();
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
//		return toString().equals(obj.toString());
		return compareType((Funct) obj);
	}

	public String getSignature() {
		String result = this.name;
		for (int i = 0; i < paras.size(); i++) {
			Obj p = this.paras.get(i);
			result += " " + p.type;
		}
		return result.trim();
	}

	public boolean compareType(Funct other) {
		if (!name.equals(other.name) || this.paras.size() != other.paras.size())
			return false;

		for (int i = 0; i < paras.size(); i++) {
			Obj p1 = this.paras.get(i);
			Obj p2 = other.paras.get(i);
			if (!p1.compareType(p2))
				return false;
		}
		return true;
	}

	public boolean canBeGroundedTo(GroundedFunct prop) {
		Funct f = prop.funct;
		if (!name.equals(f.name) || this.paras.size() != f.paras.size())
			return false;

		for (int i = 0; i < paras.size(); i++) {
			Obj p1 = this.paras.get(i);
			Obj p2 = f.paras.get(i);

			if (!p1.canBeGroundedTo(p2))
				return false;
		}
		return true;

	}
}

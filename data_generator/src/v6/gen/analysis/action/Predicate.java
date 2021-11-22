package v6.gen.analysis.action;

import java.util.ArrayList;
import java.util.List;

public class Predicate {
	public String name;
	public List<Parameter> pars;

	/***
	 * @param name
	 * @param paraTypes  separator --
	 * @param parasNames separator --
	 */
	public Predicate(String name, String paraTypes, String parasNames) {
		this.name = name;
		this.pars = new ArrayList<>();
		String[] types = paraTypes.split(Operator.SEPARATOR);
		String[] names = parasNames.split(Operator.SEPARATOR);
		assert (types.length == names.length);
		for (int i = 0; i < types.length; i++) {
			Parameter p = new Parameter(types[i], names[i]);
			this.pars.add(p);
		}
	}

	@Override
	public String toString() {
		return name + "-" + pars.toString();
	}

	@Override
	public boolean equals(Object obj) {
//		return toString().equals(obj.toString());
		return compareType((Predicate) obj);
	}

	public boolean compareType(Predicate other) {
		if (!name.equals(other.name) || this.pars.size() != other.pars.size())
			return false;

		for (int i = 0; i < pars.size(); i++) {
			Parameter p1 = this.pars.get(i);
			Parameter p2 = other.pars.get(i);
			if (!p1.compareType(p2))
				return false;
		}
		return true;
	}
}

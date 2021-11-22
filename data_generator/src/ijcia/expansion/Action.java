package ijcia.expansion;

import java.util.HashSet;
import java.util.Set;

public class Action {
	String name;
	Set<String> preconds;
	Set<String> effects;
	Set<String> neffects;

	public Action() {
		preconds = new HashSet<String>();
		effects = new HashSet<String>();
		neffects = new HashSet<String>();
	}

	public Set<String> getAllPredicates() {
		Set<String> predicates = new HashSet<String>();
		for (String p : preconds) {
			predicates.add(p);
		}
		for (String p : effects) {
			predicates.add(p);
		}
		for (String p : neffects) {
			predicates.add(p);
		}
		return predicates;
	}

	public Set<String> getNeffects() {
		return neffects;
	}

	public void setNeffects(Set<String> neffects) {
		this.neffects = neffects;
	}

	public void setEffects(Set<String> effects) {
		this.effects = effects;
	}

	public void setPreconds(Set<String> preconds) {
		this.preconds = preconds;
	}

	public Set<String> getPreconds() {
		return preconds;
	}

	public Set<String> getEffects() {
		return effects;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return toString().equals(obj.toString());
	}

	public String toPDDL() {
		String result = "(:action " + name + "\n";

		String precondPDDL = "";
		for (String pre : preconds) {
			precondPDDL += ("(" + pre + ") ");
		}

		String effectPDDL = "";
		for (String e : effects) {
			effectPDDL += ("(" + e + ") ");
		}

		for (String e : neffects) {
			effectPDDL += ("(not (" + e + ")) ");
		}

		result += "\t:precondition (and " + precondPDDL + " )\n";
		result += "\t:effect (and " + effectPDDL + " )\n";
		result += ")";
		return result;
	}

	public String toFormattedText(String tab) {
		String result = tab + name + "\n";

		String precondPDDL = tab + "pre: ";
		for (String pre : preconds) {
			precondPDDL += (pre + ", ");
		}

		String effectPDDL = tab + "e+: ";
		for (String e : effects) {
			effectPDDL += (e + ", ");
		}

		String neffectPDDL = tab + "e-: ";
		for (String e : neffects) {
			neffectPDDL += (e + ", ");
		}

		result += precondPDDL + "\n";
		result += effectPDDL + "\n";
		result += neffectPDDL + "\n";
		return result;
	}

}

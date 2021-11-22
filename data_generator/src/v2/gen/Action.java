package v2.gen;

import java.util.HashSet;
import java.util.Set;

public class Action {
	String name;
	Set<String> preconds;
	Set<String> pEffects;
	Set<String> nEffects;

	public Action() {
		preconds = new HashSet<String>();
		pEffects = new HashSet<String>();
		nEffects = new HashSet<String>();
	}
	
	public Action(String name, Set<String> preconds, Set<String> pEffects, Set<String> nEffects) {
		this.name = name;
		this.preconds = preconds;
		this.pEffects = pEffects;
		this.nEffects = nEffects;
	}

	public Set<String> getAllPredicates() {
		Set<String> predicates = new HashSet<String>();
		for (String p : preconds) {
			predicates.add(p);
		}
		for (String p : pEffects) {
			predicates.add(p);
		}
		for (String p : nEffects) {
			predicates.add(p);
		}
		return predicates;
	}

	public Set<String> getNeffects() {
		return nEffects;
	}

	public void setNeffects(Set<String> neffects) {
		this.nEffects = neffects;
	}

	public void setEffects(Set<String> effects) {
		this.pEffects = effects;
	}

	public void setPreconds(Set<String> preconds) {
		this.preconds = preconds;
	}

	public Set<String> getPreconds() {
		return preconds;
	}

	public Set<String> getEffects() {
		return pEffects;
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

	public String toPDDL() {
		String result = "(:action " + name + "\n";

		String precondPDDL = "";
		for (String pre : preconds) {
			precondPDDL += ("(" + pre + ") ");
		}

		String effectPDDL = "";
		for (String e : pEffects) {
			effectPDDL += ("(" + e + ") ");
		}

		for (String e : nEffects) {
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
		for (String e : pEffects) {
			effectPDDL += (e + ", ");
		}

		String neffectPDDL = tab + "e-: ";
		for (String e : nEffects) {
			neffectPDDL += (e + ", ");
		}

		result += precondPDDL + "\n";
		result += effectPDDL + "\n";
		result += neffectPDDL + "\n";
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		Action other = (Action) obj;
		if(other.preconds.equals(other.preconds))
			return false;
		if(other.pEffects.equals(other.pEffects))
			return false;
		if(other.nEffects.equals(other.nEffects))
			return false;
		return true;
	}
}

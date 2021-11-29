package v5.gen.supply_chain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import v5.gen.analysis.Analyzer;
import v6.gen.analysis.action.DendentActionCombo;
import v6.gen.analysis.action.Operator;

public class Action {
	String name;
	protected Set<String> preconds;
	protected Set<String> pEffects;
	protected Set<String> nEffects;

	int currentLevel = 0;
	Set<Action> dependents;
	DendentActionCombo combo;

	Operator op;

	public Action() {
		preconds = new HashSet<String>();
		pEffects = new HashSet<String>();
		nEffects = new HashSet<String>();
		dependents = new HashSet<Action>();
	}

	public Action(String name) {
		this.name = name;
		preconds = new HashSet<String>();
		pEffects = new HashSet<String>();
		nEffects = new HashSet<String>();
		dependents = new HashSet<Action>();
	}

	public Action(String name, Set<String> preconds, Set<String> pEffects, Set<String> nEffects) {
		this.name = name;
		this.preconds = preconds;
		this.pEffects = pEffects;
		this.nEffects = nEffects;
		dependents = new HashSet<Action>();
	}

	public Operator getOp() {
		return op;
	}

	public void setOp(Operator op) {
		this.op = op;
	}

	public int getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(int currentLevel) {
		this.currentLevel = currentLevel;
	}

	public void addDependent(Action a) {
		this.dependents.add(a);
	}

	public Set<Action> getDependents() {
		return dependents;
	}

	public void setDependents(Set<Action> dependents) {
		this.dependents = dependents;
	}

	public void addPre(String pre) {
		this.preconds.add(pre);
	}

	public void addPEffect(String ef) {
		this.pEffects.add(ef);
	}

	public void addNEffect(String ef) {
		this.nEffects.add(ef);
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
			precondPDDL += ("(" + pre.trim().replace(" ", "-") + ") ");
		}

		String effectPDDL = "";
		for (String e : pEffects) {
			effectPDDL += ("(" + e.trim().replace(" ", "-") + ") ");
		}

		for (String e : nEffects) {
			effectPDDL += ("(not (" + e.trim().replace(" ", "-") + ")) ");
		}

		result += "\t:precondition (and " + precondPDDL + " )\n";
		result += "\t:effect (and " + effectPDDL + " )\n";
		result += ")";
		return result;
	}

	public String toFormattedText(String tab) {
		String result = tab + name + "\n";

		String precondPDDL = tab + "--> pre: ";
		for (String pre : preconds) {
			precondPDDL += (pre + ", ");
		}

		String effectPDDL = tab + "--> e+: ";
		for (String e : pEffects) {
			effectPDDL += (e + ", ");
		}

		String neffectPDDL = tab + "--> e-: ";
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
		if (other.preconds.equals(other.preconds))
			return false;
		if (other.pEffects.equals(other.pEffects))
			return false;
		if (other.nEffects.equals(other.nEffects))
			return false;
		return true;
	}

	public void addPEffects(Set<String> effects) {
		this.pEffects.addAll(effects);
	}

	public String getName() {
		return name;
	}

	public void identifyDirectDependents(List<Action> acts) {
		Set<Action> result = new HashSet<>();
		for (Action other : acts) {
			if (!other.equals(this) && Analyzer.isIntersected(this.getEffects(), other.getPreconds()))
				result.add(other);
		}
		this.setDependents(result);
	}

	public Set<Action> getAllDependents(int level) {
		Set<Action> result = new HashSet<Action>();
		if (level == 0)
			return result;
		result.addAll(dependents);
		if (level - 1 > 0) {
			Set<Action> indirectDependents = new HashSet<>();
			level--;
			for (Action act : dependents)
				indirectDependents.addAll(act.getDependents());
			for (Action act : indirectDependents) {
				result.add(act);
				result.addAll(act.getAllDependents(level - 1));
			}
		} else {
			for (Action act : dependents) {
				result.add(act);
				result.addAll(act.getAllDependents(level - 1));
			}
		}
		return result;
	}

	public boolean isDependentOf(Action other) {
		List<String> othersEffects = new ArrayList<>(other.pEffects);
		othersEffects.retainAll(this.preconds);
		return !othersEffects.isEmpty();
	}

	public List<String> getConnectors(Action dependent) {
		List<String> result = new ArrayList<String>();
		for (String ef : this.pEffects) {
			for (String pre : dependent.preconds) {
				if (ef.equals(pre))
					result.add(pre);
			}
		}
		return result;
	}

	public void setCombo(DendentActionCombo combo) {
		this.combo = combo;
	}

	public DendentActionCombo getCombo() {
		return combo;
	}

	protected void addNEffects(Set<String> neffects) {
		this.nEffects.addAll(neffects);
	}
	
	
	/**
	 * Take appropriate actions in remainingActs, add them to the set of members.
	 * (Rovers). Different from setMembers
	 * @param remainingActs
	 */
	public void takeMembers(Set<Action> remainingActs) {
	}

	public Set<Action> getAllMembers() {
		return null;
	}

	public int getSize() {
		return 1;
	}
}

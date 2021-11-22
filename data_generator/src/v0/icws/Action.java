package v0.icws;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gen.utils.Utils;
import v0.icws.adaptive.NoOpAction;
import v5.gen.analysis.Analyzer;
import v5.gen.supply_chain.ChainGenerator;

public class Action {
	public String name;
	public Set<String> preconds;
	public Set<String> pEffects;
	public Set<String> nEffects;
	Map<String, Double> QoS;

	int currentLevel = 0;
	Set<Action> dependents;

	public Map<String, Double> QoS() {
		return QoS;
	}
	public Set<NoOpAction> getNoOpActions() {
		Set<NoOpAction> noops = new HashSet<NoOpAction>();
		for (String effect : pEffects) {
			NoOpAction noop = new NoOpAction(effect);
			noops.add(noop);
		}
		for (String pred : preconds) {
			NoOpAction noop = new NoOpAction(pred);
			noops.add(noop);
		}
		return noops;
	}

	public Action() {
		preconds = new HashSet<String>();
		pEffects = new HashSet<String>();
		nEffects = new HashSet<String>();
		dependents = new HashSet<Action>();
		QoS = new HashMap<>();
	}

	public void setQoS(Map<String, Double> qoS) {
		QoS = qoS;
	}

	public static String genActName() {
		return "ACT-" + Utils.getAlphaNumericString(ChainGenerator.MAX_ACT_NAME_LEN);
	}

	public Action(String name) {
		this.name = name;
		preconds = new HashSet<String>();
		pEffects = new HashSet<String>();
		nEffects = new HashSet<String>();
		dependents = new HashSet<Action>();
		QoS = new HashMap<>();
	}

	public Action(String name, Set<String> preconds, Set<String> pEffects, Set<String> nEffects) {
		this.name = name;
		this.preconds = preconds;
		this.pEffects = pEffects;
		this.nEffects = nEffects;
		dependents = new HashSet<Action>();
		QoS = new HashMap<>();
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
	public boolean isApplicable(Collection<String> state) {
		if (preconds.isEmpty())
			return true;
		return (new HashSet<String>(state)).containsAll(preconds);
	}

	public void generateQoS(int numQoSAttrs, int mode) {
		QoS = new HashMap<String, Double>(numQoSAttrs);
		for (int i = 1; i <= numQoSAttrs; i++) {
			String att = "ATT-" + i;
			int val = 0;
			if (mode == Cate.SHORT_EXPENSIVE_SOL)
				val = Utils.randomNumber(Cate.MIN_ATT_VAL_EXPENSIVE, Cate.MAX_ATT_VAL_EXPENSIVE);
			else if(mode == Cate.LONG_CHEAP_SOL)
				val = Utils.randomNumber(Cate.MIN_ATT_VAL_CHEAP, Cate.MAX_ATT_VAL_CHEAP);
			else 
				val = Utils.randomNumber(Cate.MIN_ATT_VAL_EXPENSIVE, Cate.MAX_ATT_VAL_EXPENSIVE);
			QoS.put(att, (double) val);
		}
	}
}

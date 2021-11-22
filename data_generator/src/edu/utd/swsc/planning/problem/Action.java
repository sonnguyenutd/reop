package edu.utd.swsc.planning.problem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Action {
	String name;
	List<Prop> inputs;
	List<Prop> outputs;
	Set<Prop> preconds;
	Set<Prop> effects;
	Set<Prop> nEffects;

	public Action(String name, Set<String> preconds, Set<String> effects, Set<String> neffects) {
		this.name = name;
		this.preconds = new HashSet<Prop>();
		for (String p : preconds) {
			this.preconds.add(new Prop(p));
		}
		this.effects = new HashSet<Prop>();
		for (String e : effects) {
			this.effects.add(new Prop(e));
		}
		this.nEffects = new HashSet<Prop>();
		for (String ne : neffects) {
			this.nEffects.add(new Prop(ne));
		}
	}

	public Set<NoOpAction> getNoOpActions() {
		Set<NoOpAction> noops = new HashSet<NoOpAction>();
		for (Prop effect : effects) {
			NoOpAction noop = new NoOpAction(effect);
			noops.add(noop);
		}
		for (Prop pred : preconds) {
			NoOpAction noop = new NoOpAction(pred);
			noops.add(noop);
		}
		return noops;
	}

	@Override
	public boolean equals(Object obj) {
		return toString().equals(obj.toString());
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public String toString() {
		return name + "\n*" + preconds + "\n+" + effects + "\n-" + nEffects;
	}

	public Action() {
		inputs = new ArrayList<Prop>();
		outputs = new ArrayList<Prop>();
		preconds = new HashSet<Prop>();
		effects = new HashSet<Prop>();
	}

	public Action(String name, List<Prop> inputs, List<Prop> outputs, Set<Prop> preconds, Set<Prop> effects) {
		this.name = name;
		this.inputs = inputs;
		this.outputs = outputs;
		this.preconds = preconds;
		this.effects = effects;
	}

	public boolean isApplicable(Collection<Prop> state) {
		if (preconds.isEmpty())
			return true;
		return (new HashSet<Prop>(state)).containsAll(preconds);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Prop> getInputs() {
		return inputs;
	}

	public void setInputs(List<Prop> inputs) {
		this.inputs = inputs;
	}

	public List<Prop> getOutputs() {
		return outputs;
	}

	public void setOutputs(List<Prop> outputs) {
		this.outputs = outputs;
	}

	public Set<Prop> getPreconds() {
		return preconds;
	}

	public void setPreconds(Set<Prop> preconds) {
		this.preconds = preconds;
	}

	public Set<Prop> getEffects() {
		return effects;
	}

	public void setEffects(Set<Prop> effects) {
		this.effects = effects;
	}

	public Set<Prop> getNEffects() {
		return nEffects;
	}
}

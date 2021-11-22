package edu.utd.swsc.planning.problem;

import java.util.HashSet;
import java.util.Set;

public class Prop {
	String name;

	public Prop(String name) {
		this.name = name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
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

	public Set<Action> getProducers(Set<Action> allProducers) {
		Set<Action> result = new HashSet<>();
		for (Action a : allProducers) {
			if (a.getEffects().contains(this))
				result.add(a);
		}
		return result;
	}
}

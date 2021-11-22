package edu.utd.swsc.planning.problem;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import gen.utils.Utils;

public class PlanProblem {
	Set<Prop> init;
	Set<Prop> goal;

	public PlanProblem() {
	}

	public static PlanProblem fromFile(String file) {
		PlanProblem p = null;
		String content = Utils.read(file);
		String[] lines = content.replace("[", "").replace("]", "").trim().split("\n");
		if (lines.length == 2) {
			String[] in = lines[0].split(",");
			Set<Prop> init = new HashSet<Prop>();
			for (String pr : in)
				init.add(new Prop(pr.trim()));

			String[] out = lines[1].split(",");
			Set<Prop> goal = new HashSet<Prop>();
			for (String pr : out)
				goal.add(new Prop(pr.trim()));
			p = new PlanProblem(init, goal);
		}
		return p;
	}

	public PlanProblem(Collection<Prop> init, Collection<Prop> goal) {
		this.init = new HashSet<>(init);
		this.goal = new HashSet<>(goal);
	}

	public void setInit(Set<Prop> init) {
		this.init = init;
	}

	public void setGoal(Set<Prop> goal) {
		this.goal = goal;
	}

	public Set<Prop> getInit() {
		return init;
	}

	public Set<Prop> getGoal() {
		return goal;
	}

	@Override
	public String toString() {
		String result = "init (" + init.size() + "): ";
		for (Prop prop : init) {
			result += "\t" + prop.getName();
		}
		result += "\n" + "goal (" + goal.size() + "): ";
		for (Prop prop : goal) {
			result += "\t" + prop.getName();
		}
		return result;
	}
}

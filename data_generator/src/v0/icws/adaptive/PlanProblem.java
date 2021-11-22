package v0.icws.adaptive;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import gen.utils.Utils;

public class PlanProblem {
	Set<String> init;
	Set<String> goal;

	public PlanProblem() {
	}

	public static PlanProblem fromFile(String file) {
		PlanProblem p = null;
		String content = Utils.read(file);
		String[] lines = content.replace("[", "").replace("]", "").trim().split("\n");
		if (lines.length == 2) {
			String[] in = lines[0].split(",");
			Set<String> init = new HashSet<String>();
			for (String pr : in)
				init.add(pr.trim());

			String[] out = lines[1].split(",");
			Set<String> goal = new HashSet<String>();
			for (String pr : out)
				goal.add(pr.trim());
			p = new PlanProblem(init, goal);
		}
		return p;
	}

	public PlanProblem(Collection<String> init, Collection<String> goal) {
		this.init = new HashSet<>(init);
		this.goal = new HashSet<>(goal);
	}

	public void setInit(Set<String> init) {
		this.init = init;
	}

	public void setGoal(Set<String> goal) {
		this.goal = goal;
	}

	public Set<String> getInit() {
		return init;
	}

	public Set<String> getGoal() {
		return goal;
	}

	@Override
	public String toString() {
		String result = "init (" + init.size() + "): ";
		for (String prop : init) {
			result += "\t" + prop;
		}
		result += "\n" + "goal (" + goal.size() + "): ";
		for (String prop : goal) {
			result += "\t" + prop;
		}
		return result;
	}
}

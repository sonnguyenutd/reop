package edu.utd.swsc.graphplan;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.utd.swsc.common.Service;
import edu.utd.swsc.planning.problem.Action;
import edu.utd.swsc.planning.problem.PlanDomain;
import edu.utd.swsc.planning.problem.PlanProblem;
import edu.utd.swsc.planning.problem.Prop;

public class PlanningGraph {
	List<Level> levels;
	public int CUR_LEVEL;
	PlanDomain domain;
	PlanProblem problem;

	public static void main(String[] args) {
		Set<Service> ss = Service.parseAllXML(new File("/Users/sonnguyen/Desktop/backup/services/1.1"));
		Set<String> ALL_IN = new HashSet<String>();
		Set<String> ALL_OUT = new HashSet<String>();
		for (Service s : ss) {
			ALL_IN.addAll(s.getInputTypes());
			ALL_OUT.addAll(s.getOutputTypes());
		}
	}

	public PlanningGraph(PlanDomain d, PlanProblem p) {
		CUR_LEVEL = 0;
		this.domain = d;
		this.problem = p;
		levels = new ArrayList<Level>();
		PropLayer propLayer = new PropLayer(domain.getActions(), problem.getInit());
		Level init = Level.createInit(propLayer);
		levels.add(init);
	}

	public boolean isFixedPoint() {
		if (CUR_LEVEL == 0)
			return false;
		Level last = levels.get(CUR_LEVEL);
		Level prev = levels.get(CUR_LEVEL - 1);
		return last.P.props.size() == prev.P.props.size();
	}

	public Level getCurLevel() {
		return levels.get(CUR_LEVEL);
	}

	public void expand() {
		Level curr = levels.get(CUR_LEVEL);
		Level next = curr.createNext();
		levels.add(next);
		CUR_LEVEL++;
	}

	private Solution search(Set<Prop> goal, Solution currentSolution, int level) {
		if (goal.isEmpty()) {
			Solution PI = extractSolution(currentSolution.getPreconds(), level - 1);
			if (!PI.equals(Solution.FAILURE)) {
				return PI.appends(currentSolution, level - 1);
			}
			return Solution.FAILURE;
		}
		Level currLevel = levels.get(level);
		Prop subGoal = selectOne(goal);
		Set<Action> providers = currLevel.getProviders(subGoal);
		if (providers.isEmpty())
			return Solution.FAILURE;
		Action a = chooseOne(providers);
		Set<Prop> newGoal = new HashSet<Prop>(goal);
		newGoal.removeAll(a.getEffects());
		return search(newGoal, currentSolution.appends(a), level);
	}

	public Solution extractSolution(Set<Prop> goal, int i) {
		if (i == 0)
			return Solution.EMPTY_SOLUTION;
		Level currLevel = levels.get(i);
		if (currLevel.isNogood(goal))
			return Solution.FAILURE;
		Solution currentSolution = search(goal, Solution.EMPTY_SOLUTION, i);
		if (!currentSolution.equals(Solution.FAILURE))
			return currentSolution;
		currLevel.updateNogood(goal);
		return Solution.FAILURE;
	}

	private Action chooseOne(Set<Action> providers) {
		for (Action a : providers)
			return a;
		return null;
	}

	private Prop selectOne(Set<Prop> goal) {
		for (Prop prop : goal)
			return prop;
		return null;
	}

	// ULTILITIES
	public Set<String> addedTypes() {
		Set<String> result = new HashSet<String>();
		Set<Prop> addedProps = levels.get(CUR_LEVEL).P.props;
		for (Prop prop : addedProps) {
			result.add(prop.getName());
		}
		return result;
	}

	public boolean reach(int k, Set<Prop> subgoal) {
		return levels.get(k).reach(subgoal);
	}

	public boolean reach(Set<Prop> goal) {
		return levels.get(CUR_LEVEL).reach(goal);
	}
}

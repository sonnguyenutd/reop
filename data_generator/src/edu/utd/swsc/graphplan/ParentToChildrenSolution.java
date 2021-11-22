package edu.utd.swsc.graphplan;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.utd.swsc.planning.problem.Action;
import edu.utd.swsc.planning.problem.ParentAction;
import edu.utd.swsc.planning.problem.Prop;

public class ParentToChildrenSolution {
	public static Solution toChildren(Solution parentSolution, Set<Prop> init, Set<Prop> goal) {
		List<Set<Action>> childrenExecution = new ArrayList<Set<Action>>();
		if (parentSolution.equals(Solution.FAILURE)) {
			System.out.println(childrenExecution);
			return Solution.FAILURE;
		}
		Solution chidrenSolution = new Solution();
		Set<Prop> achievedGoals = new HashSet<Prop>(init);

		Solution standard = parentSolution.removeNoOps();
		List<Set<Action>> execution = standard.toSteps();

//		System.out.println(standard.getSteps());

		for (int i = 0; i < execution.size() - 1; i++) {
			Set<Action> children = new HashSet<Action>();
			Set<Action> curr = execution.get(i);
			Set<Action> next = execution.get(i + 1);

			Set<Prop> subgoal = new HashSet<Prop>();
			for (Action a : next) {
				subgoal.addAll(a.getPreconds());
			}

			subgoal.removeAll(achievedGoals);

			for (Action act : curr) {
				Set<Prop> newSubGoals = new HashSet<Prop>(subgoal);
				newSubGoals.retainAll(act.getEffects());
				Set<Action> requiredActions = ((ParentAction) act).extractChildren(newSubGoals);
				chidrenSolution.actionSequence.addAll(requiredActions);

				achievedGoals.addAll(newSubGoals);

				children.addAll(requiredActions);
			}
			childrenExecution.add(children);
		}
		Set<Action> last = execution.get(execution.size() - 1);
		Set<Prop> subgoal = new HashSet<Prop>(goal);
		subgoal.removeAll(achievedGoals);
		Set<Action> children = new HashSet<Action>();
		for (Action act : last) {
			Set<Prop> newSubGoals = new HashSet<Prop>(subgoal);
			newSubGoals.retainAll(act.getEffects());
			Set<Action> requiredActions = ((ParentAction) act).extractChildren(newSubGoals);
			chidrenSolution.actionSequence.addAll(requiredActions);
			children.addAll(requiredActions);
		}
		childrenExecution.add(children);
		System.out.println(childrenExecution);
		return chidrenSolution;
	}

	private static Action selectOne(Set<Action> requiredActions) {
		for (Action action : requiredActions) {
			return action;
		}
		return null;
	}

	public static Solution toChildren2(Solution parentSolution, Set<Prop> init, Set<Prop> goal) {
		if (parentSolution.equals(Solution.FAILURE))
			return Solution.FAILURE;

		Solution chidrenSolution = new Solution();

		Set<Prop> neededGoals = new HashSet<Prop>(goal);
		neededGoals.removeAll(init);

		Solution standard = parentSolution.removeNoOps();

		List<Action> parentSequence = standard.getActionSequence();

		ParentAction last = (ParentAction) parentSequence.get(parentSequence.size() - 1);
		Set<Action> requiredActions = last.extractChildren(neededGoals);
		chidrenSolution.actionSequence.addAll(last.extractChildren(goal));

		for (int i = parentSequence.size() - 2; i >= 0; i--) {
			ParentAction curr = (ParentAction) parentSequence.get(i);
			ParentAction next = (ParentAction) parentSequence.get(i + 1);
			Set<Prop> subgoal = next.getPreconds();

			subgoal.removeAll(neededGoals);

			requiredActions = curr.extractChildren(subgoal);
			chidrenSolution.actionSequence.addAll(requiredActions);
			for (Action action : requiredActions)
				neededGoals.addAll(action.getEffects());
		}
		return chidrenSolution;
	}
}

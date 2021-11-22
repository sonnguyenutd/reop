package v0.icws.adaptive;

public class GraphPlanner {
	public static void main(String[] args) {

	}

	public static Solution plan(PlanDomain domain, PlanProblem problem) {
		PlanningGraph G = new PlanningGraph(domain, problem);
		while (true) {
			G.expand();
			if (G.isFixedPoint()) {
				break;
			}
			if (G.reach(problem.getGoal()))
				break;
		}
		if (!G.reach(problem.getGoal())) {
			return Solution.FAILURE;
		}

		int nogoodSize = !G.reach(problem.getGoal()) ? 0 : G.getCurLevel().nogood.size();

		Solution PI = G.extractSolution(problem.getGoal(), G.CUR_LEVEL);
		while (PI.equals(Solution.FAILURE)) {
			G.expand();
			PI = G.extractSolution(problem.getGoal(), G.CUR_LEVEL);
			if (PI.equals(Solution.FAILURE) && G.isFixedPoint()) {
				if (nogoodSize == G.getCurLevel().nogood.size())
					return Solution.FAILURE;
				nogoodSize = G.getCurLevel().nogood.size();
			}
		}
		return PI;
	}
}

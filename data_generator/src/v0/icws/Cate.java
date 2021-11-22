package v0.icws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gen.utils.Utils;
import v0.icws.adaptive.GraphPlanner;
import v0.icws.adaptive.PlanDomain;
import v0.icws.adaptive.PlanProblem;
import v0.icws.adaptive.Solution;
import v5.gen.supply_chain.ChainGenerator;

public class Cate extends Action {
	public static final int SHORT_EXPENSIVE_SOL = 1;
	public static final int LONG_CHEAP_SOL = 2;
	public static final int GENERAL = 3;
	public static int MAX_ATT_VAL_EXPENSIVE = 100;
	public static int MIN_ATT_VAL_EXPENSIVE = 50;

	public static int MAX_ATT_VAL_CHEAP = 50;
	public static int MIN_ATT_VAL_CHEAP = 1;

	public static int MAX_ACTS_PER_CAT = 10;
	public static int NUM_QOS_ATTRS = 3;
	public static int counter = 1;
	Set<Action> acts;
	int id;

	public static void main(String[] args) {
		int numOfProps = 100;
		int numOfCate = 20;
		int solShortLength = 4;
		int solLongLength = 6;
		numOfCate = numOfCate - solShortLength - solLongLength;
		int counter = 0;
		Set<String> allProps = generateProps(numOfProps);

		int initSize = Utils.randomNumber(1, ChainGenerator.MAX_NUM_PREDICATES);
		Set<String> init = Utils.randomSet(allProps, initSize);
		int goalSize = Utils.randomNumber(1, ChainGenerator.MAX_NUM_PREDICATES);
		Set<String> goal = Utils.randomSet(allProps, goalSize);

//			System.out.println(init);
//			System.out.println(goal);

		allProps.removeAll(init);
		allProps.removeAll(goal);

		List<Cate> cates1 = generateBySolutions(init, solShortLength, goal, SHORT_EXPENSIVE_SOL, allProps);
		List<Cate> cates2 = generateBySolutions(init, solLongLength, goal, LONG_CHEAP_SOL, allProps);

		Map<String, Double> minQoS1 = Workflow.getMin(cates1);
//			System.out.println(minQoS1); 
		Map<String, Double> minQoS2 = Workflow.getMin(cates2);
//			System.out.println(minQoS2);

		List<Cate> allCates = genearteCates(numOfCate, allProps);
		allCates.addAll(cates2);
		allCates.addAll(cates1);

		long start = System.currentTimeMillis();
		PlanProblem prob = new PlanProblem(init, goal);
		PlanDomain domain = new PlanDomain();
		domain.setActions(allCates);
		domain.addProps(init);
		domain.addProps(goal);

		Solution s = GraphPlanner.plan(domain, prob);

		computeQoS(allCates, 1);
		CompositeService cs1 = new CompositeService(s.getActionSequence());
//			System.out.println("AVG: " + cs1.QoS());

		computeQoS(allCates, 2);
		CompositeService cs2 = new CompositeService(s.getActionSequence());
//			System.out.println("MIN: " + cs2.QoS());

		computeQoS(allCates, 3);
		CompositeService cs3 = new CompositeService(s.getActionSequence());
//			System.out.println("MAX: " + cs3.QoS());

		String att = "ATT-1";
		Double estimated = cs2.QoS().get(att);
		Double real = minQoS1.get(att);

		System.out.println(Math.abs(estimated - real) / real);
		System.out.println(System.currentTimeMillis()-start);

	}

	private static void computeQoS(List<Cate> allCates, int mode) {
		for (Cate cate : allCates) {
			if (mode == 1)
				cate.computeQoS_AVG();
			if (mode == 2)
				cate.computeQoS_MIN();
			if (mode == 3)
				cate.computeQoS_MAX();
		}
	}

	public void computeQoS_AVG() {
		this.QoS = new HashMap<String, Double>();
		for (int i = 1; i <= NUM_QOS_ATTRS; i++) {
			String att = "ATT-" + i;
			double sum = 0;
			for (Action a : acts) {
				sum += a.QoS().get(att);
			}
			this.QoS.put(att, sum / (double) acts.size());
		}
	}

	public void computeQoS_MIN() {
		this.QoS = new HashMap<String, Double>();
		for (int i = 1; i <= NUM_QOS_ATTRS; i++) {
			String att = "ATT-" + i;
			double val = MAX_ATT_VAL_EXPENSIVE;
			for (Action a : acts) {
				if (a.QoS().get(att) < val) {
					val = a.QoS().get(att);
				}
			}
			this.QoS.put(att, val);
		}
	}

	public void computeQoS_MAX() {
		this.QoS = new HashMap<String, Double>();
		for (int i = 1; i <= NUM_QOS_ATTRS; i++) {
			String att = "ATT-" + i;
			double val = MIN_ATT_VAL_CHEAP;
			for (Action a : acts) {
				if (a.QoS().get(att) > val) {
					val = a.QoS().get(att);
				}
			}
			this.QoS.put(att, val);
		}
	}

	private static List<Cate> genearteCates(int numOfCate, Set<String> allProps) {
		List<Cate> result = new ArrayList<>();
		while (result.size() < numOfCate) {
			Cate c = generateACate(GENERAL, allProps);
			result.add(c);
		}
		return result;
	}

	public static List<Cate> generateBySolutions(Set<String> init, int length, Set<String> goal, int mode,
			Set<String> allProps) {
		List<Cate> result = new ArrayList<>();
		if (length < 1)
			return null;
		List<Set<String>> prevStates = new ArrayList<>();
		Set<String> currState = new HashSet<>(init);
		while (result.size() < length - 1) {
			Cate c = generateACate(currState, mode, allProps);
			// Make sure that c is not applicable for the prev states
			if (!isApplicableOnAnyState(c, prevStates)) {
				result.add(c);
				Set<String> temp = new HashSet<>(currState);
				temp.addAll(c.getEffects());
				temp.removeAll(c.getNeffects());
				currState = temp;
				prevStates.add(currState);
			}
		}
		while (true) {
			Cate last = generateACate(currState, mode, allProps);
			if (!isApplicableOnAnyState(last, prevStates)) {
				last.setEffects(goal);
				result.add(last);
				break;
			}

		}
		return result;
	}

	private static boolean isApplicableOnAnyState(Cate c, List<Set<String>> prevStates) {
		for (int i = 0; i < prevStates.size() - 1; i++) {
			Set<String> state = prevStates.get(i);
			if (c.isApplicable(state))
				return true;
		}
		return false;
	}

	public static Cate generateACate(Set<String> currState, int mode, Set<String> allProps) {
		Cate c = new Cate();
		int numOfPreconds = Utils.randomNumber(1, ChainGenerator.MAX_NUM_PREDICATES);
		Set<String> preconds = Utils.randomSet(currState, numOfPreconds);
		c.setPreconds(preconds);
		while (true) {
			int numOfEffects = Utils.randomNumber(1, ChainGenerator.MAX_NUM_PREDICATES);
			Set<String> effects = Utils.randomSet(allProps, numOfEffects);
			if (Utils.intersect(preconds, effects)) {
				continue;
			}

			int numOfNEffects = Utils.randomNumber(1, numOfPreconds);
			Set<String> nEffects = Utils.randomSet(allProps, numOfNEffects);
			if (Utils.intersect(nEffects, effects)) {
				continue;
			}
			c.setNeffects(nEffects);
			c.setEffects(effects);
			break;
		}

		int numOfActions = Utils.randomNumber(1, MAX_ACTS_PER_CAT);
		c.generateActions(numOfActions, mode);
		return c;
	}

	public static Cate generateACate(int mode, Set<String> allProps) {
		Cate c = new Cate();
		int numOfPreconds = Utils.randomNumber(1, ChainGenerator.MAX_NUM_PREDICATES);
		Set<String> preconds = Utils.randomSet(allProps, numOfPreconds);
		c.setPreconds(preconds);
		while (true) {
			int numOfEffects = Utils.randomNumber(1, ChainGenerator.MAX_NUM_PREDICATES);
			Set<String> effects = Utils.randomSet(allProps, numOfEffects);
			if (Utils.intersect(preconds, effects))
				continue;

			int numOfNEffects = Utils.randomNumber(1, numOfPreconds);
			Set<String> nEffects = Utils.randomSet(allProps, numOfNEffects);
			if (Utils.intersect(nEffects, effects))
				continue;
			c.setNeffects(nEffects);
			c.setEffects(effects);
			break;
		}

		int numOfActions = Utils.randomNumber(1, MAX_ACTS_PER_CAT);
		c.generateActions(numOfActions, mode);
		return c;
	}

	public void generateActions(int numOfActions, int mode) {
		while (acts.size() < numOfActions) {
			Action a = new Action(Action.genActName());
			a.setEffects(this.getEffects());
			a.setPreconds(this.getPreconds());
			a.setNeffects(this.getNeffects());
			a.generateQoS(NUM_QOS_ATTRS, mode);
			acts.add(a);
		}
	}

	public Cate() {
		id = counter++;
		name = "CAT-" + id;
		acts = new HashSet<>();
	}

	public void addAct(Action a) {
		acts.add(a);
	}

	public static Set<String> genPredicateSet(int numOfPredicates) {
		Set<String> result = new HashSet<>();
		while (result.size() < numOfPredicates) {
			String pred = Utils.getAlphaNumericString(ChainGenerator.MAX_PREDICATE_LEN);
			result.add(pred);
		}
		return result;

	}

	@Override
	public String toString() {
		return name + "---" + acts.size();
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setActs(Set<Action> acts) {
		this.acts = acts;
	}

	public Set<Action> getActs() {
		return acts;
	}

	public int getId() {
		return id;
	}

	public String toFormattedText(String tab) {
		String result = "";

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

	public static Set<String> generateProps(int numOfProps) {
		return genPredicateSet(numOfProps);
	}

	public Action toAction() {
		Action result = new Action("CAT-" + id);
		result.setPreconds(preconds);
		result.setEffects(pEffects);
		result.setNeffects(nEffects);
		return result;
	}

}

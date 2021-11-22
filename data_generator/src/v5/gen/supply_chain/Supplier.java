package v5.gen.supply_chain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gen.utils.Utils;

public class Supplier {
	String loc;
	Part part;
	String supplierName;

	// Generated part
	List<Process> ps;
	Action producingAct;

	Set<String> allPredicates;
	Set<Action> randomActions;

	// Actions grouped by relax1-2 - old
	Map<Action, Collection<Action>> groupedActions;

	// Actions grouped by basic
	Map<Action, Collection<Action>> groupedBasicActions;

	// Action grouped by basic + relax12
	Map<Action, Collection<Action>> groupedRelax12Actions;

	public Supplier(String loc, List<Process> ps, Action producingAct, Part part, String supplierName) {
		this.loc = loc;
		this.ps = ps;
		this.producingAct = producingAct;
		this.part = part;
		this.supplierName = supplierName;
		this.allPredicates = new HashSet<>();
		groupedActions = new HashMap<Action, Collection<Action>>();
	}

	public Part getPart() {
		return part;
	}

	public List<Action> getActions() {
		List<Action> result = new ArrayList<>();
		result.add(producingAct);
//		System.out.println("REQUIRED: " + part.getRequiredParts().size());
		for (Process p : ps) {
			result.addAll(p.getSteps());
		}
		return result;
	}

	public Supplier(String name, Part part, String loc) {
		groupedActions = new HashMap<Action, Collection<Action>>();
		this.allPredicates = new HashSet<>();
		this.supplierName = name;
		this.loc = loc;
		this.part = part;
		ps = new ArrayList<>();
		if (!part.getRequiredParts().isEmpty())
			createProcess();
		else
			createProcudingAct();
		generateAdditionalActions();
//		System.out.println("SUPPLIER: " + supplierName + "---" + part.getRequiredParts().size() + "-->Process: " + ps.size());
	}

	private void generateAdditionalActions() {
		int numOfAdditionalActs = Utils.randomNumber(1, ChainGenerator.MAX_RANDOM_ACTS_PER_SUPPLIER);
		randomActions = new HashSet<>();
		while (randomActions.size() < numOfAdditionalActs) {
			Set<String> oreconditions = genPredicateSet(part);
			Action newAct = new Action(genActName(part.getName()));
			newAct.setPreconds(oreconditions);
			Set<String> effects = genPredicateSet(part);
			newAct.setEffects(effects);
			randomActions.add(newAct);

			// Assuming random action is not grouped
			groupedActions.put(newAct, new HashSet<>());
		}
		// Generate actions with similar preconditions
		List<Action> allGeneratedActions = getActions();
		allGeneratedActions.addAll(randomActions);
		int numOfAdditionalActsBasic = Utils.randomNumber(1, ChainGenerator.MAX_RANDOM_ACTS_PER_SUPPLIER);
		List<Action> additionalActionForBasic = geneateAdditionalActionsBasic(allGeneratedActions,
				numOfAdditionalActsBasic);
		randomActions.addAll(additionalActionForBasic);
	}

	public List<Action> getREPActionsForBasic() {
		List<Action> result = new ArrayList<>();
		List<Action> allGeneratedActions = getActions();
		allGeneratedActions.addAll(randomActions);

		groupedBasicActions = new HashMap<Action, Collection<Action>>();
		Map<Set<String>, Set<Action>> groups = Utils.cluster(allGeneratedActions);
		for (Set<String> preconds : groups.keySet()) {
			Action newAct = new Action(genActName(part.getName()));
			newAct.setPreconds(preconds);
			Set<Action> acts = groups.get(preconds);
			Set<String> effects = new HashSet<>();
			for (Action child : acts) {
				effects.addAll(child.getEffects());
			}
			newAct.setEffects(effects);
			result.add(newAct);
			groupedBasicActions.put(newAct, acts);
		}

		return result;
	}

	public List<Action> getREPActionsForRelax12() {
		List<Action> result = new ArrayList<>();
		groupedRelax12Actions = new HashMap<Action, Collection<Action>>();

		Map<Set<String>, Set<Action>> groups = Utils.cluster(randomActions);

		// Combining
		for (Action rep : groupedActions.keySet()) {
			Action combinedREP = new Action(rep.getName(), rep.getPreconds(), rep.getEffects(), rep.getNeffects());

			Collection<Action> children = groupedActions.get(rep);
			Set<Action> potentialSiblings = groups.get(rep.getPreconds());
			if (potentialSiblings != null) {
				children.addAll(potentialSiblings);
				for (Action act : potentialSiblings) {
					combinedREP.addPEffects(act.getEffects());
				}
				groups.remove(rep.getPreconds());
			} 
			result.add(combinedREP);
			groupedRelax12Actions.put(combinedREP, children);
		}
		
		//Handling the rest
		for (Set<String> preconds : groups.keySet()) {
			Action newAct = new Action(genActName(part.getName()));
			newAct.setPreconds(preconds);
			Set<Action> acts = groups.get(preconds);
			Set<String> effects = new HashSet<>();
			for (Action child : acts) {
				effects.addAll(child.getEffects());
			}
			newAct.setEffects(effects);
			result.add(newAct);
			groupedRelax12Actions.put(newAct, acts);
		}


		return result;
	}
	
	public Map<Action, Collection<Action>> getGroupedBasicActions() {
		return groupedBasicActions;
	}
	public Map<Action, Collection<Action>> getGroupedRelax12Actions() {
		return groupedRelax12Actions;
	}

	private List<Action> geneateAdditionalActionsBasic(List<Action> allActs, int numOfActions) {
		List<Action> result = new ArrayList<>();
		while (result.size() < numOfActions) {
			Action newAct = new Action(genActName(part.getName()));
			int randomIndex = Utils.randomNumber(0, allActs.size() - 1);
			Action randomAction = allActs.get(randomIndex);
			newAct.setPreconds(randomAction.getPreconds());
			Set<String> effects = genPredicateSet(part);
			newAct.setEffects(effects);
			result.add(newAct);
		}
		return result;
	}

	private void createProcudingAct() {
		producingAct = new Action(genActName(part.getName()));
		producingAct.addPEffect(part.getCurrLoc(loc));
		// Available
		producingAct.addPEffect(part.getAvailable());
		allPredicates.addAll(producingAct.getAllPredicates());
		groupedActions.put(producingAct, new HashSet<>());
	}

	private void createProcess() {
		Set<Part> requiredParts = part.getRequiredParts();

		Set<String> lastPreconds = new HashSet<>();
		for (Part p : requiredParts) {
			Process process = new Process(p);
			int len = Utils.randomNumber(1, ChainGenerator.MAX_PROCESS_CHAIN_LEN);
			Action a = generateFirstAct(p);
			process.addStep(a);
			for (int i = 1; i < len; i++) {
				allPredicates.addAll(a.getAllPredicates());
				Set<String> pres = a.getEffects();
				// To make the all steps have at least one similar pre
				String pre = "CLOC-" + p.getName() + "-" + loc;
				pres.add(pre);
				Action newAct = new Action(genActName(p.getName()));
				newAct.setPreconds(pres);
				Set<String> predicates = genPredicateSet(p);
				newAct.setEffects(predicates);

				process.addStep(newAct);
				a = newAct;
			}
			lastPreconds.addAll(a.getEffects());
			ps.add(process);
		}
		producingAct = new Action(genActName(part.getName()));
		producingAct.setPreconds(lastPreconds);
		producingAct.addPEffect(part.getCurrLoc(loc));
		// Available
		producingAct.addPEffect(part.getAvailable());
		allPredicates.addAll(producingAct.getAllPredicates());

		// Grouping by relax 1-2
		for (Process p : ps) {
			Action groupedAct = groupByRelax1_2(p);
			groupedActions.put(groupedAct, p.getSteps());
		}
		groupedActions.put(producingAct, new HashSet<>());
	}

	public Set<Action> getREPActions() {
		return groupedActions.keySet();
	}

	public Map<Action, Collection<Action>> getGroupedActions() {
		return groupedActions;
	}

	private Action groupByRelax1_2(Process p) {
		List<Action> as = p.getSteps();
		Action result = new Action("REP-STEP-" + genActName(p.getPart().getName()));
		result.setPreconds(as.get(0).getPreconds());
		for (Action action : as) {
			result.addPEffects(action.getEffects());
		}
		return result;
	}

	private Set<String> genPredicateSet(Part p) {
		int numOfPredicates = Utils.randomNumber(1, ChainGenerator.MAX_NUM_PREDICATES);
		Set<String> result = new HashSet<>();
		while (result.size() < numOfPredicates) {
			String pred = Utils.getAlphaNumericString(ChainGenerator.MAX_PREDICATE_LEN);
			result.add(pred + "-" + p.getName());
		}
		return result;

	}

	private String genActName(String partName) {
		return supplierName + "-ACT-" + Utils.getAlphaNumericString(ChainGenerator.MAX_ACT_NAME_LEN) + "-" + partName;
	}

	private Action generateFirstAct(Part p) {
		Action a = new Action(genActName(p.getName()));
		String pre = "CLOC-" + p.getName() + "-" + loc;
		a.addPre(pre);
		Set<String> effects = genPredicateSet(p);
		a.setEffects(effects);
		return a;
	}

	public void setLoc(String loc) {
		this.loc = loc;
	}

	public String getLoc() {
		return loc;
	}

	@Override
	public String toString() {
		String result = "";
		for (Process p : ps) {
			result += (p.toString() + "\n");
		}
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		Supplier other = (Supplier) obj;
		return this.supplierName.equals(other.supplierName);
	}

	@Override
	public int hashCode() {
		return this.supplierName.hashCode();
	}

	public String getSupplierName() {
		return supplierName;
	}

	public Set<Action> getRandomActions() {
		return randomActions;
	}
}

package analysis.domain.service.grounding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import analysis.domain.service.Provider;
import analysis.domain.service.Service;
import analysis.domain.service.obj.Obj;

public class GroundedService {
	public Service symbolicService;
	
	public Provider provider;
	public String name;
	public List<GroundedPredicate> preconds;
	public List<GroundedPredicate> pEffects;
	public List<GroundedPredicate> nEffects;
	public List<GroundedNumericCondition> conditions;
	public List<GroundedNumericChange> changes;

	public List<Obj> arguments;
	public Map<String, Obj> paras_map;
	
	public GroundedService(String name) {
		this.name = name;
		preconds = new ArrayList<GroundedPredicate>();
		pEffects = new ArrayList<GroundedPredicate>();
		nEffects = new ArrayList<GroundedPredicate>();
		arguments = new ArrayList<>();
		conditions = new ArrayList<GroundedNumericCondition>();
		changes = new ArrayList<GroundedNumericChange>();
	}
	
	public GroundedService(Provider provider, String name) {
		this.name = name;
		preconds = new ArrayList<GroundedPredicate>();
		pEffects = new ArrayList<GroundedPredicate>();
		nEffects = new ArrayList<GroundedPredicate>();
		arguments = new ArrayList<>();
		this.provider = provider;
		conditions = new ArrayList<GroundedNumericCondition>();
		changes = new ArrayList<GroundedNumericChange>();
	}
	
	public void setSymbolicService(Service symbolicService) {
		this.symbolicService = symbolicService;
	}
	
	public void addPrecond(GroundedPredicate pre) {
		this.preconds.add(pre);
	}

	public void addPEffect(GroundedPredicate eff) {
		this.pEffects.add(eff);
	}

	public void addNEffect(GroundedPredicate eff) {
		this.nEffects.add(eff);
	}
	
	public void addCondition(GroundedNumericCondition c) {
		this.conditions.add(c);
	}

	public void addChange(GroundedNumericChange c) {
		this.changes.add(c);
	}

	public GroundedService(String name, List<GroundedPredicate> preconds, List<GroundedPredicate> pEffects, List<GroundedPredicate> nEffects) {
		this.name = name;
		this.preconds = preconds;
		this.pEffects = pEffects;
		this.nEffects = nEffects;
	}

	@Override
	public String toString() {
		String result = name + "\n\t@" + arguments + "\n\t*" + preconds + "\n\t#" + conditions + "\n\t+" + pEffects
				+ "\n\t^" + changes + "\n\t-" + nEffects;
		return result;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
}

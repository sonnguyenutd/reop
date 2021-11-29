package v8.gen.parser.rovers.group;

import java.util.HashSet;
import java.util.Set;

import v5.gen.supply_chain.Action;
import v8.gen.parser.rovers.CommunicateSoilData;
import v8.gen.parser.rovers.SampleSoil;

public class SampleSoilCommunicateSoilData extends Action {
	// For action whose effects are not used by anyone, then group them with others
	// which can produce its preconditions. (check init)
	Set<SampleSoil> sampleSoil;
	Set<CommunicateSoilData> com;

	public SampleSoilCommunicateSoilData() {
		super();
		sampleSoil = new HashSet<>();
		com = new HashSet<>();
	}

	public SampleSoilCommunicateSoilData(SampleSoil r) {
		super();
		sampleSoil = new HashSet<SampleSoil>();
		com = new HashSet<>();
		sampleSoil.add(r);
		setPreconds(r.getPreconds());
		setName("SampleSoilCommunicateSoilData-" + r.getName());
		addAct(r);
	}

	public void addAct(Action a) {
		if (a instanceof SampleSoil) {
			this.sampleSoil.add((SampleSoil) a);
		}
		if (a instanceof CommunicateSoilData) {
			this.com.add((CommunicateSoilData) a);
		}
		addPEffects(a.getEffects());
		addNEffects(a.getNeffects());
	}

	public void addActs(Set<SampleSoil> ss) {
		for (SampleSoil a : ss) {
			addAct(a);
		}
	}

	public int getSize() {
		return sampleSoil.size() + com.size();
	}

	@Override
	public void takeMembers(Set<Action> remainingActs) {
		Set<String> s = new HashSet<>(getPreconds());
		s.addAll(getEffects());
		for (Action a : remainingActs) {
			if (a instanceof CommunicateSoilData && s.containsAll(a.getPreconds()))
				addAct(a);
		}
	}

	@Override
	public Set<Action> getAllMembers() {
		Set<Action> result = new HashSet<Action>();
		result.addAll(sampleSoil);
		result.addAll(com);
		return result;
	}
}
package v8.gen.parser.rovers.group;

import java.util.HashSet;
import java.util.Set;

import v5.gen.supply_chain.Action;
import v8.gen.parser.rovers.CommunicateRockData;
import v8.gen.parser.rovers.SampleRock;

public class SampleRockCommunicateRockData extends Action {
	Set<SampleRock> sampleRock;
	Set<CommunicateRockData> com;

	public SampleRockCommunicateRockData() {
		super();
		sampleRock = new HashSet<SampleRock>();
		com = new HashSet<>();
	}

	public SampleRockCommunicateRockData(SampleRock r) {
		super();
		sampleRock = new HashSet<SampleRock>();
		com = new HashSet<>();
		sampleRock.add(r);
		setPreconds(r.getPreconds());
		setName("SampleRockCommunicateRockData-" + r.getName());
		addAct(r);
	}

	public void addAct(Action a) {
		if (a instanceof SampleRock) {
			this.sampleRock.add((SampleRock) a);
		}
		if (a instanceof CommunicateRockData) {
			this.com.add((CommunicateRockData) a);
		}
		addPEffects(a.getEffects());
		addNEffects(a.getNeffects());
	}
	public void addActs(Set<SampleRock> rs) {
		for (SampleRock a : rs) {
			addAct(a);
		}
	}
	public int getSize() {
		return sampleRock.size() + com.size();
	}
	@Override
	public void takeMembers(Set<Action> remainingActs) {
		Set<String> s = new HashSet<>(getPreconds());
		s.addAll(getEffects());
		for (Action a : remainingActs) {
			if (a instanceof CommunicateRockData && s.containsAll(a.getPreconds()))
				addAct(a);
		}
	}

	@Override
	public Set<Action> getAllMembers() {
		Set<Action> result = new HashSet<Action>();
		result.addAll(sampleRock);
		result.addAll(com);
		return result;
	}
}

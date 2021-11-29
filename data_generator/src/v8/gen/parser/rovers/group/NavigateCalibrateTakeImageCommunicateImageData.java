package v8.gen.parser.rovers.group;

import java.util.HashSet;
import java.util.Set;

import gen.utils.Utils;
import v5.gen.supply_chain.Action;
import v8.gen.parser.rovers.Calibrate;
import v8.gen.parser.rovers.CommunicateImageData;
import v8.gen.parser.rovers.Naviage;
import v8.gen.parser.rovers.TakeImage;

public class NavigateCalibrateTakeImageCommunicateImageData extends Action {
	Set<Naviage> nav;
	Set<Calibrate> cali;
	Set<TakeImage> takeImage;
	Set<CommunicateImageData> comImage;

	String at;

	public NavigateCalibrateTakeImageCommunicateImageData(String at) {
		super();
		this.at = at;
		nav = new HashSet<Naviage>();
		cali = new HashSet<Calibrate>();
		takeImage = new HashSet<>();
		comImage = new HashSet<>();
	}

	public NavigateCalibrateTakeImageCommunicateImageData(Naviage n) {
		super();
		nav = new HashSet<Naviage>();
		cali = new HashSet<Calibrate>();
		takeImage = new HashSet<>();
		comImage = new HashSet<>();
		nav.add(n);
		setPreconds(n.getPreconds());
		this.at = Utils.selectAnElm(n.getPreconds());
		setName("NavigateCalibrateTakeImageCommunicateImageData-" + this.at);
	}

	public NavigateCalibrateTakeImageCommunicateImageData() {
		super();
		nav = new HashSet<Naviage>();
		cali = new HashSet<Calibrate>();
		takeImage = new HashSet<>();
		comImage = new HashSet<>();
	}

	public void addAct(Action a) {
		if (a instanceof Naviage) {
			this.nav.add((Naviage) a);
		}
		if (a instanceof Calibrate) {
			this.cali.add((Calibrate) a);
		}
		if (a instanceof TakeImage) {
			this.takeImage.add((TakeImage) a);
		}
		if (a instanceof CommunicateImageData) {
			this.comImage.add((CommunicateImageData) a);
		}
		addPEffects(a.getEffects());
		addNEffects(a.getNeffects());
	}

	public void addActs(Set<Naviage> navs) {
		for (Naviage a : navs) {
			addAct(a);
		}
	}

	public int getSize() {
		return nav.size() + cali.size() + takeImage.size() + comImage.size();
	}

	@Override
	public void takeMembers(Set<Action> remainingActs) {
		//Adhoc
		Set<String> s = new HashSet<>(getPreconds());
		for (Action a : remainingActs) {
			if (a.getPreconds().contains(at) && a instanceof Calibrate) {
				addAct(a);
				s.addAll(a.getEffects());
			}
		}
		for (Action a : remainingActs) {
			if (a instanceof TakeImage && s.containsAll(a.getPreconds())) {
				addAct(a);
				s.addAll(a.getEffects());
			}
		}
		for (Action a : remainingActs) {
			if (a instanceof CommunicateImageData && s.containsAll(a.getPreconds()))
				addAct(a);
		}
	}

	@Override
	public Set<Action> getAllMembers() {
		Set<Action> result = new HashSet<Action>(nav);
		result.addAll(cali);
		result.addAll(takeImage);
		result.addAll(comImage);
		return result;
	}
}

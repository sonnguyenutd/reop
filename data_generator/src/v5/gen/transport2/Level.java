package v5.gen.transport2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gen.utils.Utils;

public class Level {
	List<Loc> locs;
	int lNumber;
	Set<Route> routes;
	List<Transporter> trans;
	
	public void generateTransporters(int numOfTrans, Collection<Package> packs) {
		while(trans.size() < numOfTrans) {
			String tranName = "TRAN-"+Utils.getAlphaNumericString(3);
			Transporter t = new Transporter(tranName);
			if(!trans.contains(t)) {
				t.generateActions(routes, packs);
				trans.add(t);
			}
		}
	}
	
	public List<Transporter> getTrans() {
		return trans;
	}
	
	
	public Level(int lNumber) {
		locs = new ArrayList<Loc>();
		this.lNumber = lNumber;
		routes = new HashSet<>();
		trans = new ArrayList<>();
	}
	public void addLoc(Loc l) {
		this.locs.add(l);
	}
	public int size() {
		return locs.size();
	}
	public List<Loc> getLocs() {
		return locs;
	}
	public void connect() {
		for (Loc l1 : locs) {
			l1.connect();
			for (Loc l2 : locs) {
				int r = Utils.randomNumber(1, Generator.BRANCHING_FACTOR);
				if (!l1.equals(l2) && r % Generator.BRANCHING_FACTOR != 0) {
					Route route = new Route(l1, l2);
					routes.add(route);
				}
			}
			routes.addAll(l1.getRoutes());
		}
	}
	public Set<Route> getRoutes() {
		return routes;
	}
}

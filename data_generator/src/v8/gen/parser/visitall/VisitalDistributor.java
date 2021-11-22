package v8.gen.parser.visitall;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import v8.gen.parser.Distributor;
import v8.gen.parser.GroundingThread;

public class VisitalDistributor extends Distributor {
	List<List<Pos>> combs;

	public VisitalDistributor(Collection<List<Pos>> combs) {
		this.combs = new ArrayList<>(combs);
	}

	@Override
	public List<GroundingThread> distribute(int numOfThreads) {
		List<GroundingThread> result = new ArrayList<>();
		Map<Integer, VisitallGroundingThread> map = new HashMap<Integer, VisitallGroundingThread>();
		for (int i = 0; i < numOfThreads; i++) {
			VisitallGroundingThread th = new VisitallGroundingThread(i);
			map.put(i, th);
			result.add(th);
		}
		for (int i = 0; i < combs.size(); i++) {
			VisitallGroundingThread th = map.get(i % numOfThreads);
			th.addComb(combs.get(i));
		}
		return result;
	}

}

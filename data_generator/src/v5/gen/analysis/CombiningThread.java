package v5.gen.analysis;

import java.util.List;

import v5.gen.supply_chain.Action;

public class CombiningThread implements Runnable {
	int id;
	List<Action> acts1;
	List<Action> acts2;

	public CombiningThread(int id) {
		this.id = id;
	}

	public CombiningThread(List<Action> acts1, List<Action> acts2, int id) {
		this.acts1 = acts1;
		this.acts2 = acts2;
		this.id = id;
	}

	@Override
	public void run() {
		Analyzer.combineLists(acts1, acts2);
		System.out.println("Combining " + id + " done!");
	}

	@Override
	public String toString() {
		return id + "";
	}

	@Override
	public boolean equals(Object obj) {
		return toString().equals(obj.toString());
	}
}

package v5.gen.analysis;

import java.util.ArrayList;
import java.util.List;

import v5.gen.supply_chain.Action;

public class AnalysisThread implements Runnable {
	int id;
	List<Action> actions;

	public AnalysisThread(int id) {
		this.id = id;
		actions = new ArrayList<Action>();
	}

	public AnalysisThread(List<Action> actions) {
		this.actions = actions;
	}

	@Override
	public void run() {
		Analyzer.identifyDirectDependents(actions);
		System.out.println("Thread " + id + " done!");
	}

	public List<Action> getActions() {
		return actions;
	}

	public void addAction(Action a) {
		this.actions.add(a);
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

package edu.utd.swsc.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.utd.swsc.planning.problem.Action;
import edu.utd.swsc.planning.problem.ParentAction;
import edu.utd.swsc.planning.problem.Prop;

public class ParentService extends Service {
	static int COUNTER = 1000000;
	Map<List<String>, Set<Service>> output2service;
	Set<Service> children;

	public Action toAction() {
		Set<Prop> preconds = new HashSet<Prop>();
		for (String input : inputTypes) {
			preconds.add(new Prop(input));
		}
		Set<Action> childrenAction = new HashSet<Action>();
		for (Service child : children) {
			childrenAction.add(child.toAction());
		}
		ParentAction action = new ParentAction(preconds, childrenAction);
		return action;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return hashCode() == obj.hashCode();
	}

	public ParentService() {
		super("parent_" + COUNTER);
		COUNTER++;
		children = new HashSet<Service>();
		output2service = new HashMap<List<String>, Set<Service>>();
	}

	public ParentService(List<String> input) {
		super("parent_" + COUNTER);
		COUNTER++;
		children = new HashSet<Service>();
		output2service = new HashMap<List<String>, Set<Service>>();
		this.inputTypes = new ArrayList<>(input);
	}

	private void render() {
		// Create dump parameters
		for (String type : this.inputTypes) {
			Parameter input = Parameter.create(type);
			this.inputs.add(input);
		}
		for (String type : this.outputTypes) {
			Parameter output = Parameter.create(type);
			this.outputs.add(output);
		}
	}

	public void add(Service s) {
		s.parent = this;
		children.add(s);
		this.outputTypes.addAll(s.getOutputTypes());
		Set<Service> ss = output2service.get(s.getOutputTypes());
		if (ss == null) {
			ss = new HashSet<Service>();
			ss.add(s);
			output2service.put(s.getOutputTypes(), ss);
		} else
			ss.add(s);
	}

	public void addAll(Set<Service> ss) {
		for (Service s : ss) {
			add(s);
		}
		render();
	}

	public void setChildren(Set<Service> children) {
		this.children = children;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Service> getChildren() {
		return children;
	}

	public String getName() {
		return name;
	}

	public static void main(String[] args) {

	}

}

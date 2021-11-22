package edu.utd.swsc.planning.problem;

public class NoOpAction extends Action {
	Prop para;

	public NoOpAction(Prop prop) {
		super();
		this.name = "NOOP_" + prop.getName();
		this.effects.add(prop);
		this.outputs.add(prop);
		this.inputs.add(prop);
		this.preconds.add(prop);
		this.para = prop;
	}

	public Prop getPara() {
		return para;
	}
}

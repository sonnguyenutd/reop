package analysis.domain.service;

import java.util.List;

import analysis.domain.service.obj.Obj;

public class Predicate extends Funct{

	public Predicate(String name, String paraTypes, String parasNames) {
		super(name, paraTypes, parasNames);
	}

	public Predicate(String content, List<Obj> paras) {
		super(content, paras);
	}

	public Predicate(String name) {
		super(name);
	}
}

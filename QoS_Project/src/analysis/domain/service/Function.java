package analysis.domain.service;

import java.util.List;

import analysis.domain.service.obj.Obj;

public class Function extends Funct{

	public Function(String name, String paraTypes, String parasNames) {
		super(name, paraTypes, parasNames);
	}

	public Function(String f, List<Obj> paras) {
		super(f, paras);
	}

}

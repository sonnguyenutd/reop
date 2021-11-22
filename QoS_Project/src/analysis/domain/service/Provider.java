package analysis.domain.service;

import analysis.domain.service.obj.Obj;

public class Provider extends Obj {

	public Provider(String type, String name) {
		super(type, name);
	}

	public Provider(Obj obj) {
		super(obj.type, obj.name);
	}

}

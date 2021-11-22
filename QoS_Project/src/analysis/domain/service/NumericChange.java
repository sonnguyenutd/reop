package analysis.domain.service;

import java.util.List;

import analysis.domain.service.obj.Obj;

/**
 * Just apply for simple change
 * object.property assign/+=/-=/... value/value of other object's property
 * operator property(object) value/property'(other)
 */
public class NumericChange extends NumericCC{

	public NumericChange(String content, List<Obj> paras) {
		super(content, paras);
	}
}

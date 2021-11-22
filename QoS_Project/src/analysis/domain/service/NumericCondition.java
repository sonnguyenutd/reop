package analysis.domain.service;

import java.util.List;

import analysis.domain.service.obj.Obj;

/**
 * Just apply for simple condition object.property operator value/value of other
 * object's property operator property(object) value/property'(other)
 */
public class NumericCondition extends NumericCC{

	public NumericCondition(String content, List<Obj> paras) {
		super(content, paras);
	}

	public static void main(String[] args) {
//		NumericCondition c = new NumericCondition("(> (capacity ?jug2) (amount ?jug2))");
	}

}

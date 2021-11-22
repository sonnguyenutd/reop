package analysis.domain.service.grounding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import analysis.domain.service.Funct;
import analysis.domain.service.Function;
import analysis.domain.service.NumericChange;
import analysis.domain.service.NumericCondition;
import analysis.domain.service.Predicate;
import analysis.domain.service.Provider;
import analysis.domain.service.Service;
import analysis.domain.service.obj.Obj;
import analysis.utils.Utils;

public class Grounder {
	public static void main(String[] args) {
		List<Service> services = Service.allServices();
//		Predicate loc_covered = new Predicate(Service.PRED_LOC_COVERED, Service.TYPE_LOC, "?x");
		Predicate thing_on_car = new Predicate(Service.PRED_ON,
				Service.TYPE_ROBOT + Service.SEPARATOR + Service.TYPE_CAR, "t" + Service.SEPARATOR + "c");

		Function pred = new Function("fuel", "car", "?c");
		GroundedFunct p = symbolicFunctGrounding(pred);
		System.out.println(thing_on_car);
		System.out.println(p);
	}

	public static GroundedFunct symbolicFunctGrounding(Funct pred) {
		GroundedFunct result = null;
		if (pred instanceof Predicate)
			result = new GroundedPredicate(pred);
		else
			result = new GroundedFunction(pred);
		List<Obj> arguments = new ArrayList<Obj>();
		for (Obj variable : pred.paras) {
			Obj groundedObj = createAGroundObj(variable, arguments);
			result.addObjs(groundedObj);
		}
		return result;
	}

	/**
	 * @param groundedObjs list of grounded objs, size = s.paras.size
	 * @param s
	 * @param pred         is a predicate in s
	 * @return
	 */
	public static GroundedPredicate symbolicPredicateGrounding(List<Obj> groundedObjs, Service s, Predicate pred) {
		GroundedPredicate result = new GroundedPredicate(pred);
		if (!pred.paras.isEmpty()) {
			List<Obj> serviceParas = s.paras;
			for (Obj variable : pred.paras) {
				int index = serviceParas.indexOf(variable);
				Obj groundedObj = groundedObjs.get(index);
//				assert(groundedObj!=null);
				result.addObjs(groundedObj);
			}
		}
		return result;
	}

	/**
	 * @param s
	 * @param pred is a predicate in s
	 * @return
	 */
	public static GroundedService symbolicServiceGrounding(Service s, GroundedFunct prop) {
		GroundedService gs = new GroundedService(s.name);
		gs.setSymbolicService(s);
		Funct matchedEffectOrChange = findMatchedEffectOrChange(s, prop);

		Map<String, Obj> variable_obj = new HashMap<>();
		for (int i = 0; i < matchedEffectOrChange.paras.size(); i++) {
			Obj variable = matchedEffectOrChange.paras.get(i);
			variable_obj.put(variable.toString(), prop.arguments.get(i));
		}

		List<Obj> arguments = new ArrayList<Obj>();
		for (int i = 0; i < s.paras.size(); i++) {
			Obj variable = s.paras.get(i);
			Obj arg = variable_obj.get(variable.toString());
			if (arg == null) {
				arg = createAGroundObj(variable, arguments);
			}
			arguments.add(arg);
		}

		gs.arguments = arguments;
		if (!arguments.isEmpty())
			gs.provider = new Provider(arguments.get(0));
		for (Predicate precond : s.preconds) {
			GroundedPredicate p = symbolicPredicateGrounding(arguments, s, precond);
			gs.addPrecond(p);
		}
		for (Predicate eff : s.pEffects) {
			GroundedPredicate p = symbolicPredicateGrounding(arguments, s, eff);
			gs.addPEffect(p);
		}
		for (Predicate eff : s.nEffects) {
			GroundedPredicate p = symbolicPredicateGrounding(arguments, s, eff);
			gs.addNEffect(p);
		}
		for (NumericCondition cond : s.conditions) {
			GroundedNumericCondition c = symbolicNumericGrounding(arguments, s, cond);
			gs.addCondition(c);
		}

		for (NumericChange change : s.changes) {
			GroundedNumericChange c = symbolicNumericGrounding(arguments, s, change);
			gs.addChange(c);
		}

		return gs;
	}

	private static GroundedNumericChange symbolicNumericGrounding(List<Obj> groundedObjs, Service s,
			NumericChange change) {
		GroundedNumericChange result = new GroundedNumericChange();
		String content = change.content;
		if (!change.participants.isEmpty()) {
			List<Obj> serviceParas = s.paras;
			for (Function f : change.participants) {
				GroundedFunction gf = new GroundedFunction(f);
				for (Obj variable : f.paras) {
					int index = serviceParas.indexOf(variable);
					Obj groundedObj = groundedObjs.get(index);
					gf.addObjs(groundedObj);

					content = content.replace(" " + variable.name + " ", " " + groundedObj.name + " ");
					content = content.replace(" " + variable.name + ")", " " + groundedObj.name + ")");
				}
				result.addParticipant(gf);
			}
			result.setContent(content);
		}
		return result;
	}

	private static GroundedNumericCondition symbolicNumericGrounding(List<Obj> groundedObjs, Service s,
			NumericCondition cond) {
		GroundedNumericCondition result = new GroundedNumericCondition();
		String content = cond.content;
		if (!cond.participants.isEmpty()) {
			List<Obj> serviceParas = s.paras;
			for (Function f : cond.participants) {
				GroundedFunction gf = new GroundedFunction(f);
				for (Obj variable : f.paras) {
					int index = serviceParas.indexOf(variable);
					Obj groundedObj = groundedObjs.get(index);
					gf.addObjs(groundedObj);

					content = content.replace(" " + variable.name + " ", " " + groundedObj.name + " ");
					content = content.replace(" " + variable.name + ")", " " + groundedObj.name + ")");
				}
				result.addParticipant(gf);
			}
			result.setContent(content);
		}

		return result;
	}

	private static Obj createAGroundObj(Obj variable, List<Obj> arguments) {
		Obj result = new Obj(variable.type);
		while (true) {
			result.name = variable.type + "-" + Utils.randomNumber(100, 999);
			if (!arguments.contains(result))
				break;
		}
		return result;
	}

	private static Funct findMatchedEffectOrChange(Service s, GroundedFunct prop) {
		if (prop instanceof GroundedPredicate) {
			for (Predicate pred : s.pEffects) {
				if (pred.canBeGroundedTo(prop))
					return pred;
			}
		} else {
			for (NumericChange c : s.changes) {
				for (Function f : c.participants) {
					if (f.canBeGroundedTo(prop))
						return f;
				}
			}
		}
		return null;
	}
}

package analysis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import analysis.domain.service.Funct;
import analysis.domain.service.Function;
import analysis.domain.service.NumericChange;
import analysis.domain.service.NumericCondition;
import analysis.domain.service.Predicate;
import analysis.domain.service.Service;
import analysis.domain.service.grounding.GroundedFunct;
import analysis.domain.service.grounding.Grounder;
import analysis.domain.service.obj.Obj;
import analysis.utils.Utils;

public class DomainParser {

	public static void main(String[] args) {
		List<Service> services = parseFile("/Users/sonnguyen/Desktop/test/test_new_1.pddl");
		Predicate pred = parsePredicate("(loc ?p - package-A ?l - location)");
		GroundedFunct p = Grounder.symbolicFunctGrounding(pred);
		System.out.println("---------------------");
		DomainAnalyzer.analyze(services, p);
		DomainAnalyzer.computeProviders();
	}

	public static Predicate parsePredicate(String predicateTxt) {
		//TODO: constants
//		(loc ?p - pt ?l - location)
		predicateTxt = predicateTxt.trim();
		if (predicateTxt.isBlank())
			return null;
		predicateTxt = predicateTxt.trim().replace("(", "").replace(")", "");
		int endingName = predicateTxt.indexOf(" ");
		String name = predicateTxt.substring(0, endingName).trim();

		Predicate result = new Predicate(name);

		String[] parts = predicateTxt.split(" ");
		Set<String> currParaSet = new HashSet<>();
		String currType = "";
		for (int i = 0; i < parts.length; i++) {
			if (parts[i].equals("-")) {
				currType = parts[i + 1];
				for (String pn : currParaSet) {
					Obj variable = new Obj(currType, pn);
					result.addPara(variable);
				}
				currParaSet = new HashSet<>();
			} else if (parts[i].startsWith("?")) {
				currParaSet.add(parts[i]);
			}
		}
		return result;
	}

	public static List<Service> parseFile(String file) {

		String content = Utils.readPDDL(file);

		Set<String> actSetText = new HashSet<>();
		int i = 0, j = i + 1;
		while (true) {
			i = content.indexOf("(:action", j);
			if (i == -1)
				break;
			j = Utils.findClosingParen(content.toCharArray(), i);
			if (j <= i)
				break;
			String act = content.substring(i, j + 1);
			actSetText.add(act);
			i = j + 1;
		}

		List<Service> services = new ArrayList<>();
		for (String actTxt : actSetText) {
			Service s = parseService(actTxt);
			services.add(s);
		}
		return services;
	}

	private static Service parseService(String actTxt) {
		int i, j;
		i = actTxt.indexOf("(:action") + "(:action".length();
		j = actTxt.indexOf("\n", i);
		String name = actTxt.substring(i, j).trim();
		Service s = new Service(name);

		i = actTxt.indexOf(":parameters");
		i = actTxt.indexOf("(", i);
		j = Utils.findClosingParen(actTxt.toCharArray(), i);
		String paras = actTxt.substring(i, j + 1);
		s.setParas(paras);

		i = actTxt.indexOf(":precondition");
		i = actTxt.indexOf("(", i);
		j = Utils.findClosingParen(actTxt.toCharArray(), i);
		String precondTxt = actTxt.substring(i + "(and".length(), j).trim();

		i = j = 0;
		while (true) {
			i = precondTxt.indexOf("(", j);
			if (i == -1)
				break;
			j = Utils.findClosingParen(precondTxt.toCharArray(), i) + 1;
			String p = precondTxt.substring(i, j);

			if (p.contains(">") || p.contains("<") || p.contains("=")) {
				NumericCondition cond = new NumericCondition(p, s.paras);
				s.addCond(cond);
			} else {
				Predicate pre = new Predicate(p.trim(), s.paras);
				s.addPrecond(pre);
			}
		}

		i = actTxt.indexOf(":effect");
		i = actTxt.indexOf("(", i);
		j = Utils.findClosingParen(actTxt.toCharArray(), i);
		String effectTxt = actTxt.substring(i + "(and".length(), j).trim();

		i = j = 0;
		while (true) {
			i = effectTxt.indexOf("(", j);
			if (i == -1)
				break;
			j = Utils.findClosingParen(effectTxt.toCharArray(), i) + 1;
			String e = effectTxt.substring(i, j).trim();
			if (e.startsWith("(decrease ") || e.startsWith("(increase ") || e.startsWith("(assign ")) {
				NumericChange change = new NumericChange(e, s.paras);
				s.addChange(change);
			} else {
				if (!e.startsWith("(not ")) {
					Predicate ep = new Predicate(e, s.paras);
					s.addPEffect(ep);
				} else {
					e = e.replace("(not ", "");
					e.substring(0, e.length() - 1);
					Predicate ep = new Predicate(e, s.paras);
					s.addNEffect(ep);
				}
			}
		}
		return s;
	}

	public static List<Service> parseDomain(String content) {

		Set<String> actSetText = new HashSet<>();
		int i = 0, j = i + 1;
		while (true) {
			i = content.indexOf("(:action", j);
			if (i == -1)
				break;
			j = Utils.findClosingParen(content.toCharArray(), i);
			if (j <= i)
				break;
			String act = content.substring(i, j + 1);
			actSetText.add(act);
			i = j + 1;
		}

		List<Service> services = new ArrayList<>();
		for (String actTxt : actSetText) {
			Service s = parseService(actTxt);
			services.add(s);
		}
		return services;
	}

	public static Set<Funct> allPredicates(List<Service> services) {
		Set<Funct> result = new HashSet<>();
		for (Service s : services) {
			for (Predicate p : s.pEffects)
				if (!isAddedFunct(p, result))
					result.add(p);

			for (Predicate p : s.preconds)
				if (!isAddedFunct(p, result))
					result.add(p);

			for (Function p : s.functs)
				if (!isAddedFunct(p, result))
					result.add(p);
		}
		return result;
	}

	private static boolean isAddedFunct(Funct p, Set<Funct> result) {
		for (Funct funct : result) {
			if (p.getSignature().equals(funct.getSignature()))
				return true;
		}
		return false;
	}
}

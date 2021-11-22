package analysis.test;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import analysis.DomainAnalyzer;
import analysis.DomainParser;
import analysis.ProblemCreator;
import analysis.ProblemParser;
import analysis.domain.service.Funct;
import analysis.domain.service.Predicate;
import analysis.domain.service.Provider;
import analysis.domain.service.Service;
import analysis.domain.service.grounding.GroundedFunct;
import analysis.domain.service.grounding.Grounder;
import analysis.domain.service.obj.Obj;
import analysis.utils.Utils;

public class Test1 {

	public static void main(String[] args) {
		String domainFile = "/Users/sonnguyen/Desktop/test/test1.pddl";
		String domain = Utils.readPDDL(domainFile);
		List<Service> services = DomainParser.parseDomain(domain);
		
		Set<Funct> allFuncts = DomainParser.allPredicates(services);
		System.out.println(allFuncts.size());
		for (Funct pre : allFuncts) {
			System.out.println(pre);
		}
		
		Predicate pred = DomainParser.parsePredicate("(loc ?p - package-B ?l - location)");
		GroundedFunct p = Grounder.symbolicFunctGrounding(pred);

		DomainAnalyzer.analyze(services, p);

		Map<String, Set<Provider>> reqiredProviderMap = DomainAnalyzer.computeProviders();
		Set<String> requiredProviderTypes = reqiredProviderMap.keySet();

		String prob = Utils.readPDDL("/Users/sonnguyen/Desktop/test/p11.pddl");
		
		Map<String, Set<Obj>> objs_map = ProblemParser.parseObjs(prob);
		Set<Obj> objs = new HashSet<>();
		for (String t : objs_map.keySet())
			objs.addAll(objs_map.get(t));
//
		objs = ProblemParser.parseObjInit(prob, objs);
		
		List<GroundedFunct> goals = ProblemParser.extractGoals(prob, objs);
		
		String unselectedPredicates = ProblemCreator.generateUnselectedPredicates(reqiredProviderMap);
		String dummyActions = ProblemCreator.createDummyActions(prob, objs, requiredProviderTypes);
		ProblemCreator.createDomain(dummyActions, unselectedPredicates, domain,
				"/Users/sonnguyen/Desktop/test/test4.pddl");
		ProblemCreator.createProblem(reqiredProviderMap, objs_map, prob,
				"/Users/sonnguyen/Desktop/test/p41.pddl");
	}

}

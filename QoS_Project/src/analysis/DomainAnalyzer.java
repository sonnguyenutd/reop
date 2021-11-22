package analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import analysis.domain.service.Provider;
import analysis.domain.service.Service;
import analysis.domain.service.grounding.GroundedFunct;
import analysis.domain.service.grounding.GroundedFunction;
import analysis.domain.service.grounding.GroundedNumericChange;
import analysis.domain.service.grounding.GroundedNumericCondition;
import analysis.domain.service.grounding.GroundedService;
import analysis.domain.service.grounding.Grounder;

/**
 * TODO: After analyzing, check the result if the number of objects can be
 * reduced.
 *
 */
public class DomainAnalyzer {
	private static int counter = 1000;
	static Set<GroundedService> consideredServices = new HashSet<GroundedService>();
	static Set<GroundedFunct> consideredFuncts = new HashSet<>();
	static Set<Provider> consideredProviders = new HashSet<>();

//	public static void main(String[] args) {
//		List<Service> services = Service.allServices();
//
////		Function pred = new Function("fuel", "car", "?c");
//		Predicate pred = new Predicate(Service.PRED_LOC_COVERED, Service.TYPE_LOC, "?x");
//		GroundedFunct p = Grounder.symbolicFunctGrounding(pred);
//		System.out.println(pred);
//		System.out.println(p);
//		analyze(services, p);
//		computeProviders();
//	}

	public static void analyze(List<Service> services, GroundedFunct p) {
		System.out.println("===============");
		System.out.println(">>>" + p);
		if (isConsidered(p, consideredFuncts))
			return;
		consideredFuncts.add(p);
		Set<GroundedService> producingServices = new HashSet<GroundedService>();

		for (Service s : services) {
			if (s.canProduce(p)
//					&& !canBeReused(s, p)
			) {
				GroundedService gs = Grounder.symbolicServiceGrounding(s, p);
				producingServices.add(gs);
//				System.out.println("-->" + gs.preconds);
				System.out.println("-->" + gs);
			}
		}

		consideredServices.addAll(producingServices);
		if (counter-- > 0 && !producingServices.isEmpty()) {
			for (GroundedService gs : producingServices) {
				List<GroundedFunct> conditions = new ArrayList<>(gs.preconds);
				for (GroundedNumericCondition c : gs.conditions) {
					for (GroundedFunct f : c.participants)
						conditions.add(f);
				}

				for (GroundedNumericChange c : gs.changes) {
					Set<GroundedFunct> dependingFuncts = findAllDependingFunct(c, p);
					conditions.addAll(dependingFuncts);
				}

				for (GroundedFunct f : conditions)
					analyze(services, f);
			}
		}
	}

	public static Map<String, Set<Provider>> computeProviders() {
		Map<String, Set<Provider>> providerCounters = new HashMap<>();
		for (GroundedService gs : consideredServices) {
			Provider p = gs.provider;
			Set<Provider> providers = providerCounters.get(p.type);
			if (providers == null)
				providers = new HashSet<Provider>();
			providers.add(p);
			providerCounters.put(p.type, providers);
		}

		for (String type : providerCounters.keySet()) {
			System.out.println(type + "---" + providerCounters.get(type).size());
		}
		return providerCounters;
	}

	public static Set<GroundedService> getConsideredServices() {
		return consideredServices;
	}

	private static Set<GroundedFunct> findAllDependingFunct(GroundedNumericChange c, GroundedFunct p) {
		Set<GroundedFunct> result = new HashSet<GroundedFunct>();
		if (!c.participants.isEmpty()) {
			GroundedFunction f = c.participants.get(0);
			if (f.equals(f)) {
				for (int i = 1; i < c.participants.size(); i++)
					result.add(c.participants.get(i));
			}
		}
		return result;
	}

	private static boolean isConsidered(GroundedFunct p, Set<GroundedFunct> checkedProps) {
		for (GroundedFunct checkedProp : checkedProps) {
//			if (p.funct.compareType(checkedProp.funct)) {
			if (p.funct.name.equals(checkedProp.funct.name)) {
				if (p.arguments.isEmpty())
					return true;
				if (p.arguments.get(0).equals(checkedProp.arguments.get(0))) {
					return true;
				}
			}
		}
		return false;
	}

	public static Service getServiceByName(String name, Collection<Service> sers) {
		for (Service service : sers) {
			if (service.name.equalsIgnoreCase(name))
				return service;
		}
		return null;
	}

}

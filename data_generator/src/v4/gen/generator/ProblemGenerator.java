package v4.gen.generator;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import v4.gen.obj.Order;

public class ProblemGenerator {
	public static Set<String> generate(int numOfProblem, Set<String> state, Collection<Order> orders) {
		Set<String> result = new HashSet<String>();
		while (result.size() < numOfProblem) {
			String init = getInitState(state);
			String goal = "";
			for (Order o : orders) {
				goal+= "\t\t"+o.getDone()+"\n";
			}
			String prob = 	"(define (problem p-" + result.size() + ")\n" + 
							"	(:domain test )\n" + 
							"	(:init \n"+ 
							"" + init + " \n" + "	)\n" + 
							"	(:goal (and \n" + goal + " \n" + "	))\n"+ 
							")";
			result.add(prob);
		}
		return result;
	}
	private static String getInitState(Set<String> state) {
		StringBuffer init = new StringBuffer();
		for (String s: state) {
			init.append("\t\t"+s + "\n");
		}
		return init.toString();
	}
}

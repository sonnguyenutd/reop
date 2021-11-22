package v3.gen.generator;

import java.util.Set;

public class DomainGenerator {
	public static void main(String[] args) {
		
	}

	public static String generate(Set<String> predicates, Set<String> actions) {
		StringBuffer result = new StringBuffer();
		result.append("(define (domain test)\n");

		result.append("(:predicates\n");
		for (String p : predicates) {
			result.append("\t" + p + "\n");
		}
		result.append(")");

		for (String a : actions) {
			result.append(a + "\n");
		}

		result.append(")");
		return result.toString();
	}
}

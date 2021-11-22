package edu.utd.swsc.common;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import gen.utils.Utils;

public class Domain {
	public static void main(String[] args) {
		Set<Service> ss = Service.parseAllXML(new File("/Users/sonnguyen/Desktop/backup/services/1.1"));
//		System.out.println(toPDDLNoTyping(ss));
		Utils.write("/Users/sonnguyen/Desktop/SC/domain_no_typing.pddl", toPDDLNoTyping(ss));
		Utils.write("/Users/sonnguyen/Desktop/SC/domain.pddl", toPDDL(ss));
	}
	
	public static String toPDDLNoTyping(Set<Service> services) {
		StringBuffer pddl = new StringBuffer();
		pddl.append("(define (domain swsc_no_typing)\n");
		pddl.append("\t(:requirements :strips)\n\n");
		
		
		String predicate = "(:predicates "
				+ "(agentHasKnowledgeAbout ?x)" + 
				")\n\n";
		
		pddl.append(predicate);
		
		
		for (Service s : services) {
			pddl.append("\t" + s.toPDDLNoTyping() + "\n\n");
		}
		pddl.append(")");
		return pddl.toString();
	}

	public static String toPDDL(Set<Service> services) {
		StringBuffer pddl = new StringBuffer();
		pddl.append("(define (domain swsc)\n");
		pddl.append("\t(:requirements :strips :typing)\n\n");
		String typesPDDL = "(:types";
		Set<String> types = new HashSet<String>();
		for (Service s : services) {
			types.addAll(s.getInputTypes());
			types.addAll(s.getOutputTypes());
		}

		for (String type : types) {
			typesPDDL += ("\n\t\t" + type+" - object");
		}
		typesPDDL = typesPDDL.trim() + ")";
		pddl.append("\t" + typesPDDL + "\n\n");
		
		
		String predicate = "(:predicates "
				+ "(agentHasKnowledgeAbout ?x - object)" + 
				")\n\n";
		
		pddl.append(predicate);
		
		
		for (Service s : services) {
			pddl.append("\t" + s.toPDDL() + "\n\n");
		}
		pddl.append(")");
		return pddl.toString();
	}
}

package edu.utd.swsc.common;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Problem {
	public static String toPDDLNoTyping(Case c) {
		String result = "(define (problem sc_no_typing_" + c.getID() + ")\n" + "\t(:domain swsc_no_typing)\n";

		String objects = "";
		List<Parameter> init = c.getInput();
		List<Parameter> goal = c.getOutput();
		Set<Parameter> paras = new HashSet<Parameter>(goal);
		paras.addAll(init);

		for (Parameter p : paras) {
			objects += "\t\t" + p.type + " \n";
		}

		result += "\t(:objects \n";
		result += objects;
		result += "\n\t)\n";

		String in = "";
		for (Parameter p : init) {
			in += ("\t\t " + "(" + Service.PREDICATE + " " + p.getType().replace(Service.REPLACE, "") + ")\n");
		}

		result += "\t(:init \n";
		result += in;
		result += "\n\t)\n";

		String out = "";
		for (Parameter p : goal) {
			out += ("\t\t " + "(" + Service.PREDICATE + " " + p.getType().replace(Service.REPLACE, "") + ")\n");
		}

		result += "\t(:goal ( and \n";
		result += out;
		result += "\n\t\t)\n\t)\n";

		result += ")";
		return result;
	}
	
	public static String toPDDL(Case c) {
		String result = "(define (problem sc_" + c.getID() + ")\n" + "\t(:domain swsc)\n";

		String objects = "";
		List<Parameter> init = c.getInput();
		List<Parameter> goal = c.getOutput();
		Set<Parameter> paras = new HashSet<Parameter>(goal);
		paras.addAll(init);

		for (Parameter p : paras) {
			objects += "\t\t" + p.name + " - " + p.type + "\n";
		}

		result += "\t(:objects \n";
		result += objects;
		result += "\n\t)\n";

		String in = "";
		for (Parameter p : init) {
			in += ("\t\t " + "(" + Service.PREDICATE + " " + p.getName().replace(Service.REPLACE, "") + ")\n");
		}

		result += "\t(:init \n";
		result += in;
		result += "\n\t)\n";

		String out = "";
		for (Parameter p : goal) {
			out += ("\t\t " + "(" + Service.PREDICATE + " " + p.getName().replace(Service.REPLACE, "") + ")\n");
		}

		result += "\t(:goal ( and \n";
		result += out;
		result += "\n\t\t)\n\t)\n";

		result += ")";
		return result;
	}
}

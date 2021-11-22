package gen.service;

import java.util.ArrayList;
import java.util.List;

import gen.general.Concretizer;
import gen.general.Obj;
import gen.general.ObjTemplate;
import gen.utils.Utils;

public class Service2PDDLConverter {
	public static void main(String[] args) {
		String tempFile = "/Users/sonnguyen/Desktop/objs.txt";
		List<ObjTemplate> temps = ObjTemplate.parse(tempFile);
		String sTempFile = "/Users/sonnguyen/Desktop/services.txt";
		List<ServiceTemplate> sTemps = ServiceTemplate.parse(sTempFile);
		ServiceTemplate stemp = sTemps.get(0);
//
		Obj o = Concretizer.randomlyConcritizeObj(temps.get(0));
		Obj from = new Obj("from", "loc");
		Obj to = new Obj("to", "loc");
		List<Obj> as = new ArrayList<>();
		as.add(from);
		as.add(to);
		Service s = Concretizer.concritizeService(o, as, stemp);
		System.out.println(s);

		String action = convert(s);
		System.out.println(action);
	}

	public static String convert(Service s) {
		String result = "(:action " + s.getProvider().getId() + "-" + s.getName() + "\n";
		result += " :precondition (and \n";
		for (String pre : s.getPreconds()) {
			result += "\t" + convertProp(pre) + "\n";
		}
		result += ")\n";
		result += " :effect (and \n";
		for (String pre : s.getpEffects()) {
			result += "\t" + convertProp(pre) + "\n";
		}
		for (String pre : s.getnEffects()) {
			result += "\t" + "(not " + convertProp(pre) + ")" + "\n";
		}
		result += ")\n";
//		
		return result;
	}

	private static String convertProp(String p) {
		String prop = "";
		p = p.replace("  ", " ").trim();
		String[] parts = p.trim().split("[\\-\\+=\\.\\>\\<]");
		List<String> ps = new ArrayList<>();
//		System.out.println(p);
//		System.out.println(parts.length);
		for (String part : parts) {
			if (!part.trim().isEmpty())
				ps.add(part.trim());
		}
//		System.out.println("------");
		if (ps.size() == 3) {
			String att = ps.get(1);
			String obj = ps.get(0);
			String value = ps.get(2);
			if (Utils.isBool(value)) {
				if (value.equals("T"))
					prop = "( " + att + " " + obj + ")";
				else
					prop = "(not ( " + att + " " + obj + "))";
			} else if (Utils.isNumeric(value)) {
				if (p.contains(" = "))
					prop = "(assign (" + att + " " + obj + ") " + value + ")";
				else if (p.contains(" == "))
					prop = "(== (" + att + " " + obj + ") " + value + ")";
				else if (p.contains(" += "))
					prop = "(increase (" + att + " " + obj + ") " + value + ")";
				else if (p.contains(" -= "))
					prop = "(decrease (" + att + " " + obj + ") " + value + ")";
			} else {
				prop = "( " + att + " " + obj + " " + value + ")";
			}
		}
		return prop;
	}
}

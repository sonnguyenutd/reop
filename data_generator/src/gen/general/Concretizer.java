package gen.general;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gen.service.Service;
import gen.service.ServiceTemplate;
import gen.state.State;

public class Concretizer {

	public static void main(String[] args) {
//		String tempFile = "/Users/sonnguyen/Desktop/objs.txt";
//		List<ObjTemplate> temps = ObjTemplate.parse(tempFile);
//		String sTempFile = "/Users/sonnguyen/Desktop/services.txt";
//		List<ServiceTemplate> sTemps = ServiceTemplate.parse(sTempFile);
//		ServiceTemplate temp = sTemps.get(0);
//
//		Obj prov = new Obj("carlalala", "sd-car");
//		Obj from = new Obj("from", "loc");
//		Obj to = new Obj("to", "loc");
//		List<Obj> as = new ArrayList<>();
//		as.add(from);
//		as.add(to);
//		Service s = concritizeService(prov, as, temp);
//		System.out.println(s);

//		Obj o = randomlyConcritizeObj(temps.get(0));
//		System.out.println(o);
		Map<String, List<Obj>> obj_map = new HashMap<String, List<Obj>>();
		List<String> paras = new ArrayList<>();

		List<Obj> A = new ArrayList<>();
		paras.add("A");
		paras.add("B");
		paras.add("B");
		paras.add("C");
		for (int i = 0; i < 3; i++) {
			Obj o = new Obj("a_" + i, "A");
			A.add(o);
		}
		obj_map.put("A", A);
		List<Obj> B = new ArrayList<>();
		for (int i = 0; i < 2; i++) {
			Obj o = new Obj("b_" + i, "B");
			B.add(o);
		}
		obj_map.put("B", B);
		List<Obj> C = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			Obj o = new Obj("c_" + i, "C");
			C.add(o);
		}
		obj_map.put("C", C);

		Set<List<Obj>> argSet = getArgSet(paras, obj_map);
		System.out.println(argSet.size());
		for (List<Obj> set : argSet) {
			System.out.println(set);
		}
	}

	public static Set<Service> concritizeService(ServiceTemplate temp, Map<String, List<Obj>> obj_map) {
		Set<Service> result = new HashSet<>();
		List<Obj> providers = obj_map.get(temp.getpType());
		if (!providers.isEmpty()) {
			for (Obj provider : providers) {
				Set<List<Obj>> argSet = getArgSet(temp.getParas(), obj_map);
				for (List<Obj> args : argSet) {
					Service s = concritizeService(provider, args, temp);
					result.add(s);
				}
			}
		}
		return result;
	}

	public static Set<List<Obj>> getArgSet(List<String> paras, Map<String, List<Obj>> obj_map) {
		Set<List<Obj>> result = new HashSet<>();
		List<String> newParas = new ArrayList<String>(paras);
		if (!paras.isEmpty()) {
			List<Obj> fArgs = obj_map.get(paras.get(0));
			if (paras.size() == 1) {
				for (Obj fArg : fArgs) {
					List<Obj> argSet = new ArrayList<Obj>();
					argSet.add(fArg);
					result.add(argSet);
				}
			} else {
				newParas.remove(0);
				Set<List<Obj>> smallerArgSet = getArgSet(newParas, obj_map);
				for (Obj fArg : fArgs) {
					for (List<Obj> smallerArg : smallerArgSet) {
						if (!smallerArg.contains(fArg)) {
							List<Obj> argSet = new ArrayList<Obj>();
							argSet.add(fArg);
							argSet.addAll(smallerArg);
							result.add(argSet);
						}
					}
				}
			}
		}
		return result;
	}

	public static Obj randomlyConcritizeObj(ObjTemplate temp) {
		Obj result = new Obj();
		Map<String, String> values = Generator.generate(temp.getTempState());
		State s = new State();
		for (String att : values.keySet())
			s.put(att, values.get(att));
		result.setState(s);
		result.setId(values.get("id"));
		result.setType(temp.getType());
		return result;
	}

	public static Service concritizeService(Obj p, List<Obj> args, ServiceTemplate temp) {
		Service s = new Service();
		List<String> paraNames = extractParaName(temp.getParas());
		Map<String, Obj> name_obj = new HashMap<>();
		for (int i = 0; i < args.size(); i++) {
			name_obj.put(paraNames.get(i), args.get(i));
		}
		name_obj.put("PRO", p);
		s.setName(temp.getName());
		s.setArgs(args);
		s.setProvider(p);

		List<String> preconds = concritizePredicate(temp.getPreconds(), name_obj);
		s.setPreconds(preconds);

		List<String> pEffects = concritizePredicate(temp.getpEffects(), name_obj);
		s.setpEffects(pEffects);

		List<String> nEffects = concritizePredicate(temp.getnEffects(), name_obj);
		s.setnEffects(nEffects);

		return s;
	}

	private static List<String> concritizePredicate(List<String> predicates, Map<String, Obj> name_obj) {
		List<String> results = new ArrayList<>(predicates);
		int index = 0;
		while (index < results.size()) {
			String pr = results.get(index);
			for (String name : name_obj.keySet()) {
				if (pr.contains(name + " "))
					pr = pr.replace(name + " ", " " + name_obj.get(name).getId() + " ");
				else if (pr.contains(" " + name)) {
					pr = pr.replace(" " + name, " " + name_obj.get(name).getId() + " ");
				}
			}
			results.remove(index);
			results.add(index, pr);
			index++;
		}
		return results;
	}

	private static List<String> extractParaType(List<String> parasType) {
		List<String> result = new ArrayList<>();
		for (String p : parasType) {
			String[] parts = p.split(" ");
			result.add(parts[0]);
		}
		return result;
	}

	private static List<String> extractParaName(List<String> parasType) {
		List<String> result = new ArrayList<>();
		for (String p : parasType) {
			String[] parts = p.trim().split(" ");
			result.add(parts[1]);
		}
		return result;
	}

}

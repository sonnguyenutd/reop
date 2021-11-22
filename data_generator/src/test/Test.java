package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gen.general.Concretizer;
import gen.general.Generator;
import gen.general.Obj;
import gen.general.ObjTemplate;
import gen.service.Service;
import gen.service.Service2PDDLConverter;
import gen.service.ServiceTemplate;
import gen.state.State2PDDLConverter;

public class Test {

	public static void main(String[] args) {
		// 1. Parse object template
		String tempFile = "/Users/sonnguyen/Desktop/objs.txt";
		List<ObjTemplate> objTemps = ObjTemplate.parse(tempFile);

		// 3. Generate object
		ObjTemplate objTemp = objTemps.get(0);
		Set<Obj> objs = Generator.generateObjects(objTemp, 3);
		Map<String, List<Obj>> obj_map = new HashMap<>();
		obj_map.put(objTemp.getType(), new ArrayList<>(objs));

		// 2. Parse service template
		String sTempFile = "/Users/sonnguyen/Desktop/services.txt";
		List<ServiceTemplate> serviceTemps = ServiceTemplate.parse(sTempFile);
		// 4. Concretes services
		ServiceTemplate serviceTemp = serviceTemps.get(0);
		Set<Service> concritizedServices = Concretizer.concritizeService(serviceTemp, obj_map);
		// 5. Convert services --> PDDL actions
		Set<String> pddlActions = new HashSet<>();
		for (Service s : concritizedServices) {
			String action = Service2PDDLConverter.convert(s);
			pddlActions.add(action);
		}
		// 6. Convert objects --> Objects + Their state
		Set<String> pddlProps = new HashSet<String>();
		for (String type : obj_map.keySet()) {
			List<Obj> objsWithType = obj_map.get(type);
			for (Obj o : objsWithType) {
				Set<String> props = State2PDDLConverter.convert(o);
				pddlProps.addAll(props);
			}
		}
		// 7. Generate goal corresponding to the task
		
	}

}

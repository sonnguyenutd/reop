package edu.utd.swsc.common;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Clusterer {
	public static Set<ParentService> group(Set<Service> services) {
		Set<ParentService> clusters = new HashSet<ParentService>();
		Map<List<String>, Set<Service>> dic = new HashMap<List<String>, Set<Service>>();
		for (Service s : services) {
			Set<Service> ss = dic.get(s.getInputTypes());
			if (ss == null) {
				ss = new HashSet<Service>();
				ss.add(s);
				dic.put(s.getInputTypes(), ss);
			} else {
				ss.add(s);
			}
		}
//		System.out.println(dic.keySet().size());
		for (List<String> in : dic.keySet()) {
//			System.out.println(dic.get(in).size() + "--" + in);
			ParentService p = new ParentService(in);
			p.addAll(dic.get(in));
			clusters.add(p);
		}
		return clusters;
	}

	public static void main(String[] args) {
		Set<Service> services = Service.parseAllXML(new File("/Users/sonnguyen/Desktop/backup/services/1.1"));
		Set<String> paras = new HashSet<String>();
		int counter =  0;
		for (Service service : services) {
//			paras.addAll(service.getInputTypes());
//			paras.addAll(service.getOutputTypes());
			counter+= service.getOutputs().size();
		}
		
		System.out.println((float)counter/services.size());
		
		Set<ParentService> parents = group(services);
		counter =  0;
		int max = 0;
		System.out.println(parents.size());
		for (ParentService parent : parents) {
//			paras.addAll(parent.getInputTypes());
//			paras.addAll(parent.getOutputTypes());
//			if(parent.children.size()>max)
//				max = parent.children.size();
			if(parent.getChildren().size() == 1)
				counter++;
		}
		System.out.println(counter);
		System.out.println((float)counter/parents.size());
	}
}

package test;

import java.io.FileNotFoundException;
import java.io.IOException;

import gen.utils.Utils;

public class RunnerMaker {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		String folder = "/Users/sonnguyen/Desktop/test/runners";
		String domain = "500";
//		
		String layer = "c";
		String txt = "fd-fdss___fd-lmcut___fd-ms___ff___lama___mercury___probe___symba___yahsp3";
		String[] planers = txt.split("___");
		for (String planner : planers) {
			String content = Utils.read(folder + "/runner_temp.sh");
			content = content.replace("XXX", planner);
			content = content.replace("YYY", domain);
			content = content.replace("ZZZ", layer);
			Utils.write(folder + "/runner_" + layer + "_" + planner + "_" + domain + ".sh", content);
		}
//		
//		layer = "c";
//		for (String planner : planers) {
//			String content = Utils.read("/Users/sonnguyen/Desktop/runners/runner_temp.sh");
//			content = content.replace("XXX", planner);
//			content = content.replace("YYY", domain);
//			content = content.replace("ZZZ", layer);
//			Utils.writeFile("/Users/sonnguyen/Desktop/runners/runner_"+layer+"_"+planner+"_"+domain+".sh", content);
//		}

//		File f = new File("/Users/sonnguyen/Desktop/runners2");
//		for (File p : f.listFiles()) {
//			if (p.getName().endsWith(".sh")) {
//				System.out.println("sudo docker cp " + p.getName() + " d7340b8ae893:/" + p.getName()+"\n");
//			}
//		}

//		File f = new File("/Users/sonnguyen/Downloads/FF-v2.3/data_new/dr/");
//		for (File p : f.listFiles()) {
//			if (p.getName().endsWith(".pddl")) {
//				System.out.println("sudo docker cp " + p.getName() + " d7340b8ae893:/" + p.getName()+"\n");
//			}
//		}

	}
}

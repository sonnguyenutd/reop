package gen.owls.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Reader {

	public static void main(String[] args) {
		File dir = new File ("/Users/sonnguyen/Downloads/drive-download-20210524T212359Z-001/OWLS-TC4_PDDL/htdocs/domains/1.1/weapon");
		for (File f : dir.listFiles()) {
			API a = read(f.getAbsolutePath());
			System.out.println(a.name);
		}
	}

	public static API read(String fileName) {
		API a = new API();
		a.name = (new File(fileName)).getName().replace(".owls", "");
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("<profile:hasInput")) {
					String in = extractInfo(line);
//					System.out.println(in);
					a.inputs.add(in);
				} else if (line.startsWith("<profile:hasOutput")) {
					String out = extractInfo(line);
					a.outputs.add(out);
				} else {

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return a;
	}

	private static String extractInfo(String line) {
		String result = null;
		int s = line.indexOf("\"");
		int e = line.lastIndexOf("\"");
		result = line.substring(s + 1, e);
		return result;
	}

	static class API {
		String name;
		Set<String> inputs;
		Set<String> outputs;
		Set<String> pres;
		Set<String> effs;

		public API() {
			inputs = new HashSet<String>();
			outputs = new HashSet<String>();
			pres = new HashSet<String>();
			effs = new HashSet<String>();
		}

		@Override
		public String toString() {
			return name + ":" + inputs + "-->" + outputs;
		}
	}

}

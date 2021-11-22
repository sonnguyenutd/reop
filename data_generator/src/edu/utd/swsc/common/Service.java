package edu.utd.swsc.common;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.utd.swsc.planning.problem.Action;
import edu.utd.swsc.planning.problem.Prop;
import gen.utils.Utils;

public class Service {
	public static boolean CUSTOMIZED = true;
	public static String REPLACE = "http://192.168.64.2/ontology/";
	public static String PREDICATE = "agentHasKnowledgeAbout";
	public static String DOT_REPLACE = "DDDDD";
	public static String DASH_REPLACE = "AAAAA";
	public static String SHARP_REPLACE = "SSSSS";
	public static String[] NUM_REPLACE = new String[] { "NUM0", "NUM1", "NUM2", "NUM3", "NUM4", "NUM5", "NUM6", "NUM7",
			"NUM8", "NUM9" };
	public String name;
	public List<String> inputTypes;
	public List<String> outputTypes;

	public Set<Parameter> inputs;
	public Set<Parameter> outputs;

	ParentService parent = null;

	public String toPDDLNoTyping() {
		String result = "";
		String actionName = name;

		Set<Parameter> paras = new HashSet<Parameter>();
		paras.addAll(inputs);
		paras.addAll(outputs);

		StringBuffer paras_str = new StringBuffer();
		for (Parameter para : paras) {
			paras_str.append("\n\t\t\t ?" + para.getType() + " ");
		}

		StringBuffer preconds = new StringBuffer();
		for (Parameter i : inputs) {
			preconds.append("(" + PREDICATE + " ?" + i.getType().replace(REPLACE, "") + ") ");
		}

		StringBuffer effects = new StringBuffer();
		for (Parameter o : outputs) {
			effects.append("(" + PREDICATE + " ?" + o.getType().replace(REPLACE, "") + ") ");
		}

		result = "(:action " + actionName + "\n" + "\t\t:parameters ( " + paras_str + ")\n";

		if (!inputs.isEmpty()) {
			result += "\t\t:precondition (and\n";
			result += "\t\t\t" + preconds + "\n" + "\t\t)\n";
		}
		if (!outputs.isEmpty()) {
			result += "\t\t:effect (and\n";
			result += "\t\t\t" + effects + "\n" + "\t\t)\n";
		}

		result += "\t)";

		return result;
	}

	public static void main(String[] args) {
//		Set<Service> services = Service.parse(new File("/Users/sonnguyen/Desktop/backup/domain.txt"));
//		System.out.println(services.size());
		Set<Service> ss = parseAllXML(new File("/Users/sonnguyen/Desktop/backup/services/1.1"));
		for (Service service : ss) {
			System.out.println(service.getOutputTypes());
			System.out.println(service.toPDDL());
		}
		System.out.println(ss.size());
	}

	public String toPDDL() {
		String result = "";
		String actionName = name;

		Set<Parameter> paras = new HashSet<Parameter>();
		paras.addAll(inputs);
		paras.addAll(outputs);

		StringBuffer paras_str = new StringBuffer();
		for (Parameter para : paras) {
			paras_str.append("\n\t\t\t ?" + para.getName().replace(REPLACE, "") + " - " + para.getType() + " ");
		}

		StringBuffer preconds = new StringBuffer();
		for (Parameter i : inputs) {
			preconds.append("(" + PREDICATE + " ?" + i.getName().replace(REPLACE, "") + ") ");
		}

		StringBuffer effects = new StringBuffer();
		for (Parameter o : outputs) {
			effects.append("(" + PREDICATE + " ?" + o.getName().replace(REPLACE, "") + ") ");
		}

		result = "(:action " + actionName + "\n" + "\t\t:parameters ( " + paras_str + ")\n";

		if (!inputs.isEmpty()) {
			result += "\t\t:precondition (and\n";
			result += "\t\t\t" + preconds + "\n" + "\t\t)\n";
		}
		if (!outputs.isEmpty()) {
			result += "\t\t:effect (and\n";
			result += "\t\t\t" + effects + "\n" + "\t\t)\n";
		}

		result += "\t)";

		return result;
	}

	public Service(String name) {
		this.name = name;
		inputTypes = new ArrayList<String>();
		outputTypes = new ArrayList<String>();
		inputs = new HashSet<Parameter>();
		outputs = new HashSet<Parameter>();
	}

	public static Set<Service> parseAllXML(File dir) {
		Set<Service> services = new HashSet<Service>();
		for (File f : dir.listFiles()) {
			if (f.getName().endsWith(".owls")) {
				Service s = parseXML(f);
				if (s != null)
					services.add(s);
			}
		}
		return services;
	}

	private static String normalize(String txt) {
		if (!CUSTOMIZED)
			return txt.replace(".", DOT_REPLACE).replace("#", SHARP_REPLACE).replace("_", DASH_REPLACE);
		return txt;
	}

	public static Service parseXML(File fXmlFile) {
		try {

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			NodeList inputList = doc.getElementsByTagName("process:Input");
			String name = normalize(fXmlFile.getName());
			if (!CUSTOMIZED)
				name = "SERVICE-" + normalize(fXmlFile.getName());
			Service s = new Service(name);

			for (int temp = 0; temp < inputList.getLength(); temp++) {
				Node nNode = inputList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element input = (Element) nNode;
					String paraName = input.getAttribute("rdf:ID");
					String paraType = input.getElementsByTagName("process:parameterType").item(0).getTextContent()
							.trim();
					// TODO Should we use datatype or name?
					paraType = paraType.replace(REPLACE, "");
					s.inputTypes.add(normalize(paraType));
					s.inputs.add(new Parameter(normalize(paraType), normalize(paraName)));
				}
			}
			NodeList outputList = doc.getElementsByTagName("process:Output");

			for (int temp = 0; temp < outputList.getLength(); temp++) {
				Node nNode = outputList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element output = (Element) nNode;
					String paraName = output.getAttribute("rdf:ID");
					String paraType = output.getElementsByTagName("process:parameterType").item(0).getTextContent()
							.trim();
					paraType = paraType.replace(REPLACE, "");
					s.outputTypes.add(normalize(paraType));
					s.outputs.add(new Parameter(normalize(paraType), normalize(paraName)));
				}
			}
			Collections.sort(s.inputTypes);
			Collections.sort(s.outputTypes);
			return s;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String toString() {
		return name + ":::" + inputTypes + ":::" + outputTypes;
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return toString().equals(obj.toString());
	}

	public static Set<Service> parse(File file) {
		Set<Service> services = new HashSet<Service>();
		String[] lines = Utils.read(file).trim().split("\n");
		for (String line : lines) {
			Service s = parse(line);
			if (s != null)
				services.add(s);
		}
		return services;
	}

	public static Service parse(String str) {
		str = str.replace(" ", "");
		String[] parts = str.split(":::");
		if (parts.length == 3) {
			Service s = new Service(parts[0]);
			String[] ip = parts[1].replace("]", "").replace("[", "").trim().split(",");
			List<String> input = Arrays.asList(ip);
			Collections.sort(input);
			s.setInputTypes(input);

			String[] op = parts[2].replace("]", "").replace("[", "").trim().split(",");
			List<String> output = Arrays.asList(op);
			Collections.sort(output);
			s.setOutputTypes(output);
			return s;
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getInputTypes() {
		return inputTypes;
	}

	public Set<Parameter> getInputs() {
		return inputs;
	}

	public Set<Parameter> getOutputs() {
		return outputs;
	}

	public void setInputTypes(List<String> input) {
		this.inputTypes = new ArrayList<String>();
		for (String i : input) {
			if (!i.isEmpty())
				this.inputTypes.add(i);
		}
	}

	public List<String> getOutputTypes() {
		return outputTypes;
	}

	public void setOutputTypes(List<String> output) {
		this.outputTypes = new ArrayList<String>();
		for (String i : output) {
			if (!i.isEmpty())
				this.outputTypes.add(i);
		}
	}

	public Action toAction() {
		Action action = new Action();
		action.setName(name);
		for (String input : inputTypes) {
			action.getInputs().add(new Prop(input));
			action.getPreconds().add(new Prop(input));
		}
		for (String output : outputTypes) {
			action.getOutputs().add(new Prop(output));
			action.getEffects().add(new Prop(output));
		}
		return action;
	}
}

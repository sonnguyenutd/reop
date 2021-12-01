package gen.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import v5.gen.supply_chain.Action;
import v5.gen.supply_chain.ChainGenerator;
import v5.gen.supply_chain.Config;

public class Utils {
	public static String read(String fileName) {
		StringBuffer content = new StringBuffer();
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
			while ((line = br.readLine()) != null) {
				content.append(line + "\n");
			}
		} catch (IOException e) {
			return null;
		}
		return content.toString();
	}

	public static String read(File f) {
		StringBuffer content = new StringBuffer();
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			String line;
			while ((line = br.readLine()) != null) {
				content.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content.toString();
	}

	public static void write(String filePath, String content) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
			bw.write(content);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean isValidUTF8(byte[] input) {
		CharsetDecoder cs = Charset.forName("UTF-8").newDecoder();
		try {
			cs.decode(ByteBuffer.wrap(input));
			return true;
		} catch (CharacterCodingException e) {
			return false;
		}
	}

	public static void write(List<String> content, String filePath) {
		BufferedOutputStream bout = null;
		try {
			bout = new BufferedOutputStream(new FileOutputStream(filePath));
			for (String line : content) {
				line += System.getProperty("line.separator");
				bout.write(line.getBytes("UTF-8"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bout != null) {
				try {
					bout.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public static boolean isNumeric(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static boolean isBool(String value) {
		if (value.equals("T") || value.equals("F"))
			return true;
		return false;
	}

	public static int randomNumber(int min, int max) {
		if (max < min)
			return min;
		if (max == min)
			return max;
		Random rand = new Random();
		int n = rand.nextInt(max - min) + min;
		return n;
	}

	public static String toDomain(Collection<Action> allActions, String name) {
		StringBuffer predicates = new StringBuffer();
		StringBuffer actions = new StringBuffer();
		Set<String> allPredicate = new HashSet<String>();

		for (Action a : allActions) {
			actions.append(a.toPDDL() + "\n");
			allPredicate.addAll(a.getAllPredicates());
		}

		predicates.append("(:predicates\n");
		for (String p : allPredicate) {
			predicates.append("\t(" + p + ")\n");
		}
		predicates.append(")\n");

		String domain = "(define (domain " + name + ")\n" + predicates.toString() + actions.toString() + "\n)";
		return domain;
	}

	public static String toDomain(Collection<Action> allActions, String name, Set<String> additionalPredicates) {
		StringBuffer predicates = new StringBuffer();
		StringBuffer actions = new StringBuffer();
		Set<String> allPredicate = new HashSet<String>(additionalPredicates);

		for (Action a : allActions) {
			actions.append(a.toPDDL() + "\n");
			allPredicate.addAll(a.getAllPredicates());
		}

		predicates.append("(:predicates\n");
		for (String p : allPredicate) {
			predicates.append("\t(" + p + ")\n");
		}
		predicates.append(")\n");

		String domain = "(define (domain " + name + ")\n" + predicates.toString() + actions.toString() + "\n)";
		return domain;
	}

	public static String toProblem(String init, String goal) {
		String domain = "(define (problem prob1) (:domain test)\n " + "\t(:init \n \t" + init + "\n\t)\n"
				+ "\t(:goal (and \n \t" + goal + "\n\t)\n)" + "\n)";
		return domain;
	}

	public static String getAlphaNumericString(int n) {
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuilder sb = new StringBuilder(n);
		for (int i = 0; i < n; i++) {
			int index = (int) (AlphaNumericString.length() * Math.random());
			sb.append(AlphaNumericString.charAt(index));
		}
		return sb.toString();
	}

	public static Set<String> generateStringSet(int size) {
		Set<String> result = new HashSet<String>();
		while (result.size() < size) {
			String l = getAlphaNumericString(4);
			result.add(l);
		}
		return result;
	}

	public static String generateLoc() {
		return "LOC-" + Utils.getAlphaNumericString(ChainGenerator.MAX_PART_LEN);
	}

	public static Map<Set<String>, Set<Action>> cluster(Collection<Action> acts) {
		Map<Set<String>, Set<Action>> result = new HashMap<>();
		for (Action a : acts) {
			Set<Action> group = result.get(a.getPreconds());
			if (group == null)
				group = new HashSet<>();
			group.add(a);
			result.put(a.getPreconds(), group);
		}
		return result;
	}

	public static void toREP(String path, Map<Action, Collection<Action>> groupedActions, int size, String type) {
		String content = "";
		for (Action rep : groupedActions.keySet()) {
			content += rep.getName() + "----" + groupedActions.get(rep).toString() + "\n";
		}
//		System.out.println(content);
//		System.out.println("--------------------");
		write(Config.path + Config.size + "/REP_" + type + ".txt", content);
	}

	public static <T extends Object> Set<T> randomSet(Set<T> elements, int numOfElms) {
		if (numOfElms >= elements.size())
			return elements;
		Set<T> result = new HashSet<>();
		List<T> elList = new ArrayList<>(elements);
		Collections.shuffle(elList);
		while (result.size() < numOfElms) {
			int i = randomNumber(0, elements.size() - 1);
			result.add(elList.get(i));
		}
		return result;
	}

	public static boolean intersect(Set<String> s1, Set<String> s2) {
		Set<String> intersection = new HashSet<String>(s1);
		intersection.retainAll(s2);
		return !intersection.isEmpty();
	}

	public static <T> T selectAnElm(Collection<T> navs) {
		if (navs == null || navs.isEmpty())
			return null;
		for (T action : navs) {
			return action;
		}
		return null;
	}

	public static int findClosingParen(char[] text, int openPos) {
		int closePos = openPos;
		int counter = 1;
		while (counter > 0) {
			char c = text[++closePos];
			if (c == '(') {
				counter++;
			} else if (c == ')') {
				counter--;
			}
		}
		return closePos;
	}

	public static Set<String> extractPredicates(String prob) {
		String content = read(prob);
		int index = content.indexOf("(:init") + "(:init".length();
		int end = findClosingParen(content.toCharArray(), index);
		if (end > 0) {
			Set<String> result = new HashSet<>();
			String init = content.substring(index, end).trim();
			String[] lines = init.split("\n");
			for (String l : lines) {
				result.add(l.replace("(", "").replace(")", ""));
			}
			return result;
		}

		return null;
	}

	public static Collection<File> listFileTree(File dir) {
		Set<File> fileTree = new HashSet<File>();
		if (dir == null || dir.listFiles() == null) {
			return fileTree;
		}
		for (File entry : dir.listFiles()) {
			if (entry.isFile())
				fileTree.add(entry);
			else
				fileTree.addAll(listFileTree(entry));
		}
		return fileTree;
	}
}

package dataset.filtering;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import analysis.utils.Utils;

public class Filtering {
	public static void main(String[] args) {
		String pddl = "/Users/sonnguyen/Downloads/pddl";
		String bm2 = "/Users/sonnguyen/Downloads/benchmark2";
		List<String> allDomains = listOfFiles(pddl);
		List<String> consideringDomains = new ArrayList<>();
		for (String domain : allDomains) {
			if (isNumeric(domain)) {
				consideringDomains.add(domain);
			} else {
				File d = new File(domain);
				System.out.println(domain);
				deleteDir(d.getParentFile());
			}
		}
		Set<String> domainNames = new HashSet<>();
		for (String d : consideringDomains) {
			int i = d.indexOf("domains") + "domains".length() + 1;
			String name = d.substring(i, d.length()).trim();
			String[] parts = name.split("-");
			domainNames.add(parts[0]);
			System.out.println(d.replace(pddl, ""));
		}
		for (String n : domainNames) {
			for (String d : consideringDomains) {
				int i = d.indexOf("domains") + "domains".length() + 1;
				String name = d.substring(i, d.length()).trim();
				String[] parts = name.split("-");
				if (parts[0].equals(n)) {
					File domain = new File(d);
					try {
						File dest = new File(bm2+"/"+name.replace("/domain.pddl", ""));
						dest.mkdir();
						copyDirectory(domain.getParentFile().getAbsolutePath(), dest.getAbsolutePath());
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
			}
		}
	}

	private static boolean isNumeric(String domain) {
		String content = Utils.read(new File(domain));
		return content.contains(" :typing") && content.contains("(:functions ") && !content.contains(":adl")
				&& !content.contains(":derived-predicates") && !content.contains(":preferences")
				&& !content.contains(":constraints") && !content.contains("(*") && !content.contains("(/");

	}

	public static List<String> listOfFiles(String path) {
		try (Stream<Path> walk = Files.walk(Paths.get(path))) {
			List<String> result = walk.map(x -> x.toString()).filter(f -> f.endsWith("domain.pddl"))
					.collect(Collectors.toList());
			return result;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	static void deleteDir(File file) {
		File[] contents = file.listFiles();
		if (contents != null) {
			for (File f : contents) {
				if (!Files.isSymbolicLink(f.toPath())) {
					deleteDir(f);
				}
			}
		}
		file.delete();
	}

	public static void copyDirectory(String sourceDirectoryLocation, String destinationDirectoryLocation)
			throws IOException {
		Files.walk(Paths.get(sourceDirectoryLocation)).forEach(source -> {
			Path destination = Paths.get(destinationDirectoryLocation,
					source.toString().substring(sourceDirectoryLocation.length()));
			try {
				Files.copy(source, destination);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
}

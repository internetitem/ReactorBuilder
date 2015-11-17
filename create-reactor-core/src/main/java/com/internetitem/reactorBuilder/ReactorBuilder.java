package com.internetitem.reactorBuilder;

import com.internetitem.reactorBuilder.config.Configuration;
import nu.xom.Document;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ReactorBuilder {

	private static final String DEFAULT_POM = "/default-pom.xml";

	private Inputter inputter;
	private Outputter outputter;
	private ModuleLister lister;

	public ReactorBuilder(Inputter inputter, Outputter outputter, ModuleLister lister) {
		this.inputter = inputter;
		this.outputter = outputter;
		this.lister = lister;
	}

	public void buildReactorProject(Configuration config) throws IOException {
		List<String> modules = getModules(config);
		modules.addAll(0, config.getPrependModules());
		modules.addAll(config.getAppendModules());

		Document doc = loadPom(config);
		PomFile.transformPom(modules, config, doc);
		writePom(doc, config);
	}

	void writePom(Document doc, Configuration config) throws IOException {
		String outputFile = config.getOutputFile();
		String xml = XmlUtility.xmlFromDocument(doc);
		if (outputFile != null) {
			outputter.writeOutput(outputFile, xml);
		} else {
			System.out.println(xml);
		}
	}

	Document loadPom(Configuration config) throws IOException {
		String pomFile = config.getTemplateFile();
		String text;
		if (pomFile == null) {
			text = loadDefaultPom();
		} else {
			text = inputter.readInput(pomFile);
		}
		return XmlUtility.documentFromXml(text);
	}

	String loadDefaultPom() throws IOException {
		return FileUtility.loadResource(DEFAULT_POM);
	}

	List<String> getModules(Configuration config) throws IOException {
		List<String> rawModuleDirectories = getModuleSearchDirectories(config);
		Path relativeTo = getRelativeToPath(config);

		List<String> allModules = new ArrayList<>();
		for (String moduleDirectory : rawModuleDirectories) {
			for (String directory : lister.listModules(moduleDirectory)) {
				allModules.add(relativizePath(relativeTo, directory));
			}
		}
		return allModules;
	}

	public static Path getRelativeToPath(Configuration config) {
		String relativeToString = config.getRelativeTo();
		if (relativeToString != null) {
			return getAbsolutePath(relativeToString);
		}
		return null;
	}

	public static Path getAbsolutePath(String name) {
		return Paths.get(name).toAbsolutePath();
	}

	public static String relativizePath(Path relativeTo, String filename) {
		if (relativeTo == null) {
			return filename;
		}

		Path path = getAbsolutePath(filename);
		if (path.startsWith(relativeTo)) {
			return relativeTo.relativize(path).toString();
		} else {
			return path.toString();
		}
	}

	public static List<String> getModuleSearchDirectories(Configuration config) {
		List<String> moduleDirectories = new ArrayList<>(config.getModuleSearchDirectories());
		if (moduleDirectories.isEmpty()) {
			moduleDirectories.add(".");
		}
		return moduleDirectories;
	}

}

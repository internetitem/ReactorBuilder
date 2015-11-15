package com.internetitem.reactorBuilder;

import com.internetitem.reactorBuilder.config.Configuration;
import nu.xom.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
		transformDocument(modules, config, doc);
		writePom(doc, config);
	}

	void transformDocument(List<String> modules, Configuration config, Document doc) {
		String namespace = config.getXmlns();

		Element root = doc.getRootElement();
		maybeAddElement(root, namespace, "artifactId", config.getArtifactId());
		maybeAddElement(root, namespace, "groupId", config.getGroupId());
		maybeAddElement(root, namespace, "version", config.getVersion());
		maybeAddElement(root, namespace, "packaging", config.getPackaging());

		appendModules(root, namespace, modules);
	}

	void appendModules(Element root, String namespace, List<String> modules) {
		Element modulesElement = root.getFirstChildElement("modules", namespace);
		if (modulesElement == null) {
			modulesElement = new Element("modules", namespace);
			root.appendChild(modulesElement);
		}

		for (String module : modules) {
			Element moduleElement = new Element("module", namespace);
			moduleElement.appendChild(module);
			modulesElement.appendChild(moduleElement);
		}
	}

	void maybeAddElement(Element e, String namespace, String name, String value) {
		if (value == null) {
			return;
		}

		Element child = e.getFirstChildElement(name, namespace);
		if (child != null) {
			child.removeChildren();
		} else {
			child = new Element(name, namespace);
			e.appendChild(child);
		}
		child.appendChild(value);
	}

	void writePom(Document doc, Configuration config) throws IOException {
		String outputFile = config.getOutputFile();
		String xml = formatXml(doc);
		if (outputFile != null) {
			outputter.writeOutput(outputFile, xml);
		} else {
			System.out.println(xml);
		}
	}

	String formatXml(Document doc) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Serializer serializer = new Serializer(baos, "UTF-8");
		serializer.setIndent(4);
		serializer.write(doc);
		return baos.toString("UTF-8");
	}

	Document loadPom(Configuration config) throws IOException {
		String pomFile = config.getTemplateFile();
		String text;
		if (pomFile == null) {
			text = loadDefaultPom();
		} else {
			text = inputter.readInput(pomFile);
		}
		return parseXml(text);
	}

	Document parseXml(String text) throws IOException {
		Builder parser = new Builder();
		try {
			return parser.build(text, null);
		} catch (ParsingException e) {
			throw new IOException("Unable to parse XML: " + e.getMessage(), e);
		}
	}

	String loadDefaultPom() throws IOException {
		return loadResource(DEFAULT_POM);
	}

	String loadResource(String resourceName) throws IOException {
		try (InputStream istream = getClass().getResourceAsStream(resourceName)) {
			if (istream == null) {
				throw new IOException("Unable to load resource " + resourceName);
			}
			try (InputStreamReader reader = new InputStreamReader(istream, "UTF-8")) {
				return FileInputter.slurp(reader);
			}
		}
	}

	List<String> getModules(Configuration config) throws IOException {
		List<String> moduleDirectories = config.getModuleDirectories();
		List<String> allModules = new ArrayList<>();
		if (moduleDirectories == null || moduleDirectories.isEmpty()) {
			return lister.listModules(".");
		}
		for (String moduleDirectory : moduleDirectories) {
			allModules.addAll(lister.listModules(moduleDirectory));
		}
		return allModules;
	}

}

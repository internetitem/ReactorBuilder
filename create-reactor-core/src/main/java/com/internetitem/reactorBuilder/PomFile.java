package com.internetitem.reactorBuilder;

import com.internetitem.reactorBuilder.config.Configuration;
import nu.xom.Document;
import nu.xom.Element;

import java.util.List;

public class PomFile {

	public static void transformPom(List<String> modules, Configuration config, Document doc) {
		String namespace = config.getXmlns();

		Element root = doc.getRootElement();
		maybeAddElement(root, namespace, "artifactId", config.getArtifactId());
		maybeAddElement(root, namespace, "groupId", config.getGroupId());
		maybeAddElement(root, namespace, "version", config.getVersion());
		maybeAddElement(root, namespace, "packaging", config.getPackaging());

		appendModules(root, namespace, modules);
	}

	public static void appendModules(Element root, String namespace, List<String> modules) {
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

	public static void maybeAddElement(Element e, String namespace, String name, String value) {
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

}

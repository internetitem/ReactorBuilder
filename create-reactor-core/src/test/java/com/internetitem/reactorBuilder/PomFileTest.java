package com.internetitem.reactorBuilder;

import com.internetitem.reactorBuilder.config.Configuration;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.internetitem.reactorBuilder.XmlTestUtility.assertElementText;
import static com.internetitem.reactorBuilder.XmlTestUtility.assertElementsText;
import static org.junit.Assert.*;

public class PomFileTest {

	private static final String TEST_NAMESPACE = "http://myns/";

	@Test
	public void testAddNewElement() throws Exception {
		Element e = new Element("root", TEST_NAMESPACE);
		PomFile.maybeAddElement(e, TEST_NAMESPACE, "key", "val");
		Element child = e.getFirstChildElement("key", TEST_NAMESPACE);
		assertNotNull(child);
		assertEquals("val", child.getValue());
	}

	@Test
	public void testDontAddNewElement() throws Exception {
		Element e = new Element("root", TEST_NAMESPACE);
		PomFile.maybeAddElement(e, TEST_NAMESPACE, "key", null);
		Element child = e.getFirstChildElement("key", TEST_NAMESPACE);
		assertNull(child);
	}

	@Test
	public void testEditElement() throws Exception {
		Element e = new Element("root", TEST_NAMESPACE);
		Element c = new Element("key", TEST_NAMESPACE);
		c.appendChild("yes");
		e.appendChild(c);
		PomFile.maybeAddElement(e, TEST_NAMESPACE, "key", "val");
		Elements es = e.getChildElements("key", TEST_NAMESPACE);
		assertEquals(1, es.size());
		Element child = es.get(0);
		assertNotNull(child);
		assertEquals("val", child.getValue());
	}


	@Test
	public void testAppendModulesExistingElement() throws Exception {
		Element root = new Element("root", TEST_NAMESPACE);
		Element modulesElement = new Element("modules", TEST_NAMESPACE);
		root.appendChild(modulesElement);

		List<String> modules = new ArrayList<>();
		modules.add("module1");
		modules.add("module2");
		PomFile.appendModules(root, TEST_NAMESPACE, modules);

		Elements modulesElements = modulesElement.getChildElements("module", TEST_NAMESPACE);
		assertNotNull(modulesElements);
		assertEquals(2, modulesElements.size());
		assertElementText("module1", modulesElements.get(0));
		assertElementText("module2", modulesElements.get(1));
	}

	@Test
	public void testAppendModulesNewElement() throws Exception {
		Element root = new Element("root", TEST_NAMESPACE);

		List<String> modules = new ArrayList<>();
		modules.add("module1");
		modules.add("module2");
		PomFile.appendModules(root, TEST_NAMESPACE, modules);

		Element modulesElement = root.getFirstChildElement("modules", TEST_NAMESPACE);
		assertNotNull(modulesElement);
		Elements modulesElements = modulesElement.getChildElements("module", TEST_NAMESPACE);
		assertNotNull(modulesElements);
		assertEquals(2, modulesElements.size());
		assertElementText("module1", modulesElements.get(0));
		assertElementText("module2", modulesElements.get(1));
	}


	@Test
	public void testTransformBareDocument() throws Exception {
		List<String> modules = new ArrayList<>();
		modules.add("module1");
		modules.add("module2");

		Configuration config = new Configuration();
		config.setVersion("v1.0.0");
		config.setArtifactId("my artifact");
		config.setGroupId("my groupId");
		config.setPackaging("my packaging");
		config.setXmlns(TEST_NAMESPACE);

		Element root = new Element("pomroot", TEST_NAMESPACE);
		Document doc = new Document(root);

		PomFile.transformPom(modules, config, doc);

		assertElementText("v1.0.0", root.getFirstChildElement("version", TEST_NAMESPACE));
		assertElementText("my artifact", root.getFirstChildElement("artifactId", TEST_NAMESPACE));
		assertElementText("my groupId", root.getFirstChildElement("groupId", TEST_NAMESPACE));
		assertElementText("my packaging", root.getFirstChildElement("packaging", TEST_NAMESPACE));
	}

	@Test
	public void testTransformExistingDocument() throws Exception {
		List<String> modules = new ArrayList<>();
		modules.add("module1");
		modules.add("module2");

		Configuration config = new Configuration();
		config.setVersion("v1.0.0");
		config.setArtifactId("my artifact");
		config.setGroupId("my groupId");
		config.setPackaging("my packaging");

		String xml = FileUtility.loadResource("/test-pom.xml");
		Document doc = XmlUtility.documentFromXml(xml);

		PomFile.transformPom(modules, config, doc);

		Element root = doc.getRootElement();

		String xmlns = config.getXmlns();
		assertElementsText(root.getChildElements("version", xmlns), "v1.0.0");
		assertElementsText(root.getChildElements("artifactId", xmlns), "my artifact");
		assertElementsText(root.getChildElements("groupId", xmlns), "my groupId");
		assertElementsText(root.getChildElements("packaging", xmlns), "my packaging");
	}
}

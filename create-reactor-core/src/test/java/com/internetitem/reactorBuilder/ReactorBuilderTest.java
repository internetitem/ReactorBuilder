package com.internetitem.reactorBuilder;

import com.internetitem.reactorBuilder.config.Configuration;
import com.internetitem.reactorBuilder.config.mocks.MockInputter;
import com.internetitem.reactorBuilder.config.mocks.MockModuleLister;
import com.internetitem.reactorBuilder.config.mocks.MockOutputter;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ReactorBuilderTest {

	private static final String TEST_NAMESPACE = "http://myns/";

	@Test
	public void testBuildReactorProject() throws Exception {
		MockInputter mockInputter = new MockInputter();
		MockOutputter mockOutputter = new MockOutputter();
		MockModuleLister mockLister = new MockModuleLister();
		ReactorBuilder builder = new ReactorBuilder(mockInputter, mockOutputter, mockLister);

		Configuration config = new Configuration();
		config.setVersion("v1.0.0");
		config.setArtifactId("my artifact");
		config.setGroupId("my groupId");
		config.setPackaging("my packaging");
		config.setTemplateFile("/dir/pom.xml");
		String xml = builder.loadResource("/test-pom.xml");
		mockInputter.addData("/dir/pom.xml", xml);
		mockLister.addDirectory(".", "module1", "module2");
		config.setOutputFile("/out/pom.xml");

		builder.buildReactorProject(config);
		String output = mockOutputter.getData("/out/pom.xml");
		assertNotNull(output);
		Document doc = builder.parseXml(output);
		Element root = doc.getRootElement();
		assertNotNull(root);

		String xmlns = config.getXmlns();

		assertElementText("v1.0.0", root.getFirstChildElement("version", xmlns));
		assertElementText("my artifact", root.getFirstChildElement("artifactId", xmlns));
		assertElementText("my groupId", root.getFirstChildElement("groupId", xmlns));
		assertElementText("my packaging", root.getFirstChildElement("packaging", xmlns));

		Element modulesElement = root.getFirstChildElement("modules", xmlns);
		assertNotNull(modulesElement);

		Elements es = modulesElement.getChildElements("module", xmlns);
		assertElementsText(es, "module1", "module2");
	}

	@Test
	public void testBuildReactorProjectWithRelativePaths() throws Exception {
		MockInputter mockInputter = new MockInputter();
		MockOutputter mockOutputter = new MockOutputter();
		MockModuleLister mockLister = new MockModuleLister();
		ReactorBuilder builder = new ReactorBuilder(mockInputter, mockOutputter, mockLister);

		Configuration config = new Configuration();
		config.setVersion("v1.0.0");
		config.setArtifactId("my artifact");
		config.setGroupId("my groupId");
		config.setPackaging("my packaging");
		config.setTemplateFile("/dir/pom.xml");
		String xml = builder.loadResource("/test-pom.xml");
		mockInputter.addData("/dir/pom.xml", xml);

		config.setRelativeTo("/path1");
		mockLister.addDirectory("/path1", "/path1/module1", "/path1/module2");
		mockLister.addDirectory("/path2", "/path2/module3", "/path2/module4");


		List<String> searchDirectories = new ArrayList<>();
		searchDirectories.add("/path1");
		searchDirectories.add("/path2");
		config.setModuleSearchDirectories(searchDirectories);

		config.setOutputFile("/out/pom.xml");

		builder.buildReactorProject(config);
		String output = mockOutputter.getData("/out/pom.xml");
		assertNotNull(output);
		Document doc = builder.parseXml(output);
		Element root = doc.getRootElement();
		assertNotNull(root);

		String xmlns = config.getXmlns();

		assertElementText("v1.0.0", root.getFirstChildElement("version", xmlns));
		assertElementText("my artifact", root.getFirstChildElement("artifactId", xmlns));
		assertElementText("my groupId", root.getFirstChildElement("groupId", xmlns));
		assertElementText("my packaging", root.getFirstChildElement("packaging", xmlns));

		Element modulesElement = root.getFirstChildElement("modules", xmlns);
		assertNotNull(modulesElement);

		Elements es = modulesElement.getChildElements("module", xmlns);
		assertElementsText(es, "module1", "module2", "/path2/module3", "/path2/module4");
	}

	@Test
	public void testTransformBareDocument() throws Exception {
		MockInputter mockInputter = new MockInputter();
		MockOutputter mockOutputter = new MockOutputter();
		MockModuleLister mockLister = new MockModuleLister();
		ReactorBuilder builder = new ReactorBuilder(mockInputter, mockOutputter, mockLister);

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

		builder.transformDocument(modules, config, doc);

		assertElementText("v1.0.0", root.getFirstChildElement("version", TEST_NAMESPACE));
		assertElementText("my artifact", root.getFirstChildElement("artifactId", TEST_NAMESPACE));
		assertElementText("my groupId", root.getFirstChildElement("groupId", TEST_NAMESPACE));
		assertElementText("my packaging", root.getFirstChildElement("packaging", TEST_NAMESPACE));
	}

	@Test
	public void testTransformExistingDocument() throws Exception {
		MockInputter mockInputter = new MockInputter();
		MockOutputter mockOutputter = new MockOutputter();
		MockModuleLister mockLister = new MockModuleLister();
		ReactorBuilder builder = new ReactorBuilder(mockInputter, mockOutputter, mockLister);

		List<String> modules = new ArrayList<>();
		modules.add("module1");
		modules.add("module2");

		Configuration config = new Configuration();
		config.setVersion("v1.0.0");
		config.setArtifactId("my artifact");
		config.setGroupId("my groupId");
		config.setPackaging("my packaging");

		String xml = builder.loadResource("/test-pom.xml");
		Document doc = builder.parseXml(xml);

		builder.transformDocument(modules, config, doc);

		Element root = doc.getRootElement();

		String xmlns = config.getXmlns();
		assertElementsText(root.getChildElements("version", xmlns), "v1.0.0");
		assertElementsText(root.getChildElements("artifactId", xmlns), "my artifact");
		assertElementsText(root.getChildElements("groupId", xmlns), "my groupId");
		assertElementsText(root.getChildElements("packaging", xmlns), "my packaging");
	}


	@Test
	public void testAppendModulesExistingElement() throws Exception {
		MockInputter mockInputter = new MockInputter();
		MockOutputter mockOutputter = new MockOutputter();
		MockModuleLister mockLister = new MockModuleLister();
		ReactorBuilder builder = new ReactorBuilder(mockInputter, mockOutputter, mockLister);
		Element root = new Element("root", TEST_NAMESPACE);
		Element modulesElement = new Element("modules", TEST_NAMESPACE);
		root.appendChild(modulesElement);

		List<String> modules = new ArrayList<>();
		modules.add("module1");
		modules.add("module2");
		builder.appendModules(root, TEST_NAMESPACE, modules);

		Elements modulesElements = modulesElement.getChildElements("module", TEST_NAMESPACE);
		assertNotNull(modulesElements);
		assertEquals(2, modulesElements.size());
		assertElementText("module1", modulesElements.get(0));
		assertElementText("module2", modulesElements.get(1));
	}

	@Test
	public void testAppendModulesNewElement() throws Exception {
		MockInputter mockInputter = new MockInputter();
		MockOutputter mockOutputter = new MockOutputter();
		MockModuleLister mockLister = new MockModuleLister();
		ReactorBuilder builder = new ReactorBuilder(mockInputter, mockOutputter, mockLister);
		Element root = new Element("root", TEST_NAMESPACE);

		List<String> modules = new ArrayList<>();
		modules.add("module1");
		modules.add("module2");
		builder.appendModules(root, TEST_NAMESPACE, modules);

		Element modulesElement = root.getFirstChildElement("modules", TEST_NAMESPACE);
		assertNotNull(modulesElement);
		Elements modulesElements = modulesElement.getChildElements("module", TEST_NAMESPACE);
		assertNotNull(modulesElements);
		assertEquals(2, modulesElements.size());
		assertElementText("module1", modulesElements.get(0));
		assertElementText("module2", modulesElements.get(1));
	}

	@Test
	public void testAddNewElement() throws Exception {
		MockInputter mockInputter = new MockInputter();
		MockOutputter mockOutputter = new MockOutputter();
		MockModuleLister mockLister = new MockModuleLister();
		ReactorBuilder builder = new ReactorBuilder(mockInputter, mockOutputter, mockLister);
		Element e = new Element("root", TEST_NAMESPACE);
		builder.maybeAddElement(e, TEST_NAMESPACE, "key", "val");
		Element child = e.getFirstChildElement("key", TEST_NAMESPACE);
		assertNotNull(child);
		assertEquals("val", child.getValue());
	}

	@Test
	public void testDontAddNewElement() throws Exception {
		MockInputter mockInputter = new MockInputter();
		MockOutputter mockOutputter = new MockOutputter();
		MockModuleLister mockLister = new MockModuleLister();
		ReactorBuilder builder = new ReactorBuilder(mockInputter, mockOutputter, mockLister);
		Element e = new Element("root", TEST_NAMESPACE);
		builder.maybeAddElement(e, TEST_NAMESPACE, "key", null);
		Element child = e.getFirstChildElement("key", TEST_NAMESPACE);
		assertNull(child);
	}

	@Test
	public void testEditElement() throws Exception {
		MockInputter mockInputter = new MockInputter();
		MockOutputter mockOutputter = new MockOutputter();
		MockModuleLister mockLister = new MockModuleLister();
		ReactorBuilder builder = new ReactorBuilder(mockInputter, mockOutputter, mockLister);
		Element e = new Element("root", TEST_NAMESPACE);
		Element c = new Element("key", TEST_NAMESPACE);
		c.appendChild("yes");
		e.appendChild(c);
		builder.maybeAddElement(e, TEST_NAMESPACE, "key", "val");
		Elements es = e.getChildElements("key", TEST_NAMESPACE);
		assertEquals(1, es.size());
		Element child = es.get(0);
		assertNotNull(child);
		assertEquals("val", child.getValue());
	}

	@Test
	public void testWritePom() throws Exception {
		MockInputter mockInputter = new MockInputter();
		MockOutputter mockOutputter = new MockOutputter();
		MockModuleLister mockLister = new MockModuleLister();
		ReactorBuilder builder = new ReactorBuilder(mockInputter, mockOutputter, mockLister);
		Configuration config = new Configuration();
		config.setOutputFile("outPom.xml");
		Element root = new Element("parent");
		Element child = new Element("child");
		child.appendChild("value");
		root.appendChild(child);
		Document doc = new Document(root);
		builder.writePom(doc, config);
		String xml = mockOutputter.getData("outPom.xml");
		assertTrue(xml.contains("<child>value</child>"));
	}

	@Test
	public void testFormatXml() throws Exception {
		MockInputter mockInputter = new MockInputter();
		MockOutputter mockOutputter = new MockOutputter();
		MockModuleLister mockLister = new MockModuleLister();
		ReactorBuilder builder = new ReactorBuilder(mockInputter, mockOutputter, mockLister);
		Element root = new Element("parent");
		Element child = new Element("child");
		child.appendChild("value");
		root.appendChild(child);
		Document doc = new Document(root);
		String xml = builder.formatXml(doc);
		assertTrue(xml.contains("    "));
	}

	@Test
	public void testLoadPom() throws Exception {
		MockInputter mockInputter = new MockInputter();
		mockInputter.addData("pom.xml", "<pomFile></pomFile>");
		MockOutputter mockOutputter = new MockOutputter();
		MockModuleLister mockLister = new MockModuleLister();
		ReactorBuilder builder = new ReactorBuilder(mockInputter, mockOutputter, mockLister);
		Configuration config = new Configuration();
		config.setTemplateFile("pom.xml");
		Document document = builder.loadPom(config);
		assertNotNull(document);
		Element root = document.getRootElement();
		assertNotNull(root);
		assertEquals("pomFile", root.getLocalName());
	}

	@Test
	public void testParseXml() throws Exception {
		MockInputter mockInputter = new MockInputter();
		MockOutputter mockOutputter = new MockOutputter();
		MockModuleLister mockLister = new MockModuleLister();
		ReactorBuilder builder = new ReactorBuilder(mockInputter, mockOutputter, mockLister);
		Document document = builder.parseXml("<parent><child>value</child></parent>");
		assertNotNull(document);
		Element root = document.getRootElement();
		assertNotNull(root);
		assertEquals("parent", root.getLocalName());
		Element child = root.getFirstChildElement("child");
		assertNotNull(child);
		String text = child.getValue();
		assertEquals("value", text);
	}

	@Test
	public void testLoadDefaultPom() throws Exception {
		MockInputter mockInputter = new MockInputter();
		MockOutputter mockOutputter = new MockOutputter();
		MockModuleLister mockLister = new MockModuleLister();
		ReactorBuilder builder = new ReactorBuilder(mockInputter, mockOutputter, mockLister);
		String defaultPom = builder.loadDefaultPom();
		assertNotNull(defaultPom);
		assertTrue(defaultPom.contains("<"));
	}

	@Test
	public void testGetModulesDefaultDirectory() throws Exception {
		MockInputter mockInputter = new MockInputter();
		MockOutputter mockOutputter = new MockOutputter();
		MockModuleLister mockLister = new MockModuleLister();
		mockLister.addDirectory(".", "module1", "module2");
		ReactorBuilder builder = new ReactorBuilder(mockInputter, mockOutputter, mockLister);
		Configuration config = new Configuration();
		List<String> modules = builder.getModules(config);
		assertNotNull(modules);
		assertEquals(2, modules.size());
		assertEquals("module1", modules.get(0));
		assertEquals("module2", modules.get(1));
	}

	@Test
	public void testGetModulesMultipleDirectories() throws Exception {
		MockInputter mockInputter = new MockInputter();
		MockOutputter mockOutputter = new MockOutputter();
		MockModuleLister mockLister = new MockModuleLister();
		mockLister.addDirectory("dir1", "module1", "module2");
		mockLister.addDirectory("dir2", "module3", "module4");
		ReactorBuilder builder = new ReactorBuilder(mockInputter, mockOutputter, mockLister);
		Configuration config = new Configuration();
		List<String> dirs = new ArrayList<>();
		dirs.add("dir1");
		dirs.add("dir2");
		config.setModuleSearchDirectories(dirs);
		List<String> modules = builder.getModules(config);
		assertNotNull(modules);
		assertEquals(4, modules.size());
		assertEquals("module1", modules.get(0));
		assertEquals("module2", modules.get(1));
		assertEquals("module3", modules.get(2));
		assertEquals("module4", modules.get(3));
	}

	@Test
	public void testGetModuleSearchDirectories() throws Exception {
		Configuration config = new Configuration();
		List<String> dirs = new ArrayList<>();
		dirs.add("dir1");
		dirs.add("dir2");
		config.setModuleSearchDirectories(dirs);
		List<String> moduleDirectories = ReactorBuilder.getModuleSearchDirectories(config);
		assertNotNull(moduleDirectories);
		assertEquals(2, moduleDirectories.size());
		assertEquals("dir1", moduleDirectories.get(0));
		assertEquals("dir2", moduleDirectories.get(1));
	}

	@Test
	public void testGetModuleSearchNoDirectories() throws Exception {
		Configuration config = new Configuration();
		List<String> moduleDirectories = ReactorBuilder.getModuleSearchDirectories(config);
		assertNotNull(moduleDirectories);
		assertEquals(1, moduleDirectories.size());
		assertEquals(".", moduleDirectories.get(0));
	}

	@Test
	public void testGetModuleSearchEmptyDirectories() throws Exception {
		Configuration config = new Configuration();
		List<String> dirs = new ArrayList<>();
		config.setModuleSearchDirectories(dirs);
		List<String> moduleDirectories = ReactorBuilder.getModuleSearchDirectories(config);
		assertNotNull(moduleDirectories);
		assertEquals(1, moduleDirectories.size());
		assertEquals(".", moduleDirectories.get(0));
	}

	@Test
	public void testRelativePath() throws Exception {
		Path relativeTo = Paths.get("/file1");
		String relativePath = ReactorBuilder.relativizePath(relativeTo, "/file1/file2");
		assertEquals("file2", relativePath);
	}

	@Test
	public void testDontRelativePath() throws Exception {
		Path relativeTo = Paths.get("/file1");
		String relativePath = ReactorBuilder.relativizePath(relativeTo, "/file3/file2");
		assertEquals("/file3/file2", relativePath);
	}

	private void assertElementText(String expectedValue, Element e) {
		assertNotNull(e);
		String actualValue = e.getValue();
		assertEquals(expectedValue, actualValue);
	}

	private void assertElementsText(Elements es, String...expectedValues) {
		assertNotNull(es);
		assertEquals(expectedValues.length, es.size());
		for (int i = 0; i < expectedValues.length; i++) {
			String expected = expectedValues[i];
			Element e = es.get(i);
			assertElementText(expected, e);
		}
	}

}

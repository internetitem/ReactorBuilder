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
import java.util.ArrayList;
import java.util.List;

import static com.internetitem.reactorBuilder.XmlTestUtility.assertElementText;
import static com.internetitem.reactorBuilder.XmlTestUtility.assertElementsText;
import static org.junit.Assert.*;

public class ReactorBuilderTest {

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
		String xml = FileUtility.loadResource("/test-pom.xml");
		mockInputter.addData("/dir/pom.xml", xml);
		mockLister.addDirectory(".", "module1", "module2");
		config.setOutputFile("/out/pom.xml");

		builder.buildReactorProject(config);
		String output = mockOutputter.getData("/out/pom.xml");
		assertNotNull(output);
		Document doc = XmlUtility.documentFromXml(output);
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
		String xml = FileUtility.loadResource("/test-pom.xml");
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
		Document doc = XmlUtility.documentFromXml(output);
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
		assertElementsText(es, "module1", "module2", ReactorBuilder.getAbsolutePath("/path2/module3").toString(), ReactorBuilder.getAbsolutePath("/path2/module4").toString());
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
		Path relativeTo = ReactorBuilder.getAbsolutePath("/file1");
		String relativePath = ReactorBuilder.relativizePath(relativeTo, "/file1/file2");
		assertEquals("file2", relativePath);
	}

	@Test
	public void testDontRelativePath() throws Exception {
		Path relativeTo = ReactorBuilder.getAbsolutePath("/file1");
		String relativePath = ReactorBuilder.relativizePath(relativeTo, "/file3/file2");
		Path actualPath = ReactorBuilder.getAbsolutePath("/file3/file2");
		assertEquals(actualPath.toString(), relativePath.toString());
	}

}

package com.internetitem.reactorBuilder.config;

import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static com.internetitem.reactorBuilder.config.TestUtility.defaultLogger;

public class ConfigurationTest {

	@Test
	public void testBasicConfiguration() {
		KeyedOptions options = new KeyedOptions();
		options.addValue("groupId", "group");
		options.addValue("artifactId", "test");
		options.addValue("version", "1.0.0");
		Configuration config = new Configuration(defaultLogger(), options);

		assertEquals("http://maven.apache.org/POM/4.0.0", config.getXmlns());
		assertEquals("group", config.getGroupId());
		assertEquals("test", config.getArtifactId());
		assertEquals("1.0.0", config.getVersion());
		assertEquals("pom", config.getPackaging());

		List<String> prependModules = config.getPrependModules();
		assertNotNull(prependModules);
		assertEquals(0, prependModules.size());

		List<String> appendModules = config.getAppendModules();
		assertNotNull(appendModules);
		assertEquals(0, appendModules.size());
	}

	@Test
	public void testFullConfiguration() {
		KeyedOptions options = new KeyedOptions();
		options.addValue("xmlns", "XML Rules!");
		options.addValue("templateFile", "tf");
		options.addValue("templateUrl", "tu");
		options.addValue("groupId", "gi");
		options.addValue("artifactId", "ai");
		options.addValue("version", "ver");
		options.addValue("packaging", "pkg");
		options.addValue("prependModule", "pm");
		options.addValue("appendModule", "am");
		options.addValue("moduleSearchDirectory", "md");
		options.addValue("relativeTo", "rt");
		options.addValue("outputFile", "of");
		Configuration config = new Configuration(defaultLogger(), options);

		assertEquals("XML Rules!", config.getXmlns());
		assertEquals("tf", config.getTemplateFile());
		assertEquals("gi", config.getGroupId());
		assertEquals("ai", config.getArtifactId());
		assertEquals("ver", config.getVersion());
		assertEquals("pkg", config.getPackaging());

		List<String> prependModules = config.getPrependModules();
		assertNotNull(prependModules);
		assertEquals(1, prependModules.size());
		assertEquals("pm", prependModules.get(0));

		List<String> appendModules = config.getAppendModules();
		assertNotNull(appendModules);
		assertEquals(1, appendModules.size());
		assertEquals("am", appendModules.get(0));

		List<String> moduleDirectories = config.getModuleSearchDirectories();
		assertNotNull(moduleDirectories);
		assertEquals(1, moduleDirectories.size());
		assertEquals("md", moduleDirectories.get(0));

		assertEquals("rt", config.getRelativeTo());
		assertEquals("of", config.getOutputFile());
	}

	@Test
	public void testConfigurationWithMultipleOptions() {
		KeyedOptions options = new KeyedOptions();
		options.addValue("groupId", "gi");
		options.addValue("artifactId", "ai");
		options.addValue("version", "ver");
		options.addValue("prependModule", "pm1");
		options.addValue("prependModule", "pm2");
		options.addValue("prependModule", "pm3");
		options.addValue("appendModule", "am1");
		options.addValue("appendModule", "am2");
		options.addValue("appendModule", "am3");
		options.addValue("moduleSearchDirectory", "md1");
		options.addValue("moduleSearchDirectory", "md2");
		options.addValue("moduleSearchDirectory", "md3");
		Configuration config = new Configuration(defaultLogger(), options);

		List<String> prependModules = config.getPrependModules();
		assertNotNull(prependModules);
		assertEquals(3, prependModules.size());
		assertEquals("pm1", prependModules.get(0));
		assertEquals("pm2", prependModules.get(1));
		assertEquals("pm3", prependModules.get(2));

		List<String> appendModules = config.getAppendModules();
		assertNotNull(appendModules);
		assertEquals(3, appendModules.size());
		assertEquals("am1", appendModules.get(0));
		assertEquals("am2", appendModules.get(1));
		assertEquals("am3", appendModules.get(2));

		List<String> moduleDirectories = config.getModuleSearchDirectories();
		assertNotNull(moduleDirectories);
		assertEquals(3, moduleDirectories.size());
		assertEquals("md1", moduleDirectories.get(0));
		assertEquals("md2", moduleDirectories.get(1));
		assertEquals("md3", moduleDirectories.get(2));
	}

	@Test
	public void testSummarizeEmptyConfiguration() {
		MockLogger mockLogger = new MockLogger();
		Configuration config = new Configuration(mockLogger);
		config.summarizeConfiguration();
		assertEquals(4, mockLogger.getMessages().size());
	}

	@Test
	public void testSummarizeNormalConfiguration() {
		MockLogger mockLogger = new MockLogger();
		Configuration config = new Configuration(mockLogger);
		config.setRelativeTo("rt");
		config.setModuleSearchDirectories(Collections.singletonList("searchMe"));
		config.setOutputFile("of");
		config.summarizeConfiguration();
		assertEquals(7, mockLogger.getMessages().size());
	}

}

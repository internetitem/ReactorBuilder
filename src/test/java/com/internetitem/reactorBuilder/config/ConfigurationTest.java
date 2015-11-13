package com.internetitem.reactorBuilder.config;

import com.internetitem.reactorBuilder.config.exception.ConfigurationException;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ConfigurationTest {

	@Test
	public void testBasicConfiguration() throws ConfigurationException {
		KeyedOptions options = new KeyedOptions();
		options.addValue("groupId", "group");
		options.addValue("artifactId", "test");
		options.addValue("version", "1.0.0");
		Configuration config = new Configuration(options);

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
	public void testFullConfiguration() throws ConfigurationException {
		KeyedOptions options = new KeyedOptions();
		options.addValue("xmlns", "XML Rules!");
		options.addValue("templateFile", "tf");
		options.addValue("templateUrl", "tu");
		options.addValue("groupId", "gi");
		options.addValue("artifactId", "ai");
		options.addValue("version", "ver");
		options.addValue("packaging", "pkg");
		options.addValue("prependModules", "pm");
		options.addValue("appendModules", "am");
		options.addValue("moduleDirectories", "md");
		options.addValue("outputFile", "of");
		Configuration config = new Configuration(options);

		assertEquals("XML Rules!", config.getXmlns());
		assertEquals("tf", config.getTemplateFile());
		assertEquals("tu", config.getTemplateUrl());
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

		List<String> moduleDirectories = config.getModuleDirectories();
		assertNotNull(moduleDirectories);
		assertEquals(1, moduleDirectories.size());
		assertEquals("md", moduleDirectories.get(0));

		assertEquals("of", config.getOutputFile());
	}

	@Test
	public void testConfigurationWithMultipleOptions() throws ConfigurationException {
		KeyedOptions options = new KeyedOptions();
		options.addValue("groupId", "gi");
		options.addValue("artifactId", "ai");
		options.addValue("version", "ver");
		options.addValue("prependModules", "pm1");
		options.addValue("prependModules", "pm2");
		options.addValue("prependModules", "pm3");
		options.addValue("appendModules", "am1");
		options.addValue("appendModules", "am2");
		options.addValue("appendModules", "am3");
		options.addValue("moduleDirectories", "md1");
		options.addValue("moduleDirectories", "md2");
		options.addValue("moduleDirectories", "md3");
		Configuration config = new Configuration(options);

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

		List<String> moduleDirectories = config.getModuleDirectories();
		assertNotNull(moduleDirectories);
		assertEquals(3, moduleDirectories.size());
		assertEquals("md1", moduleDirectories.get(0));
		assertEquals("md2", moduleDirectories.get(1));
		assertEquals("md3", moduleDirectories.get(2));
	}

	@Test(expected = ConfigurationException.class)
	public void testRequiredGroupId() throws ConfigurationException {
		KeyedOptions options = new KeyedOptions();
		options.addValue("artifactId", "ai");
		options.addValue("version", "ver");
		new Configuration(options);
	}

	@Test(expected = ConfigurationException.class)
	public void testRequiredArtifactId() throws ConfigurationException {
		KeyedOptions options = new KeyedOptions();
		options.addValue("groupId", "gi");
		options.addValue("version", "ver");
		new Configuration(options);
	}

	@Test(expected = ConfigurationException.class)
	public void testRequiredVersion() throws ConfigurationException {
		KeyedOptions options = new KeyedOptions();
		options.addValue("groupId", "gi");
		options.addValue("artifactId", "ai");
		new Configuration(options);
	}

}

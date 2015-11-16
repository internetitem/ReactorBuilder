package com.internetitem.reactorBuilder;

import com.internetitem.reactorBuilder.config.Configuration;
import com.internetitem.reactorBuilder.config.mocks.MockPropertiesLoader;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommandLineRunnerTest {

	@Test
	public void testEmptyBuildConfiguration() throws Exception {
		MockPropertiesLoader mockPropertiesLoader = new MockPropertiesLoader();
		CommandLineRunner runner = new CommandLineRunner(mockPropertiesLoader);
		runner.buildConfiguration(new String[0]);
	}

	@Test
	public void testBuildConfigurationWithProperty() throws Exception {
		MockPropertiesLoader mockPropertiesLoader = new MockPropertiesLoader();
		mockPropertiesLoader.addProperty("file1.properties", "version", "v1.0.0");
		CommandLineRunner runner = new CommandLineRunner(mockPropertiesLoader);
		Configuration configuration = runner.buildConfiguration(new String[]{"--propertyFile=file1.properties"});
		assertEquals("v1.0.0", configuration.getVersion());
	}

	@Test
	public void testBuildConfigurationWithMultiplePropertyFiles() throws Exception {
		MockPropertiesLoader mockPropertiesLoader = new MockPropertiesLoader();
		mockPropertiesLoader.addProperty("file1.properties", "version", "v1.0.0");
		mockPropertiesLoader.addProperty("file2.properties", "groupId", "gid");
		CommandLineRunner runner = new CommandLineRunner(mockPropertiesLoader);
		Configuration configuration = runner.buildConfiguration(new String[]{"--propertyFile=file1.properties", "--propertyFile=file2.properties"});
		assertEquals("v1.0.0", configuration.getVersion());
		assertEquals("gid", configuration.getGroupId());
	}
}

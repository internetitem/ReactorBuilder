package com.internetitem.reactorBuilder.maven;

import com.internetitem.reactorBuilder.*;
import com.internetitem.reactorBuilder.config.Configuration;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Mojo(name = "create-reactor-project", defaultPhase = LifecyclePhase.GENERATE_RESOURCES, threadSafe = true, requiresProject = false)
public class CreateReactorProjectMojo extends AbstractMojo {

	private static final String PROPERTY_PREFIX = "create-reactor.";

	@Parameter(property = "xmlns", required = false, defaultValue = "${" + PROPERTY_PREFIX + "xmlns}")
	private String xmlns = "http://maven.apache.org/POM/4.0.0";

	@Parameter(property = "templateFile", required = false, defaultValue = "${" + PROPERTY_PREFIX + "templateFile}")
	private String templateFile;

	@Parameter(property = "groupId", required = false, defaultValue = "${" + PROPERTY_PREFIX + "groupId}")
	private String groupId;

	@Parameter(property = "artifactId", required = false, defaultValue = "${" + PROPERTY_PREFIX + "artifactId}")
	private String artifactId;

	@Parameter(property = "version", required = false, defaultValue = "${" + PROPERTY_PREFIX + "version}")
	private String version;

	@Parameter(property = "packaging", required = false, defaultValue = "${" + PROPERTY_PREFIX + "packaging}")
	private String packaging = "pom";

	@Parameter(property = "prependModules", required = false, defaultValue = "${" + PROPERTY_PREFIX + "prependModules}")
	private String prependModules;

	@Parameter(property = "appendModules", required = false, defaultValue = "${" + PROPERTY_PREFIX + "appendModules}")
	private String appendModules;

	@Parameter(property = "relativeTo", required = false, defaultValue = "${" + PROPERTY_PREFIX + "relativeTo}")
	private String relativeTo;

	@Parameter(property = "moduleSearchDirectories", required = false, defaultValue = "${" + PROPERTY_PREFIX + "moduleSearchDirectories}")
	private String moduleSearchDirectories;

	@Parameter(property = "outputFile", required = false, defaultValue = "${" + PROPERTY_PREFIX + "outputFile}")
	private String outputFile;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		Configuration reactorConfiguration = buildReactorConfiguration();

		Inputter inputter = new FileInputter();
		Outputter outputter = new FileOutputter();
		ModuleLister lister = new FilesystemModuleLister();
		ReactorBuilder builder = new ReactorBuilder(inputter, outputter, lister);
		try {
			builder.buildReactorProject(reactorConfiguration);
		} catch (IOException e) {
			throw new MojoExecutionException("Failed to generate Reactor Project: " + e.getMessage(), e);
		}
	}

	private Configuration buildReactorConfiguration() {
		Configuration config = new Configuration();

		if (xmlns != null) {
			config.setXmlns(xmlns);
		}
		if (templateFile != null) {
			config.setTemplateFile(templateFile);
		}
		if (groupId != null) {
			config.setGroupId(groupId);
		}
		if (artifactId != null) {
			config.setArtifactId(artifactId);
		}
		if (version != null) {
			config.setVersion(version);
		}
		if (packaging != null) {
			config.setPackaging(packaging);
		}
		if (prependModules != null && !prependModules.isEmpty()) {
			List<String> prependModulesList = Arrays.asList(prependModules.split(":"));
			config.setPrependModules(prependModulesList);
		}
		if (appendModules != null && !appendModules.isEmpty()) {
			List<String> appendModulesList = Arrays.asList(appendModules.split(":"));
			config.setAppendModules(appendModulesList);
		}
		if (relativeTo != null) {
			config.setRelativeTo(relativeTo);
		}
		if (moduleSearchDirectories != null && !moduleSearchDirectories.isEmpty()) {
			List<String> moduleSearchDirectoriesList = Arrays.asList(moduleSearchDirectories.split(":"));
			config.setModuleSearchDirectories(moduleSearchDirectoriesList);
		}
		if (outputFile != null) {
			config.setOutputFile(outputFile);
		}

		return config;
	}
}

package com.internetitem.reactorBuilder.config;

import com.internetitem.reactorBuilder.config.exception.ConfigurationException;

import java.util.Collections;
import java.util.List;

public class Configuration {

	private String xmlns = "http://maven.apache.org/POM/4.0.0";

	private String templateFile;
	private String templateUrl;
	private String groupId;
	private String artifactId;
	private String version;
	private String packaging = "pom";

	private List<String> prependModules;
	private List<String> appendModules;

	private List<String> moduleDirectories;

	private String outputFile;

	public Configuration(KeyedOptions options) throws ConfigurationException {
		this.xmlns = getOptionalOption(options, "xmlns", xmlns);
		this.templateFile = getOptionalOption(options, "templateFile", templateFile);
		this.templateUrl = getOptionalOption(options, "templateUrl", templateUrl);
		this.groupId = getRequiredOption(options, "groupId");
		this.artifactId = getRequiredOption(options, "artifactId");
		this.version = getRequiredOption(options, "version");
		this.packaging = getOptionalOption(options, "packaging", packaging);
		this.prependModules = getOptionList(options, "prependModules");
		this.appendModules = getOptionList(options, "appendModules");
		this.moduleDirectories = getOptionList(options, "moduleDirectories");
		this.outputFile = getOptionalOption(options, "outputFile", outputFile);
	}

	private List<String> getOptionList(KeyedOptions options, String key) {
		List<String> values = options.getValues(key);
		if (values == null) {
			return Collections.emptyList();
		}
		return values;
	}

	private String getOptionalOption(KeyedOptions options, String key, String defaultValue) {
		String value = options.getFirstValue(key);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}

	private String getRequiredOption(KeyedOptions options, String key) throws ConfigurationException {
		String value = options.getFirstValue(key);
		if (value == null) {
			throw new ConfigurationException("Required option " + key + " missing");
		}
		return value;
	}

	public String getXmlns() {
		return xmlns;
	}

	public String getTemplateFile() {
		return templateFile;
	}

	public String getTemplateUrl() {
		return templateUrl;
	}

	public String getGroupId() {
		return groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public String getVersion() {
		return version;
	}

	public String getPackaging() {
		return packaging;
	}

	public List<String> getPrependModules() {
		return prependModules;
	}

	public List<String> getAppendModules() {
		return appendModules;
	}

	public List<String> getModuleDirectories() {
		return moduleDirectories;
	}

	public String getOutputFile() {
		return outputFile;
	}
}

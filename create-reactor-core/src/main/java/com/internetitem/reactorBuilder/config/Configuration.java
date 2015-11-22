package com.internetitem.reactorBuilder.config;

import com.internetitem.reactorBuilder.LogWrapper;
import com.internetitem.reactorBuilder.StringUtility;

import java.util.Collections;
import java.util.List;

public class Configuration {

	public static final String[] LIST_OPTION_NAMES = {"prependModules", "appendModules", "moduleSearchDirectories"};
	private String xmlns = "http://maven.apache.org/POM/4.0.0";

	private String templateFile;
	private String groupId;
	private String artifactId;
	private String version;
	private String packaging = "pom";

	private List<String> prependModules = Collections.emptyList();
	private List<String> appendModules = Collections.emptyList();

	private String relativeTo;
	private List<String> moduleSearchDirectories = Collections.emptyList();

	private String outputFile;

	private LogWrapper logWrapper;

	public Configuration(LogWrapper logWrapper) {
		this.logWrapper = logWrapper;
	}

	public Configuration(LogWrapper logWrapper, KeyedOptions options) {
		this.logWrapper = logWrapper;

		this.xmlns = getOption(options, "xmlns", xmlns);
		this.templateFile = getOption(options, "templateFile", templateFile);
		this.groupId = getOption(options, "groupId", groupId);
		this.artifactId = getOption(options, "artifactId", artifactId);
		this.version = getOption(options, "version", version);
		this.packaging = getOption(options, "packaging", packaging);
		this.prependModules = getOptionList(options, "prependModule");
		this.appendModules = getOptionList(options, "appendModule");
		this.relativeTo = getOption(options, "relativeTo", relativeTo);
		this.moduleSearchDirectories = getOptionList(options, "moduleSearchDirectory");
		this.outputFile = getOption(options, "outputFile", outputFile);
	}

	private List<String> getOptionList(KeyedOptions options, String key) {
		List<String> values = options.getValues(key);
		if (values == null) {
			return Collections.emptyList();
		}
		return values;
	}

	private String getOption(KeyedOptions options, String key, String defaultValue) {
		String value = options.getFirstValue(key);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}

	public String getXmlns() {
		return xmlns;
	}

	public void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}

	public String getTemplateFile() {
		return templateFile;
	}

	public void setTemplateFile(String templateFile) {
		this.templateFile = templateFile;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPackaging() {
		return packaging;
	}

	public void setPackaging(String packaging) {
		this.packaging = packaging;
	}

	public List<String> getPrependModules() {
		return prependModules;
	}

	public void setPrependModules(List<String> prependModules) {
		this.prependModules = prependModules;
	}

	public List<String> getAppendModules() {
		return appendModules;
	}

	public void setAppendModules(List<String> appendModules) {
		this.appendModules = appendModules;
	}

	public List<String> getModuleSearchDirectories() {
		return moduleSearchDirectories;
	}

	public void setModuleSearchDirectories(List<String> moduleSearchDirectories) {
		this.moduleSearchDirectories = moduleSearchDirectories;
	}

	public String getRelativeTo() {
		return relativeTo;
	}

	public void setRelativeTo(String relativeTo) {
		this.relativeTo = relativeTo;
	}

	public String getOutputFile() {
		return outputFile;
	}

	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}

	public LogWrapper getLogWrapper() {
		return logWrapper;
	}

	public void summarizeConfiguration() {
		logWrapper.info("Reactor Builder Configuration Start");
		maybeOutput(xmlns, "xmlns");
		maybeOutput(templateFile, "templateFile");
		maybeOutput(groupId, "groupId");
		maybeOutput(artifactId, "artifactId");
		maybeOutput(version, "version");
		maybeOutput(packaging, "packaging");
		maybeOutput(prependModules, "prependModules");
		maybeOutput(appendModules, "appendModules");
		maybeOutput(relativeTo, "relativeTo");
		maybeOutput(moduleSearchDirectories, "moduleSearchDirectories");
		maybeOutput(outputFile, "outputFile");
		logWrapper.info("Reactor Builder Configuration Finish");
	}

	private void maybeOutput(String value, String name) {
		if (value == null) {
			return;
		}
		logWrapper.info(" " + name + " = " + value);
	}

	private void maybeOutput(List<String> values, String name) {
		if (values == null || values.isEmpty()) {
			return;
		}
		String valueString = StringUtility.joinValues(values, ", ");
		logWrapper.info(" " + name + " = " + valueString);
	}
}

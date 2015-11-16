package com.internetitem.reactorBuilder;

import com.internetitem.reactorBuilder.config.*;
import com.internetitem.reactorBuilder.config.exception.CommandLineParsingException;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class CommandLineRunner {

	private static final String KEY_PROPERTY_FILE = "propertyFile";

	private PropertiesLoader propertiesLoader;

	public CommandLineRunner(PropertiesLoader propertiesLoader) {
		this.propertiesLoader = propertiesLoader;
	}

	public Configuration buildConfiguration(String[] args) throws IOException, CommandLineParsingException {
		CommandLineParser parser = new CommandLineParser();
		KeyedOptions options = parser.parseCommandLine(args);

		List<String> propertyFiles = options.getValues(KEY_PROPERTY_FILE);
		if (propertyFiles != null && !propertyFiles.isEmpty()) {
			PropertiesParser propertiesParser = new PropertiesParser(Configuration.LIST_OPTION_NAMES);
			for (String name : propertyFiles) {
				Properties props = propertiesLoader.loadProperties(name);
				propertiesParser.parseProperties(options, props);
			}
		}

		return new Configuration(options);
	}

	public static void main(String[] args) throws CommandLineParsingException, IOException {
		FilePropertiesLoader loader = new FilePropertiesLoader();
		CommandLineRunner runner = new CommandLineRunner(loader);
		Inputter inputter = new FileInputter();
		Outputter outputter = new FileOutputter();
		ModuleLister lister = new FilesystemModuleLister();
		ReactorBuilder builder = new ReactorBuilder(inputter, outputter, lister);
		Configuration config = runner.buildConfiguration(args);
		builder.buildReactorProject(config);
	}

}

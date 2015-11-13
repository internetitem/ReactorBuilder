package com.internetitem.reactorBuilder;

import com.internetitem.reactorBuilder.config.*;
import com.internetitem.reactorBuilder.config.exception.CommandLineParsingException;
import com.internetitem.reactorBuilder.config.exception.ConfigurationException;

public class ReactorBuilder {

	public static void main(String[] args) throws CommandLineParsingException, ConfigurationException {
		CommandLineParser parser = new CommandLineParser();
		KeyedOptions options = parser.parseCommandLine(args);
		Configuration config = new Configuration(options);

	}
}

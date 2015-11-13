package com.internetitem.reactorBuilder.config;

import com.internetitem.reactorBuilder.config.exception.CommandLineParsingException;

public class CommandLineParser {

	public CommandLineParser() {
	}

	public KeyedOptions parseCommandLine(String[] args) throws CommandLineParsingException {
		KeyedOptions cmdline = new KeyedOptions();
		for (String arg : args) {
			String[] parts = arg.split("=", 2);
			String left = parts[0];
			if (parts.length != 2 || !left.startsWith("--")) {
				throw new CommandLineParsingException("All arguments must be of the form --key=value");
			}
			String key = left.substring(2);
			if (key.isEmpty()) {
				throw new CommandLineParsingException("Empty key");
			}
			String value = parts[1];
			cmdline.addValue(key, value);
		}
		return cmdline;
	}
}

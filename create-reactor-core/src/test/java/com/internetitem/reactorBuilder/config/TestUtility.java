package com.internetitem.reactorBuilder.config;

import com.internetitem.reactorBuilder.ConsoleLogWrapper;
import com.internetitem.reactorBuilder.LogWrapper;

public class TestUtility {
	public static Configuration emptyConfiguration() {
		LogWrapper logger = defaultLogger();
		return new Configuration(logger);
	}

	public static LogWrapper defaultLogger() {
		String value = System.getProperty("logger.enabled");
		boolean silent = true;
		if (value != null && value.equals("logger.enabled")) {
			silent = false;
		}
		return new ConsoleLogWrapper(silent);
	}
}

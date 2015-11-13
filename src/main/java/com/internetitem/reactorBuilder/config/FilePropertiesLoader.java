package com.internetitem.reactorBuilder.config;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class FilePropertiesLoader implements PropertiesLoader {

	@Override
	public Properties loadProperties(String path) throws IOException {
		Properties props = new Properties();
		try (FileReader reader = new FileReader(path)) {
			props.load(reader);
		}
		return props;
	}

}

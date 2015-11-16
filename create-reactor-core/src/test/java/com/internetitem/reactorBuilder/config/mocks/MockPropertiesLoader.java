package com.internetitem.reactorBuilder.config.mocks;

import com.internetitem.reactorBuilder.config.PropertiesLoader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class MockPropertiesLoader implements PropertiesLoader {
	private Map<String, Properties> propertiesMap;

	public MockPropertiesLoader() {
		this.propertiesMap = new HashMap<>();
	}

	public void addProperty(String filename, String key, String value) {
		Properties props = propertiesMap.get(filename);
		if (props == null) {
			props = new Properties();
			propertiesMap.put(filename, props);
		}
		props.put(key, value);
	}

	@Override
	public Properties loadProperties(String path) throws IOException {
		Properties props = propertiesMap.get(path);
		if (props == null) {
			throw new IOException("Invalid properties [" + path + "]");
		}
		return props;
	}
}

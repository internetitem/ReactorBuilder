package com.internetitem.reactorBuilder.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyedOptions {

	private Map<String, List<String>> options;

	public KeyedOptions() {
		this.options = new HashMap<>();
	}

	public void addValue(String key, String value) {
		List<String> options = this.options.get(key);
		if (options == null) {
			options = new ArrayList<>();
			this.options.put(key, options);
		}
		options.add(value);
	}

	public List<String> getValues(String key) {
		return options.get(key);
	}

	public String getFirstValue(String key) {
		return getFirstValue(key, null);
	}

	public String getFirstValue(String key, String defaultValue) {
		List<String> values = options.get(key);
		if (values != null) {
			return values.get(0);
		}
		return defaultValue;
	}

	public boolean getBooleanValue(String key) {
		boolean exists = options.containsKey(key);
		if (!exists) {
			return false;
		}
		String value = getFirstValue(key);
		if (value == null || value.isEmpty()) {
			return true;
		}
		if (value.equals("false")) {
			return false;
		}
		return true;
	}
}

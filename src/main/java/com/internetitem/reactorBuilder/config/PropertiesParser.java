package com.internetitem.reactorBuilder.config;

import java.util.*;

public class PropertiesParser {

	private String[] collectionPrefixes;

	public PropertiesParser(String[] collectionPrefixes) {
		this.collectionPrefixes = collectionPrefixes;
	}

	public KeyedOptions parseProperties(KeyedOptions options, Properties props) {
		Map<String, List<KeyValuePair>> collectionMap = new HashMap<>();

		for (String name : props.stringPropertyNames()) {
			String value = props.getProperty(name);
			String collectionKey = getCollectionKey(name);
			if (collectionKey != null) {
				List<KeyValuePair> values = getValuesForKey(collectionMap, collectionKey);
				values.add(new KeyValuePair(name, value));
			} else {
				options.addValue(name, value);
			}
		}

		mergeCollectionMap(options, collectionMap);

		return options;
	}

	private void mergeCollectionMap(KeyedOptions options, Map<String, List<KeyValuePair>> map) {
		for (Map.Entry<String, List<KeyValuePair>> entry : map.entrySet()) {
			String key = entry.getKey();
			List<KeyValuePair> kvps = entry.getValue();
			Collections.sort(kvps);
			for (KeyValuePair kvp : kvps) {
				String value = kvp.getValue();
				options.addValue(key, value);
			}
		}

	}

	private List<KeyValuePair> getValuesForKey(Map<String, List<KeyValuePair>> map, String key) {
		List<KeyValuePair> values = map.get(key);
		if (values == null) {
			values = new ArrayList<>();
			map.put(key, values);
		}
		return values;
	}

	private String getCollectionKey(String name) {
		for (String prefix : collectionPrefixes) {
			if (name.startsWith(prefix + ".")) {
				return prefix;
			}
		}
		return null;
	}

	private class KeyValuePair implements Comparable<KeyValuePair> {
		private String key;
		private String value;

		public KeyValuePair(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		@Override
		public int compareTo(KeyValuePair o) {
			return key.compareTo(o.key);
		}
	}
}

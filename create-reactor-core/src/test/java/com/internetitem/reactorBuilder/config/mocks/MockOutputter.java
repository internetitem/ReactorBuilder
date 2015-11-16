package com.internetitem.reactorBuilder.config.mocks;

import com.internetitem.reactorBuilder.Outputter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MockOutputter implements Outputter {

	private Map<String, String> dataMap;

	public MockOutputter() {
		this.dataMap = new HashMap<>();
	}

	@Override
	public void writeOutput(String name, String data) throws IOException {
		if (dataMap.containsKey(name)) {
			throw new IOException("Data " + name + " already exists");
		}
		dataMap.put(name, data);
	}

	public String getData(String name) {
		return dataMap.get(name);
	}
}

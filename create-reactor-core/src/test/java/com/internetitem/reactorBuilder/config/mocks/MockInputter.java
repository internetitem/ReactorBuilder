package com.internetitem.reactorBuilder.config.mocks;

import com.internetitem.reactorBuilder.Inputter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MockInputter implements Inputter {

	private Map<String, String> dataMap;

	public MockInputter() {
		this.dataMap = new HashMap<>();
	}

	public void addData(String name, String data) {
		dataMap.put(name, data);
	}

	@Override
	public String readInput(String name) throws IOException {
		String data = dataMap.get(name);
		if (data == null) {
			throw new IOException("Input data " + name + " not found");
		}
		return data;
	}
}

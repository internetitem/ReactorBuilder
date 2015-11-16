package com.internetitem.reactorBuilder.config.mocks;

import com.internetitem.reactorBuilder.ModuleLister;

import java.io.IOException;
import java.util.*;

public class MockModuleLister implements ModuleLister {

	private Map<String, List<String>> directoryMap;

	public MockModuleLister() {
		this.directoryMap = new HashMap<>();
	}

	public void addDirectory(String name, String...directories) {
		List<String> directoryList = Arrays.asList(directories);
		directoryMap.put(name, directoryList);
	}

	@Override
	public List<String> listModules(String startingDirectory) throws IOException {
		List<String> directories = directoryMap.get(startingDirectory);
		if (directories == null) {
			throw new IOException("Directory [" + startingDirectory + "] does not exist");
		}
		return directories;
	}
}

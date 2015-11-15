package com.internetitem.reactorBuilder;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FilesystemModuleLister implements ModuleLister {

	@Override
	public List<String> listModules(String startingDirectory) throws IOException {
		File directory = new File(startingDirectory);
		if (!directory.isDirectory()) {
			throw new IOException(startingDirectory + " is not a directory");
		}

		List<String> files = new ArrayList<>();

		for (File subdirectory : directory.listFiles(new DirectoryFilter())) {
			File pom = new File(subdirectory, "pom.xml");
			if (pom.isFile()) {
				files.add(subdirectory.getPath());
			}
		}

		return files;
	}

	private static class DirectoryFilter implements FileFilter {

		@Override
		public boolean accept(File pathname) {
			return pathname.isDirectory();
		}
	}

}

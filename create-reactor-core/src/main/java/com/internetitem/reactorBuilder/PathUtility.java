package com.internetitem.reactorBuilder;

import com.internetitem.reactorBuilder.config.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PathUtility {

	public static Path getRelativeToPath(Configuration config) {
		String relativeToString = config.getRelativeTo();
		if (relativeToString != null) {
			return getAbsolutePath(relativeToString);
		}
		return null;
	}

	public static Path getAbsolutePath(String name) {
		return Paths.get(name).toAbsolutePath();
	}

	public static String relativizePath(Path relativeTo, String filename) {
		if (relativeTo == null) {
			return filename;
		}

		Path path = getAbsolutePath(filename);
		if (path.startsWith(relativeTo)) {
			return relativeTo.relativize(path).toString();
		} else {
			return path.toString();
		}
	}
}

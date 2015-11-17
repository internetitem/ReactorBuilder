package com.internetitem.reactorBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtility {

	public static String loadResource(String resourceName) throws IOException {
		try (InputStream istream = ReactorBuilder.class.getResourceAsStream(resourceName)) {
			if (istream == null) {
				throw new IOException("Unable to load resource " + resourceName);
			}
			try (InputStreamReader reader = new InputStreamReader(istream, "UTF-8")) {
				return FileInputter.slurp(reader);
			}
		}
	}
}

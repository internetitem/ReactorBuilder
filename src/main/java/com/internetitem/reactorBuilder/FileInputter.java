package com.internetitem.reactorBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class FileInputter implements Inputter {
	@Override
	public String readInput(String name) throws IOException {
		try (FileReader reader = new FileReader(name)) {
			return slurp(reader);
		}
	}

	public static String slurp(Reader reader) throws IOException {
		StringBuilder builder = new StringBuilder();
		char[] buf = new char[2048];
		int numRead;
		while ((numRead = reader.read(buf)) > 0) {
			builder.append(buf, 0, numRead);
		}
		return builder.toString();
	}
}

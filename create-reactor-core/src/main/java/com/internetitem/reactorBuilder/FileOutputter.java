package com.internetitem.reactorBuilder;

import java.io.FileWriter;
import java.io.IOException;

public class FileOutputter implements Outputter {

	@Override
	public void writeOutput(String name, String data) throws IOException {
		try (FileWriter writer = new FileWriter(name)) {
			writer.write(data);
		}

	}
}

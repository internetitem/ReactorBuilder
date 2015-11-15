package com.internetitem.reactorBuilder;

import java.io.IOException;

public interface Outputter {
	void writeOutput(String name, String data) throws IOException;
}

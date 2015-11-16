package com.internetitem.reactorBuilder;

import java.io.IOException;
import java.util.List;

public interface ModuleLister {
	List<String> listModules(String startingDirectory) throws IOException;
}

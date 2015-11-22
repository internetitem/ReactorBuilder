package com.internetitem.reactorBuilder.maven;

import com.internetitem.reactorBuilder.LogWrapper;
import org.apache.maven.plugin.logging.Log;

public class MavenLogWrapper implements LogWrapper {

	private Log log;

	public MavenLogWrapper(Log log) {
		this.log = log;
	}

	@Override
	public void info(String message) {
		log.info(message);
	}

	@Override
	public void error(String message) {
		log.error(message);
	}

	@Override
	public void error(String message, Throwable t) {
		log.error(message, t);
	}
}

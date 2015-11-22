package com.internetitem.reactorBuilder;

public interface LogWrapper {
	void info(String message);
	void error(String message);
	void error(String message, Throwable t);
}

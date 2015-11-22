package com.internetitem.reactorBuilder;

public class ConsoleLogWrapper implements LogWrapper {

	private boolean silent;

	public ConsoleLogWrapper(boolean silent) {
		this.silent = silent;
	}

	@Override
	public void info(String message) {
		if (!silent) {
			System.err.println(message);
		}
	}

	@Override
	public void error(String message) {
		if (!silent) {
			System.err.println(message);
		}
	}

	@Override
	public void error(String message, Throwable t) {
		if (!silent) {
			System.err.println(message);
			t.printStackTrace(System.err);
		}
	}
}

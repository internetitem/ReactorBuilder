package com.internetitem.reactorBuilder.config;

import com.internetitem.reactorBuilder.LogWrapper;

import java.util.ArrayList;
import java.util.List;

public class MockLogger implements LogWrapper {

	private List<String> messages;

	public MockLogger() {
		this.messages = new ArrayList<>();
	}

	@Override
	public void info(String message) {
		messages.add(message);
	}

	@Override
	public void error(String message) {
		messages.add(">" + message);
	}

	@Override
	public void error(String message, Throwable t) {
		messages.add("+>" + message);
	}

	public List<String> getMessages() {
		return messages;
	}
}

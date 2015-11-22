package com.internetitem.reactorBuilder;

import java.util.Collection;

public class StringUtility {
	public static String joinValues(Collection<String> values, String separator) {
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for (String value : values) {
			if (!first) {
				builder.append(separator);
			} else {
				first = false;
			}
			builder.append(value);
		}
		return builder.toString();
	}
}

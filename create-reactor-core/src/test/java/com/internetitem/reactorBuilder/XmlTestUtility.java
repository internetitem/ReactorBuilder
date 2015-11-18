package com.internetitem.reactorBuilder;

import nu.xom.Element;
import nu.xom.Elements;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlTestUtility {


	public static void assertElementText(String expectedValue, Element e) {
		assertNotNull(e);
		String actualValue = e.getValue();
		assertEquals(expectedValue, actualValue);
	}

	public static void assertElementsText(Elements es, String... expectedValues) {
		assertNotNull(es);
		assertEquals(expectedValues.length, es.size());
		for (int i = 0; i < expectedValues.length; i++) {
			String expected = expectedValues[i];
			Element e = es.get(i);
			assertElementText(expected, e);
		}
	}

}

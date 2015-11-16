package com.internetitem.reactorBuilder.config;

import org.junit.Test;

import java.util.List;
import java.util.Properties;

import static org.junit.Assert.*;

public class PropertiesParserTest {

	@Test
	public void testParseEmptyProperties() throws Exception {
		PropertiesParser parser = new PropertiesParser(new String[0]);
		Properties props = new Properties();
		KeyedOptions options = new KeyedOptions();
		KeyedOptions options2 = parser.parseProperties(options, props);
		assertSame(options, options2);
	}

	@Test
	public void testParseBasicProperties() throws Exception {
		PropertiesParser parser = new PropertiesParser(new String[0]);
		Properties props = new Properties();
		props.put("foo", "bar");
		KeyedOptions options = parser.parseProperties(new KeyedOptions(), props);
		assertEquals("bar", options.getFirstValue("foo"));
	}

	@Test
	public void testParseMultiProperties() throws Exception {
		PropertiesParser parser = new PropertiesParser(new String[] {"multi"});
		Properties props = new Properties();
		props.put("multi.1", "one");
		props.put("multi.2", "two");
		props.put("multi.0", "zero");
		KeyedOptions options = parser.parseProperties(new KeyedOptions(), props);
		List<String> values = options.getValues("multi");
		assertNotNull(values);
		assertEquals(3, values.size());
		assertEquals("zero", values.get(0));
		assertEquals("one", values.get(1));
		assertEquals("two", values.get(2));
	}
}

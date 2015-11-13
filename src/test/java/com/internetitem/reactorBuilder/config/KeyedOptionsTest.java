package com.internetitem.reactorBuilder.config;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class KeyedOptionsTest {

	@Test
	public void testAddValue() throws Exception {
		KeyedOptions cmdline = new KeyedOptions();
		cmdline.addValue("foo", "bar");

		assertEquals("bar", cmdline.getFirstValue("foo"));
	}

	@Test
	public void testAddValues() throws Exception {
		KeyedOptions cmdline = new KeyedOptions();
		cmdline.addValue("foo", "bar");
		cmdline.addValue("foo", "baz");

		assertEquals("bar", cmdline.getFirstValue("foo"));
		List<String> values = cmdline.getValues("foo");
		assertNotNull(values);
		assertEquals(2, values.size());

		assertEquals("bar", values.get(0));
		assertEquals("baz", values.get(1));
	}

	@Test
	public void testMissingValue() throws Exception {
		KeyedOptions cmdline = new KeyedOptions();
		cmdline.addValue("foo", "bar");

		assertNull(cmdline.getFirstValue("blah"));
	}

	@Test
	public void testBooleanDefault() {
		KeyedOptions cmdline = new KeyedOptions();
		cmdline.addValue("something", "");
		assertTrue(cmdline.getBooleanValue("something"));
	}

	@Test
	public void testBooleanFalseMissing() {
		KeyedOptions cmdline = new KeyedOptions();
		cmdline.addValue("something", "");
		assertFalse(cmdline.getBooleanValue("nottrue"));
	}

	@Test
	public void testBooleanTrue() {
		KeyedOptions cmdline = new KeyedOptions();
		cmdline.addValue("yup", "anything");
		assertTrue(cmdline.getBooleanValue("yup"));
	}

	@Test
	public void testBooleanFalse() {
		KeyedOptions cmdline = new KeyedOptions();
		cmdline.addValue("nope", "false");
		assertFalse(cmdline.getBooleanValue("nope"));
	}
}

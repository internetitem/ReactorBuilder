package com.internetitem.reactorBuilder.config;

import com.internetitem.reactorBuilder.config.exception.CommandLineParsingException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CommandLineParserTest {

	@Test
	public void testEmptyCommandLine() throws Exception {
		CommandLineParser parser = new CommandLineParser();
		KeyedOptions parsed = parser.parseCommandLine(new String[]{});
		assertNotNull(parsed);
	}

	@Test
	public void testCommandLineArgs() throws Exception {
		CommandLineParser parser = new CommandLineParser();
		KeyedOptions parsed = parser.parseCommandLine(new String[]{"--foo=bar"});
		String bar = parsed.getFirstValue("foo");
		assertEquals("bar", bar);
	}

	@Test(expected = CommandLineParsingException.class)
	public void testBadCommandLine() throws Exception {
		CommandLineParser parser = new CommandLineParser();
		parser.parseCommandLine(new String[]{"foo=bar"});
	}

	@Test(expected = CommandLineParsingException.class)
	public void testBadCommandLineWithEmptyKey() throws Exception {
		CommandLineParser parser = new CommandLineParser();
		parser.parseCommandLine(new String[]{"--=bar"});
	}

}

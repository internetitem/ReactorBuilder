package com.internetitem.reactorBuilder;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class StringUtilityTest {

	@Test
	public void testJoinStringsOneItem() {
		List<String> items = new ArrayList<>();
		items.add("one");
		String joined = StringUtility.joinValues(items, ",");
		assertEquals("one", joined);
	}

	@Test
	public void testJoinStringsTwoItems() {
		List<String> items = new ArrayList<>();
		items.add("one");
		items.add("two");
		String joined = StringUtility.joinValues(items, ",");
		assertEquals("one,two", joined);
	}

	@Test
	public void testJoinStringsThreeItems() {
		List<String> items = new ArrayList<>();
		items.add("one");
		items.add("two");
		items.add("three");
		String joined = StringUtility.joinValues(items, ",");
		assertEquals("one,two,three", joined);
	}
}

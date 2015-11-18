package com.internetitem.reactorBuilder;

import org.junit.Test;

import java.nio.file.Path;

import static org.junit.Assert.assertEquals;

public class PathUtilityTest {

	@Test
	public void testRelativePath() throws Exception {
		Path relativeTo = PathUtility.getAbsolutePath("/file1");
		String relativePath = PathUtility.relativizePath(relativeTo, "/file1/file2");
		assertEquals("file2", relativePath);
	}

	@Test
	public void testDontRelativePath() throws Exception {
		Path relativeTo = PathUtility.getAbsolutePath("/file1");
		String relativePath = PathUtility.relativizePath(relativeTo, "/file3/file2");
		Path actualPath = PathUtility.getAbsolutePath("/file3/file2");
		assertEquals(actualPath.toString(), relativePath);
	}

}
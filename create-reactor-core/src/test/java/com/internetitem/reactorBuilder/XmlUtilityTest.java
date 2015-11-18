package com.internetitem.reactorBuilder;

import nu.xom.Document;
import nu.xom.Element;
import org.junit.Test;

import static org.junit.Assert.*;

public class XmlUtilityTest {

	@Test
	public void testFormatXml() throws Exception {
		Element root = new Element("parent");
		Element child = new Element("child");
		child.appendChild("value");
		root.appendChild(child);
		Document doc = new Document(root);
		String xml = XmlUtility.xmlFromDocument(doc);
		assertTrue(xml.contains("    "));
	}


	@Test
	public void testParseXml() throws Exception {
		Document document = XmlUtility.documentFromXml("<parent><child>value</child></parent>");
		assertNotNull(document);
		Element root = document.getRootElement();
		assertNotNull(root);
		assertEquals("parent", root.getLocalName());
		Element child = root.getFirstChildElement("child");
		assertNotNull(child);
		String text = child.getValue();
		assertEquals("value", text);
	}


}

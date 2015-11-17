package com.internetitem.reactorBuilder;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;
import nu.xom.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class XmlUtility {

	public static Document documentFromXml(String text) throws IOException {
		Builder parser = new Builder();
		try {
			return parser.build(text, null);
		} catch (ParsingException e) {
			throw new IOException("Unable to parse XML: " + e.getMessage(), e);
		}
	}

	public static String xmlFromDocument(Document doc) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Serializer serializer = new Serializer(baos, "UTF-8");
		serializer.setIndent(4);
		serializer.write(doc);
		return baos.toString("UTF-8");
	}

}

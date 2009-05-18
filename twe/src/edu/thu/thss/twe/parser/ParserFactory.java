package edu.thu.thss.twe.parser;

import edu.thu.thss.twe.parser.xml.XmlParser;

public class ParserFactory {
	public static Parser getXmlParser() {
		return new XmlParser();
	}
}

package com.haven.simplej.xml;

import org.junit.Test;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * @Author: havenzhang
 * @Date: 2019/4/15 19:29
 * @Version 1.0
 */
public class XmlParserTest {

	@Test
	public void obj2xmlTest() throws ParserConfigurationException, IOException {

		Node node = new Node();
		node.setName("hello");
		String xml = XmlParser.obj2xml(node);

		System.out.println(XmlParser.prettyXml(XmlParser.obj2xmlDoc(node)));
	}
}

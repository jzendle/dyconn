/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.level3.hiper.dyconn.api.nso;

import com.level3.hiper.dyconn.api.Device;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashSet;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author zendle.joe
 */
public class XMLParser {

	private Document doc;
	private static final String NID_DEVICES = "/dycon/nid-metro/devices";
	private static final String PE_DEVICES = "/dycon/pe/devices";
	private static final String DEVICE = "device";
	private static final String INTERFACE = "int";
	private static final String NID_SI = "service-instance";
	private static final String PE_INTERFACES = "interfaces";

	private static final Logger log = LoggerFactory.getLogger(XMLParser.class);

	public XMLParser(String xml) throws SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;

		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException ex) {
			log.error(ex.getMessage(), ex);
		}
		ByteArrayInputStream input = null;
		try {
			input = new ByteArrayInputStream(
				xml.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException ex) {
			log.error(ex.getMessage(), ex);
			throw ex;
		}

		doc = builder.parse(input); // throws
	}

	public Collection<Device> getEdgeDevices() throws IllegalArgumentException {
		Collection<Device> ret = new HashSet<>();

		NodeList nodeList = getNodeListForXPath(doc, NID_DEVICES);

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);

			Device device = new Device();

			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element elem = (Element) node;
				String tid = getCurrentTag(elem, DEVICE);
				String inf = getCurrentTag(elem, INTERFACE);
				String si = getCurrentTag(elem, NID_SI);
				device.setTid(tid);
				// TODO will it always be this??
				device.setInf(inf + ".ServiceInstance." + si);
			}
			ret.add(device);

		}

		return ret;
	}

	public Collection<Device> getPeDevices() {
		Collection<Device> ret = new HashSet<>();

		NodeList nodeList = getNodeListForXPath(doc, PE_DEVICES);

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);

			Device device = new Device();

			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element elem = (Element) node;
				String hostname = getCurrentTag(elem, DEVICE);

				String inf = null;

				NodeList interfaces = getCurrentNodeList(elem, PE_INTERFACES);
				// find first "int" - can there be more than 1?
				for (int j = 0; j < interfaces.getLength(); j++) {
					Element infNode = (Element) interfaces.item(j);
					if (infNode.getNodeType() == Node.ELEMENT_NODE) {
						inf = getCurrentTag(infNode, INTERFACE);
						break;
					}

				}

				device.setHostname(hostname);
				device.setInf(inf);
			}
			ret.add(device);

		}

		return ret;
	}

	private String getCurrentTag(Element node, String tag) {
		NodeList nodeList = getCurrentNodeList(node, tag);
		return nodeList.item(0).getTextContent();
	}

	private NodeList getCurrentNodeList(Element node, String tag) {
		NodeList nodeList = node.getElementsByTagName(tag);
		if (nodeList == null || nodeList.getLength() == 0) {
			throw new IllegalArgumentException("tag not found: " + node.getTagName() + '/' + tag);
		}
		return nodeList;

	}

	private NodeList getNodeListForXPath(Document doc, String path) {
		NodeList nodeList = null;
		XPath xPath = XPathFactory.newInstance().newXPath();

		try {
			nodeList = (NodeList) xPath.compile(path).evaluate(doc, XPathConstants.NODESET);
		} catch (XPathExpressionException ex) {
			log.error(NID_DEVICES, ex);
			throw new IllegalArgumentException(NID_DEVICES, ex);
		}
		return nodeList;
	}

}

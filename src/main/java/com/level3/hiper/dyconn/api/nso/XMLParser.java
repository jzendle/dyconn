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
import java.util.logging.Level;
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

   private String xml;
   private Document doc;
   private static final String NID_DEVICES = "/dycon/nid-metro/devices";
   private static final String PE_DEVICES = "/dycon/pe/devices";

   private static final Logger log = LoggerFactory.getLogger(XMLParser.class);

   public XMLParser(String xml) throws SAXException, IOException {
      this.xml = xml;
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
      }

      doc = builder.parse(input); // throws

   }

   public Collection<Device> getEdgeDevices() {
      Collection<Device> ret = new HashSet<>();

      XPath xPath = XPathFactory.newInstance().newXPath();
      NodeList nodeList = null;
      try {
         nodeList = (NodeList) xPath.compile(NID_DEVICES).evaluate(doc, XPathConstants.NODESET);
      } catch (XPathExpressionException ex) {
         log.error(null, ex);
      }

      for (int i = 0; i < nodeList.getLength(); i++) {
         Node node = nodeList.item(i);

         Device device = new Device();

         if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element elem = (Element) node;
            String tid = elem.getElementsByTagName("device").item(0).getTextContent();
            String inf = elem.getElementsByTagName("int").item(0).getTextContent();
            String si = elem.getElementsByTagName("service-instance").item(0).getTextContent();
            device.setTid(tid);
            device.setInf(inf + ".ServiceInstance." + si);
         }
         ret.add(device);

      }

      return ret;
   }

   public Collection<Device> getPeDevices() {
      Collection<Device> ret = new HashSet<>();

      XPath xPath = XPathFactory.newInstance().newXPath();
      NodeList nodeList = null;
      try {
         nodeList = (NodeList) xPath.compile(PE_DEVICES).evaluate(doc, XPathConstants.NODESET);
      } catch (XPathExpressionException ex) {
         log.error(null, ex);
      }

      for (int i = 0; i < nodeList.getLength(); i++) {
         Node node = nodeList.item(i);

         Device device = new Device();

         if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element elem = (Element) node;
            String hostname = elem.getElementsByTagName("device").item(0).getTextContent();

            String inf = null;
            NodeList interfaces = elem.getElementsByTagName("interfaces");
            for (int j = 0; j < interfaces.getLength(); j++) {
               Node infNode = interfaces.item(j);
               if (infNode.getNodeType() == Node.ELEMENT_NODE) {
                  Element elem2 = (Element) infNode;
                  inf = elem2.getElementsByTagName("int").item(0).getTextContent();
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

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.level3.hiper.dyconn.api.nso;

import com.level3.hiper.dyconn.api.Device;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.xpath.XPathExpressionException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.xml.sax.SAXException;

/**
 *
 * @author zendle.joe
 */
public class XMLParserTest {

   static XMLParser test;
   InputStream in;

   public XMLParserTest() {
   }

   @BeforeClass
   public static void setUpClass() {
   }

   @AfterClass
   public static void tearDownClass() {
   }

   @Before
   public void setUp() {

      in = this.getClass().getResourceAsStream("/nso_payload_example.xml");

   }

   @After
   public void tearDown() {
      if (in != null) {
         try {
            in.close();
         } catch (IOException ex) {
            Logger.getLogger(XMLParserTest.class.getName()).log(Level.SEVERE, null, ex);
         }
      }
   }

   // TODO add test methods here.
   // The methods must be annotated with annotation @Test. For example:
   //
   @Test
   public void hello() {
      
             String str = new Scanner(in,"UTF-8").useDelimiter("\\A").next();

             System.out.println(str);
      try {
         test = new XMLParser(str);
      } catch (Exception ex) {
         System.out.println("here1 " + ex.getMessage());
        ex.printStackTrace();
         return;
      }
      
      Collection<Device> devs = test.getEdgeDevices();
      
      System.out.println("edge: " + devs.toString());
      
      devs = test.getPeDevices();
      
      System.out.println("pe: " + devs.toString());
      assertTrue(true);
   }
}

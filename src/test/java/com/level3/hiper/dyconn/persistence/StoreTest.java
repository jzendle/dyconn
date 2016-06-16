/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.level3.hiper.dyconn.persistence;

import com.level3.hiper.dyconn.api.Connection;
import com.level3.hiper.dyconn.api.Device;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author zendle.joe
 */
public class StoreTest {

   static ConnectionStore store = ConnectionStore.instance();

   public StoreTest() {
   }

   @BeforeClass
   public static void setUpClass() {
      System.out.println("setUpClass");
      store.init("C:\\temp\\test.db");
      store.clear();

   }

   @AfterClass
   public static void tearDownClass() {
      System.out.println("tearDownClass");
      store.close();

   }

   @Before
   public void setUp() {
      System.out.println("setUp");
      store.clear();
   }

   @After
   public void tearDown() {
   }

   // TODO add test methods here.
   // The methods must be annotated with annotation @Test. For example:
   //
  
   public void addDevices() {
      System.out.println("addDevices");
      Connection conn1 = new Connection("circ1");
      conn1.addDevice(new Device("dev1", "inf1"));
      conn1.addDevice(new Device("dev2", "inf2"));

      Connection conn2 = new Connection("circ2");
      conn2.addDevice(new Device("dev1", "inf1"));
      conn2.addDevice(new Device("dev3", "inf3"));

      Connection conn3 = new Connection("circ3");
      conn3.addDevice(new Device("dev1", "inf1"));
      conn3.addDevice(new Device("dev4", "inf4"));

      assertTrue(store.addConnection(conn1));
      assertTrue(store.addConnection(conn2));
      assertTrue(store.addConnection(conn3));

      assertEquals(store.getByCircuitId("circ1"), conn1);
      assertEquals(store.getByCircuitId("circ2"), conn2);
      assertEquals(store.getByCircuitId("circ3"), conn3);

      store.dump();

      System.out.println("addDevices done");

   }

   @Test
   public void attemptToAddExisting() {
      System.out.println("attemptToAddExisting");

      addDevices();

      Connection conn1 = new Connection("circ1");
      conn1.addDevice(new Device("dev1", "inf1"));
      conn1.addDevice(new Device("dev2", "inf2"));

      assertFalse(store.addConnection(conn1));

   }

   @Test
   public void deleteByDevice() {
      System.out.println("deleteByDevice");

      addDevices();
      Boolean ret = store.deleteByDeviceName("dev1");

      System.out.println("after deleting by device");
      store.dump();

      assertTrue(ret);

   }

}

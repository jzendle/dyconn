/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.level3.hiper.dyconn.snmp;

import java.io.IOException;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.smi.Address;
import org.snmp4j.transport.DefaultUdpTransportMapping;

/**
 *
 * @author zendle.joe
 */
public class SnmpPool {

   // recommended in docs to share single instance across threads
   private static org.snmp4j.Snmp theSnmp = null;

   public static Snmp instance() throws IOException {
      if (theSnmp == null) {
         TransportMapping<? extends Address> transport = new DefaultUdpTransportMapping();;
         theSnmp = new Snmp(transport);
         transport.listen();

      }

      return theSnmp;
   }
}

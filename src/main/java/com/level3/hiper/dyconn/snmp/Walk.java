package com.level3.hiper.dyconn.snmp;

import java.io.IOException;
import java.util.List;
import org.apache.log4j.Logger;

import org.snmp4j.CommunityTarget;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TreeUtils;
import org.snmp4j.util.TreeEvent;



public class Walk {

   private String hostName;
   private String oidStr;
   private String commStr;
   private int snmpVersion;
   private String portNum;
   private final String usage;
   Address tgtAddress;
   //TransportMapping<? extends Address> transport;
   //Snmp snmp;
   CommunityTarget tgt;

   private static final Logger log = Logger.getLogger(Walk.class);

   Walk(String host, int port, int version, String commStr) {
      // Set default value.
      this.hostName = host;
      this.commStr = commStr;
      this.snmpVersion = SnmpConstants.version2c;
      if ( version == 1 ) this.snmpVersion = SnmpConstants.version1;
      this.portNum = Integer.toString(port);
      tgtAddress = GenericAddress.parse("udp:" + hostName + "/" + portNum);
      // setting up target
      tgt = new CommunityTarget();
      tgt.setCommunity(new OctetString(commStr));
      tgt.setAddress(tgtAddress);
      tgt.setRetries(3);
      tgt.setTimeout(1000 * 3);
      tgt.setVersion(snmpVersion);

      usage = "Usage: snmpwalk [-c communityName -p portNumber -v snmpVersion(1 or 2)] targetAddr oid";
   }

   void init() throws IOException {
      Config.instance();
   }

   // private int getIndexForMatching(Target target, String strOid, String toMatch) throws IOException {
   private int getIndexForMatching(Target target, String strOid, String toMatch) throws IOException {

      int ret = -1;

      OID oid = null;
      try {
         oid = new OID(strOid);
      } catch (RuntimeException ex) {
         throw new IllegalArgumentException("invalid oid: " + strOid, ex);
      }

      TreeUtils treeUtils = new TreeUtils(Config.instance(), new DefaultPDUFactory());
      List<TreeEvent> events = treeUtils.getSubtree(target, oid);
      if (events == null || events.size() == 0) {
         log.info("no subtree found for " + strOid);
         return ret;
      }

      // Get snmpwalk result.
      for (TreeEvent event : events) {
         // if (event != null) {
         if (event.isError()) {
            System.err.println("oid [" + oid + "] " + event.getErrorMessage());
         }

//         VariableBinding[] varBindings = event.getVariableBindings();
//         if (varBindings == null || varBindings.length == 0) {
//            // System.out.println("No result returned.");
//            break;
//         }
         for (VariableBinding varBinding : event.getVariableBindings()) {
            int sz = varBinding.getOid().size();
            String var = varBinding.getVariable().toString();
            if ( toMatch.equals(var)) {
               ret = varBinding.getOid().get(sz-1);
               break;
            }
            System.out.println(
                    varBinding.getOid()
                    + " : "
                    + varBinding.getOid().get(sz-1)
                    + " : "
                    + varBinding.getVariable().getSyntaxString()
                    + " : "
                    + varBinding.getVariable());

         }
      }

      return ret;
   }

   private void doSnmpwalk() throws IOException {

      OID oid = null;
      try {
         oid = new OID(oidStr);
      } catch (RuntimeException ex) {
         System.out.println("OID is not specified correctly.");
         System.exit(1);
      }

      TreeUtils treeUtils = new TreeUtils(Config.instance(), new DefaultPDUFactory());
      List<TreeEvent> events = treeUtils.getSubtree(tgt, oid);
      if (events == null || events.size() == 0) {
         System.out.println("No result returned.");
         System.exit(1);
      }

      // Get snmpwalk result.
      for (TreeEvent event : events) {
         // if (event != null) {
         if (event.isError()) {
            System.err.println("oid [" + oid + "] " + event.getErrorMessage());
         }

         VariableBinding[] varBindings = event.getVariableBindings();
//         if (varBindings == null || varBindings.length == 0) {
//            // System.out.println("No result returned.");
//            break;
//         }
         for (VariableBinding varBinding : varBindings) {
            int sz = varBinding.getOid().size();
            System.out.println(
                    varBinding.getOid()
                    + " : "
                    + varBinding.getOid().get(sz-1)
                    + " : "
                    + varBinding.getVariable().getSyntaxString()
                    + " : "
                    + varBinding.getVariable());
         }
      }
   }

   void close() throws IOException {
      Config.instance().close();
   }

   private static void checkAndSetArgs(String[] args) {
  
    
      if (args.length < 2) {
         System.err.println(usage);
         System.exit(0);
      }

      for (int i = 0; i < args.length; i++) {
         if ("-c".equals(args[i])) {
            commStr = args[++i];
         } else if ("-v".equals(args[i])) {
            if (Integer.parseInt(args[++i]) == 1) {
               snmpVersion = SnmpConstants.version1;
            } else {
               snmpVersion = SnmpConstants.version2c;
            }
         } else if ("-p".equals(args[i])) {
            portNum = args[++i];
         } else {
            hostName = args[i++];
            oidStr = args[i];
         }
      }
      if (hostName == null || oidStr == null) {
         System.err.println(usage);
         System.exit(0);
      }
   }

   // Delegate main function to Snmpwalk.
   public static void main(String[] args) throws IOException {
      System.out.println("len" + args.length);
      String[] tmpArgs = {"-c", "comm2", "-p", "161", "-v", "2", "jzdev", "1.3.6.1.2.1.31.1.1.1.1"};
      if (args.length == 8) {
         tmpArgs = args;
      }
      
          
       String hostName;
   String oidStr;
    String commStr;
   int snmpVersion;
   String portNum;
    final String usage;
//      Walk snmpwalk = new Walk();

      try {
         snmpwalk.checkAndSetArgs(tmpArgs);
         snmpwalk.init();
         snmpwalk.doSnmpwalk();
      } catch (Exception e) {
         System.err.println("----- An Exception happend as follows. Please confirm the usage. -----");
         e.printStackTrace();
         System.err.println("--------------------");
      }
      finally {
         snmpwalk.close();
      }
   }
}

package com.level3.hiper.dyconn.snmp;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
// import org.apache.log4j.Logger;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.Counter32;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TreeUtils;
import org.snmp4j.util.TreeEvent;
import org.snmp4j.TransportMapping;

public class Requestor {

   private final String hostName;
   private String oidStr;
   private final String commStr;
   private int snmpVersion;
   private final String portNum;
   Address tgtAddress;
   TransportMapping<? extends Address> transport;
   //Snmp snmp;
   CommunityTarget communityTarget = new CommunityTarget();

//      private static final Logger log = Logger.getLogger(Requestor.class);
   Requestor(String host, int port, String commStr, int version) {
      // Set default value.
      this.hostName = host;
      this.commStr = commStr;
      this.snmpVersion = SnmpConstants.version2c;
      if (version == 1) {
         this.snmpVersion = SnmpConstants.version1;
      }
      this.portNum = Integer.toString(port);
      this.tgtAddress = GenericAddress.parse("udp:" + hostName + "/" + portNum);
      // setting up target
      this.communityTarget.setCommunity(new OctetString(commStr));
      this.communityTarget.setAddress(tgtAddress);
      this.communityTarget.setRetries(3);
      this.communityTarget.setTimeout(1000 * 3);
      this.communityTarget.setVersion(this.snmpVersion);

   }

   private long getIndexForMatching(String strOid, String toMatch) throws IOException {

      long ret = -1;

      OID oid = null;
      try {
         oid = new OID(strOid);
      } catch (RuntimeException ex) {
         throw new IllegalArgumentException("invalid oid: " + strOid, ex);
      }

      TreeUtils treeUtils = new TreeUtils(SnmpPool.instance(), new DefaultPDUFactory());
      List<TreeEvent> events = treeUtils.getSubtree(communityTarget, oid);
      if (events == null || events.isEmpty()) {
         System.out.println("no subtree found for " + strOid);
         return ret;
      }

      // Get snmpwalk result.
      for (TreeEvent event : events) {
         // if (event != null) {}
         if (event.isError()) {
            System.err.println("oid [" + oid + "] " + event.getErrorMessage());
         }

         VariableBinding[] varBindings = event.getVariableBindings();
         if (varBindings == null || varBindings.length == 0) {
            // System.out.println("No result returned.");
            break;
         }
         for (VariableBinding varBinding : event.getVariableBindings()) {
            int sz = varBinding.getOid().size();
            String var = varBinding.getVariable().toString();
            if (var.matches(toMatch)) {
               ret = varBinding.getOid().getUnsigned(sz - 1);
               break;
            }
            System.out.println(
                    varBinding.getOid()
                    + " : "
                    + varBinding.getOid().get(sz - 1)
                    + " : "
                    + varBinding.getVariable().getSyntaxString()
                    + " : "
                    + varBinding.getVariable());

         }
      }

      return ret;
   }

   private String getIndexesForMatching(String strOid, String toMatch, char delimiter) throws IOException {

      System.out.println("toMatch: " + toMatch);

      String ret = null;

      OID oid = null;
      try {
         oid = new OID(strOid);
      } catch (RuntimeException ex) {
         throw new IllegalArgumentException("invalid oid: " + strOid, ex);
      }
      TreeUtils treeUtils = new TreeUtils(SnmpPool.instance(), new DefaultPDUFactory());
      List<TreeEvent> events = treeUtils.getSubtree(communityTarget, oid);
      if (events == null || events.isEmpty()) {
         System.out.println("no subtree found for " + strOid);
         return ret;
      }

      ret = ""; // got here so give non-null response at least
      // Get snmpwalk result.
      for (TreeEvent event : events) {
         // if (event != null) {}
         if (event.isError()) {
            System.err.println("oid [" + oid + "] " + event.getErrorMessage());
         }

         // build delim delimitted string of indexes
         for (VariableBinding varBinding : event.getVariableBindings()) {
            int sz = varBinding.getOid().size();
            String var = varBinding.getVariable().toString();
            System.out.println("variable: " + varBinding.getVariable());
            if (var.matches(toMatch)) {
               if (!ret.isEmpty()) {
                  ret += delimiter;
               }
               ret += Integer.toString(varBinding.getOid().get(sz - 1));
            }

         }
      }

      return ret;
   }

   public long getConterForOid(String oidStr) throws IOException {

      long ret = -1;
      PDU pdu = new PDU();
      pdu.add(new VariableBinding(new OID(oidStr)));
      pdu.setType(PDU.GET);
      pdu.setRequestID(new Integer32(1));
      ResponseEvent response = SnmpPool.instance().get(pdu, communityTarget);
      if (response == null) {
         return ret;
      }
      PDU responsePDU = response.getResponse();

      int errorStatus = responsePDU.getErrorStatus();

      if (errorStatus == PDU.noError) {
         // ret = responsePDU.getVariableBindings().elementAt(0).toString();
         System.out.println("Snmp Get Response = " + responsePDU.getVariableBindings());
      } else {
         int errorIndex = responsePDU.getErrorIndex();
         String errorStatusText = responsePDU.getErrorStatusText();

      }
      Counter32 ctr;
      ctr = (Counter32) responsePDU.get(0).getVariable();
      return ctr.getValue();
   }

   private String[] expand(String[] oidStr2, String index, int offset) {
      String[] out = new String[oidStr2.length];
      for (int i = 0; i < oidStr2.length; i++) {
         out[i] = oidStr2[i].replaceAll("\\{\\{" + index + "\\}\\}", Integer.toString(offset));
      }
      return out;
   }

   void close() throws IOException {
      SnmpPool.instance().close();
   }

   public static void main(String[] args) throws IOException {

      String hostName = args[0];
      String ifName = "1.3.6.1.2.1.31.1.1.1.1"; // ifName
      String ifAlias = "1.3.6.1.2.1.31.1.1.1.18"; // ifAlias
      String cbQosIdIndex = "1.3.6.1.4.1.9.9.166.1.1.1.1.4";
      String oidStr2[] = {"1.3.6.1.2.1.2.2.1.10.{{index}}", "1.3.6.1.2.1.2.2.1.16.{{index}}"};
      String commStr = "twtinet94";
      int snmpVersion = 2;
      int portNum = 161;
      Requestor requestor = new Requestor(hostName, portNum, commStr, snmpVersion);

      try {
         String offset = requestor.getIndexesForMatching(ifAlias, ".*" + args[1] + ".*", '|');
         System.out.println("oids from circuit search: " + offset);
         if (offset.equals("")) {
            System.out.println("no offset found for " + args[1]);
            return;
         }

         // String[] arr = requestor.expand(oidStr2, "index", offset);
         // System.out.println(requestor.getConterForOid(arr[0]));
         long str = requestor.getIndexForMatching(cbQosIdIndex, ".*" + offset + ".*");
         System.out.println("oids: " + str);
      } catch (Exception e) {
         System.err.println("----- An Exception happend as follows. Please confirm the usage. -----");
         e.printStackTrace();
         System.err.println("--------------------");
      } finally {
         requestor.close();
      }
   }

}

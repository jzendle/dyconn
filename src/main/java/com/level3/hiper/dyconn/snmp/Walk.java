package com.level3.hiper.dyconn.snmp;

import java.io.IOException;
import java.util.List;

import org.snmp4j.CommunityTarget;
import org.snmp4j.Snmp;
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

	private String targetAddr;
	private String oidStr;
	private String commStr;
	private int snmpVersion;
	private String portNum;
	private final String usage;
	Address targetAddress;
	TransportMapping<? extends Address> transport;
	Snmp snmp;
	CommunityTarget target;

	Walk() throws IOException {
		// Set default value.
		targetAddr = null;
		oidStr = null;
		commStr = "public";
		snmpVersion = SnmpConstants.version2c;
		portNum = "161";
		usage = "Usage: snmpwalk [-c communityName -p portNumber -v snmpVersion(1 or 2)] targetAddr oid";
	}

	void init() throws IOException {
		targetAddress = GenericAddress.parse("udp:" + targetAddr + "/" + portNum);
		transport = new DefaultUdpTransportMapping();
		snmp = new Snmp(transport);
		transport.listen();
		// setting up target
		target = new CommunityTarget();
		target.setCommunity(new OctetString(commStr));
		target.setAddress(targetAddress);
		target.setRetries(3);
		target.setTimeout(1000 * 3);
		target.setVersion(snmpVersion);
	}

	private void doSnmpwalk() throws IOException {


		OID oid = null;
		try {
			oid = new OID(oidStr);
		} catch (RuntimeException ex) {
			System.out.println("OID is not specified correctly.");
			System.exit(1);
		}

		TreeUtils treeUtils = new TreeUtils(snmp, new DefaultPDUFactory());
		List<TreeEvent> events = treeUtils.getSubtree(target, oid);
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
				if (varBindings == null || varBindings.length == 0) {
					// System.out.println("No result returned.");
					break;
				}
				for (VariableBinding varBinding : varBindings) {
					/* System.out.println(
						varBinding.getOid()
						+ " : "
						+ varBinding.getVariable().getSyntaxString()
						+ " : "
						+ varBinding.getVariable());
*/
				}
			// }
		}
	}

	void close() throws IOException {
		snmp.close();
	}

	private void checkAndSetArgs(String[] args) {
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
				targetAddr = args[i++];
				oidStr = args[i];
			}
		}
		if (targetAddr == null || oidStr == null) {
			System.err.println(usage);
			System.exit(0);
		}
	}

	// Delegate main function to Snmpwalk.
	public static void main(String[] args) throws IOException {
		System.out.println("len" + args.length);
		String[] tmpArgs = {"-c", "public", "-p", "161", "-v", "1", "localhost", "1.3.6.1.2.1.31.1.1.1.1"};
		if (args.length == 8) {
			tmpArgs = args;
		}
		Walk snmpwalk = new Walk();
		try {
			snmpwalk.checkAndSetArgs(tmpArgs);
			snmpwalk.init();
			while (true==true) {
				snmpwalk.doSnmpwalk();
				Thread.sleep(100);
			}
		} catch (Exception e) {
			snmpwalk.close();
			System.err.println("----- An Exception happend as follows. Please confirm the usage. -----");
			e.printStackTrace();
			System.err.println("--------------------");
		}
	}
}

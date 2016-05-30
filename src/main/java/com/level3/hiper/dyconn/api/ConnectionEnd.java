package com.level3.hiper.dyconn.api;

import java.util.HashSet;
import java.util.Set;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author jzendle
 */
public class ConnectionEnd {

	private String circuitId = "";
   @JsonProperty
	private Set<String> devices = new HashSet<>();

   public ConnectionEnd(String circuitId) {
      this.circuitId = circuitId;
   } 
   public ConnectionEnd() {
   }

   public String getCircuitId() {
      return circuitId;
   }

   public void setCircuitId(String circuitId) {
      this.circuitId = circuitId;
   }

   public Set<String> getDevices() {
      return devices;
   }

   public void setDevices(Set<String> devices) {
      this.devices = devices;
   }

   public void addDevice(String device) {
      this.devices.add(device);
   }

   
}

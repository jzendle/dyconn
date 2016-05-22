package com.level3.hiper.dyconn.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author jzendle
 */
@XmlRootElement
public class Input {

	private String circuitId = "";
   @JsonProperty
	private Set<String> devices = new HashSet<>();

   public Input(String circuitId) {
      this.circuitId = circuitId;
   } 
   public Input() {
   }

   public String getCircuitId() {
      return circuitId;
   }

   public void setCircuitId(String circuitId) {
      this.circuitId = circuitId;
   }

   // @JsonProperty
   public Set<String> getDevices() {
      return devices;
   }

   // @JsonProperty
   public void setDevices(Set<String> devices) {
      this.devices = devices;
   }

   public void addDevice(String device) {
      this.devices.add(device);
   }

   
}

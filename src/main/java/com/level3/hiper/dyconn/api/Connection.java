package com.level3.hiper.dyconn.api;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jzendle
 */
public class Connection implements IValidate {

	private String circuitId = "";
	private List<Device> devices = new ArrayList<>();


   public Connection() {
   }
   public Connection(String circuitId) {
      this.circuitId = circuitId;
   } 

   public String getCircuitId() {
      return circuitId;
   }

   public void setCircuitId(String circuitId) {
      this.circuitId = circuitId;
   }

   public List<Device> getDevices() {
      return devices;
   }

   public void setDevices(List<Device> devices) {
      this.devices = devices;
   }

   public void addDevice(Device device) {
      this.devices.add(device);
   }

   @Override
   public void validate() {
      if ( circuitId == null || "".equals(circuitId)) throw new ValidationException("circuitId cannot be empty");

      for (Device device : devices) {
         device.validate();
      }
   }
   
}

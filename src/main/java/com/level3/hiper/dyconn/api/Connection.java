package com.level3.hiper.dyconn.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author jzendle
 */
public class Connection implements IValidate,Serializable,Comparable<Connection> {

   private Integer bandwidth;
   private Integer cos;
	private String circuitId = "";
	private List<Device> devices = new ArrayList<>();

   private static Set<Integer> validCos = new HashSet<>(Arrays.asList(new Integer(1), new Integer(3), new Integer(5)));


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
      if ( !validCos.contains(cos)) throw new ValidationException("invalid value for cos: " + cos);
      if ( bandwidth == null || bandwidth < 0 ) throw new ValidationException("invalid value for bandwidth: " + bandwidth);
      for (Device device : devices) {
         device.validate();
      }
   }

   public Integer getBandwidth() {
      return bandwidth;
   }

   public void setBandwidth(Integer bandwidth) {
      this.bandwidth = bandwidth;
   }

   public Integer getCos() {
      return cos;
   }

   public void setCos(Integer cos) {
      this.cos = cos;
   }

   @Override
   public String toString() {
      return "Connection{" + " circuitId=" + circuitId + " devices=" + devices + " bandwidth=" + bandwidth + " cos=" + cos + " '}'";
   }

   @Override
   public int compareTo(Connection o) {
      return this.circuitId.compareTo(o.circuitId);
   }

   @Override
   public int hashCode() {
      int hash = 5;
      hash = 29 * hash + Objects.hashCode(this.circuitId);
      return hash;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }
      if (obj == null) {
         return false;
      }
      if (getClass() != obj.getClass()) {
         return false;
      }
      final Connection other = (Connection) obj;
      if (!Objects.equals(this.circuitId, other.circuitId)) {
         return false;
      }
      return true;
   }
   
   public Boolean containsDevice(String name) {
      boolean ret = false;
      for (Device device : devices) {
         if ( name.equals(device.getName())) {
            ret = true;
            break;
         }
      }

      return ret;
   }
   


   
}

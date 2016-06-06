/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.level3.hiper.dyconn.network.device;

import java.util.Objects;

/**
 *
 * @author zendle.joe
 */
// entry from devices.txt

// OVTR IRNG4838I7001 10.248.253.155 2c 161 0v3rtur31sg
public class Info {
   String type;
   String name;
   String ip;
   String version;
   String community;

   public Info(String type, String name, String ip, String version, String community) {
      this.type = type;
      this.name = name;
      this.ip = ip;
      this.version = version;
      this.community = community;
   }

   public Info() {
   }

   public String getType() {
      return type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getIp() {
      return ip;
   }

   public void setIp(String ip) {
      this.ip = ip;
   }

   public String getVersion() {
      return version;
   }

   public void setVersion(String version) {
      this.version = version;
   }

   public String getCommunity() {
      return community;
   }

   public void setCommunity(String community) {
      this.community = community;
   }

   @Override
   public int hashCode() {
      int hash = 7;
      hash = 79 * hash + Objects.hashCode(this.name);
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
      final Info other = (Info) obj;
      if (!Objects.equals(this.name, other.name)) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      return "DeviceInfo{" + "type=" + type + ", name=" + name + ", ip=" + ip + ", version=" + version + ", community=" + community + '}';
   }
   
   
}

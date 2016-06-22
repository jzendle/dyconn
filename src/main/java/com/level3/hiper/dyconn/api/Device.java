/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.level3.hiper.dyconn.api;

import java.io.Serializable;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author zendle.joe
 */
@JsonIgnoreProperties({"preferredName"})

public class Device implements IValidate ,Serializable {
   String tid = "";
   String hostname = "";
   String inf = "";

   public Device() {
   }

   public Device(String tid, String hostname ,String inf) {
      this.tid = tid;
      this.hostname = hostname;
      this.inf = inf;
   }

   public String getTid() {
      return tid;
   }

   public void setTid(String tid) {
      this.tid = tid;
   }

   public String getHostname() {
      return hostname;
   }

   public void setHostname(String hostname) {
      this.hostname = hostname;
   }

   public String getInf() {
      return inf;
   }

   public void setInf(String inf) {
      this.inf = inf;
   }

   // prefer hostname over tid
   public String getPreferredName() {
      if ( hostname != null && !"".equals(hostname)) return hostname;
      return tid;
   }
   
   @Override
   public void validate() throws ValidationException {
      if ( tid == null || "".equals(tid)) throw new ValidationException("device name cannot be empty");
      if ( inf == null || "".equals(inf)) throw new ValidationException("interface cannot be empty");
   }

   @Override
   public String toString() {
      return "Device{tid=" + tid + ",hostname=" + hostname + ", inf=" + inf + '}';
   }
   
}

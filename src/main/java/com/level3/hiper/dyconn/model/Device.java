/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.level3.hiper.dyconn.model;

import com.level3.hiper.dyconn.api.IValidate;
import com.level3.hiper.dyconn.api.ValidationException;
import java.io.Serializable;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author zendle.joe
 */
@JsonIgnoreProperties({"preferredName","objectCode"})

public class Device implements Serializable {
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
   
   public String getObjectCode() {
      if( tid == null || "".equals(tid)) return "";
      return tid.substring(8, 10);
   }
   

   @Override
   public String toString() {
      return "Device{tid=" + tid + ",hostname=" + hostname + ", inf=" + inf + '}';
   }
   
}

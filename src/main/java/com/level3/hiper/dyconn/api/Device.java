/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.level3.hiper.dyconn.api;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author zendle.joe
 */
public class Device implements IValidate {
   String name = "";
   String inf = "";

   public Device() {
   }

   public Device(String name, String inf) {
      this.name = name;
      this.inf = inf;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getInf() {
      return inf;
   }

   public void setInf(String inf) {
      this.inf = inf;
   }

   @Override
   public void validate() throws ValidationException {
      if ( name == null || "".equals(name)) throw new ValidationException("device name cannot be empty");
      if ( inf == null || "".equals(inf)) throw new ValidationException("interface cannot be empty");
   }
   
}
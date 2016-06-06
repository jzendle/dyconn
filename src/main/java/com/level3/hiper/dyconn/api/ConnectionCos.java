package com.level3.hiper.dyconn.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author jzendle
 */
public class ConnectionCos extends Connection implements IValidate {

   private Integer bandwidth;
   private Integer cos;

   private static Set<Integer> validCos = new HashSet<>(Arrays.asList(new Integer(1), new Integer(3), new Integer(5)));

   

   public ConnectionCos() {
   }
   public ConnectionCos(String circuitId) {
      super(circuitId);
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
   public void validate() throws ValidationException {
      super.validate();
      if ( !validCos.contains(cos)) throw new ValidationException("invalid value for cos: " + cos);
      
   }

   
}

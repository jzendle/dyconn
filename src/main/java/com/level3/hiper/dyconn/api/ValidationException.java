/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.level3.hiper.dyconn.api;

/**
 *
 * @author zendle.joe
 */
public class ValidationException extends IllegalArgumentException {

   public ValidationException() {
      super();
   }

   public ValidationException(String s) {
      super(s);
   }

   public ValidationException(String message, Throwable cause) {
      super(message, cause);
   }

   public ValidationException(Throwable cause) {
      super(cause);
   }
   
}

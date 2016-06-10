/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.level3.hiper.dyconn.persistence;

import java.io.IOException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author zendle.joe
 */
public class JsonMapper {
   private static ObjectMapper mapper = new ObjectMapper();
   public static String toJson(Object obj) throws IOException {
      return mapper.writeValueAsString(obj);
   }
   public static Object fromJson(String string, Class clazz) throws IOException {
      return mapper.readValue(string,clazz);
   }
   
}

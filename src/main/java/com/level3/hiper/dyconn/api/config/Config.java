/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.level3.hiper.dyconn.api.config;

import io.ous.jtoml.JToml;
import io.ous.jtoml.Toml;
import java.io.IOException;

/**
 *
 * @author zendle.joe
 */
public class Config {

   private static Config theRepo = null;

   private Toml toml = null;
   private String fn = "";

   public static Config instance() {
      if (theRepo == null) {
         theRepo = new Config();
      }
      return theRepo;
   }

   public Config initialize(String fn) throws IOException {
      this.fn = fn;
      this.toml = JToml.parse(Config.class.getResourceAsStream(fn));
      return this;
   }

   public String getString(String key) {
      return this.toml.getString(key);
   }

   public Long getLong(String key) {
      return toml.getLong(key);
   }
   
}

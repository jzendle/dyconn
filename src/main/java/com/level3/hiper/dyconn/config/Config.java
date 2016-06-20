/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.level3.hiper.dyconn.config;

import io.ous.jtoml.JToml;
import io.ous.jtoml.Toml;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zendle.joe
 */
public class Config {

   private static final Logger log = LoggerFactory.getLogger(Config.class);
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
      String value = this.toml.getString(key);
      log.debug("key '{}': value '{}'", key, value);
      return value;
   }

   public Long getLong(String key) {
      Long value = toml.getLong(key);
      log.debug("key '{}': value '{}'", key, value);
      return value;
   }

   public String env() {
      return this.toml.getString("env");
   }

}

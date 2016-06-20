/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.level3.hiper.dyconn.config;

import io.ous.jtoml.JToml;
import io.ous.jtoml.Toml;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author zendle.joe
 */
public class Test {
//   public String name;
//   public Double fl;
//
//   class Embed {
//      public String embedString;
//      public Integer embedId;
//   }
//   public List<Embed> embedded;
//
//   class Embed2 {
//      public String embedString;
//      public Integer embed2Id;
//   }
//   public Embed2 e2;
//
public static void main ( String [] args ) throws IOException {
//   Test tt = new Test();
//   tt.name = "testName";
//   tt.fl = 56.6;
//
//   tt.embedded = new ArrayList<>();
//   Test.Embed embi = tt.new Embed();
//   embi.embedId = 45;
//   embi.embedString = "embedSt1";
//
//   tt.embedded.add(embi);
//   tt.embedded.add(embi);
//
//   tt.e2.embedString = "frfr";
//   tt.e2.embed2Id = 45;
    Toml toml = JToml.parse(Test.class.getResourceAsStream("/dyconn-toml.cfg"));

    String env = toml.getString("env");
    System.out.println(toml.getString("servers." + env + ".ip"));
}   

   
}

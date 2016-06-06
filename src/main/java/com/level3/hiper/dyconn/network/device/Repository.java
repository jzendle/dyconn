/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.level3.hiper.dyconn.network.device;

import com.level3.hiper.dyconn.network.device.Info;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

/**
 *
 * @author zendle.joe
 */
/*
** wrapper around devices.txt periodically refreshed
 */
public class Repository {

   private static Repository theRepo;

   public static Repository instance() {
      if (theRepo == null) {
         theRepo = new Repository();
      }
      return theRepo;
   }

   private Map<String, Info> deviceInfoMap = new HashMap<>();
   private String fileName;

   public synchronized void load(String fn) throws IOException {

      fileName = fn;
      // entry from devices.txt
      // OVTR IRNG4838I7001 10.248.253.155 2c 161 0v3rtur31sg

      LineIterator it = FileUtils.lineIterator(new File(fn), "UTF-8");
      deviceInfoMap.clear();
      try {
         while (it.hasNext()) {
            String line = it.nextLine();
            if ("".equals(line)) continue;
            if (line.startsWith("#")) continue;
            String [] parts = line.split("\\s+");
            String name = parts[1];
            deviceInfoMap.put(name, new Info(parts[0], parts[1], parts[2], parts[3], parts[4]));
            
         }
      } finally {
         it.close();
      }

   }

   public void refresh() throws IOException {
      this.load(fileName);
   }

   public synchronized Info getByName(String name) {
      return deviceInfoMap.get(name);
   }

   public static void main (String [] args) throws IOException {
      String cfgName = "/dyconn-toml.cfg";
      com.level3.hiper.dyconn.api.config.Config.instance().init(cfgName);
      String devFile = com.level3.hiper.dyconn.api.config.Config.instance().getString("network.dev.deviceFile");

      Repository.instance().load(devFile);

      String dev = "AUSXTXIKW2001";
      System.out.println("device " + dev +": " + Repository.instance().getByName(dev));

      
   }

}

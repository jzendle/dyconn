/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.level3.hiper.dyconn.network.device;

import com.level3.hiper.dyconn.api.config.Config;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author zendle.joe
 */
public class Refresh extends Thread {


   @Override
   public void run() {
      try {
         System.out.println("refreshing network devices");
         Repository.instance().refresh();
      } catch (IOException ex) {
         Logger.getLogger(Refresh.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   public static void main (String [] args) throws IOException, InterruptedException {
      String cfgName = "/dyconn-toml.cfg";
      Config.instance().initialize(cfgName);
      String devFile = Config.instance().getString("network.dev.deviceFile");

      Repository.instance().load(devFile);

      String dev = "AUSXTXIKW2001";
      System.out.println("device " + dev +": " + Repository.instance().getByName(dev));

      ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
      ses.scheduleAtFixedRate(new Refresh(), 0, 10, TimeUnit.SECONDS);

      System.out.println("here");
      
   }
   
}

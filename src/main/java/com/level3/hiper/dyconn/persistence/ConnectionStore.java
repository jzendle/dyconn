/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.level3.hiper.dyconn.persistence;

import com.level3.hiper.dyconn.api.Connection;
import com.level3.hiper.dyconn.api.Device;
import com.level3.hiper.dyconn.api.config.Config;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import org.mapdb.BTreeKeySerializer;
import org.mapdb.DB;
import org.mapdb.Fun;
import org.mapdb.DBMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zendle.joe
 */
public class ConnectionStore {

   private static final Logger log = LoggerFactory.getLogger(ConnectionStore.class);
   private static final String BY_CIRCUIT = "byCircuit";
   private static final String BY_DEVICE = "byDevice";
   private static final String REPLAY = "replayMsg";

   private ConcurrentMap<String, Connection> byCircuitMap = null;
   private NavigableSet<Fun.Tuple2<String, Connection>> byDeviceMultiMap = null;

   static ConnectionStore instance = null;

   public static ConnectionStore instance() {
      if (instance == null) {
         instance = new ConnectionStore();
      }
      return instance;
   }

   private DB db;

   public void init(String file) {
      
      log.info("accessing db file: " + file);
      db = DBMaker.newFileDB(new File(file)).closeOnJvmShutdown().make();

      byDeviceMultiMap = (db.exists(BY_DEVICE) ? db.getTreeSet(BY_DEVICE)
              : db.createTreeSet(BY_DEVICE).serializer(BTreeKeySerializer.TUPLE2).make());

      byCircuitMap = (db.exists(BY_CIRCUIT) ? db.getHashMap(BY_CIRCUIT)
              : db.createHashMap(BY_CIRCUIT).make());
   }

   public Boolean addConnection(Connection conn) {
      // return null if circuit exists

      String circuitId = conn.getCircuitId();
      // circuitId should be unique
      Boolean exists = byCircuitMap.containsKey(circuitId);
      if (exists) {
         return false;
      }

      for (Device device : conn.getDevices()) {
         byDeviceMultiMap.add(Fun.t2(device.getName(), conn));
      }
      byCircuitMap.put(circuitId, conn);

      db.commit();

      return true;

   }

   public Connection getByCircuitId(String circuitId) {
      return byCircuitMap.get(circuitId);
   }

   public List<Connection> getByDeviceName(String deviceName) {
      List<Connection> ret = new ArrayList<>();
      for (Connection conn : Fun.filter(byDeviceMultiMap, deviceName)) {
         ret.add(conn);
      }
      return ret;
   }

   public Boolean deleteByCircuitId(String circuitId) {
      Boolean ret = false;

      // only one connection should match
      Connection conn = byCircuitMap.remove(circuitId);

      if (conn == null) {
         return ret;
      }
      for (Device dev : conn.getDevices()) {
         // find all matching devices by name 
         String deviceName = dev.getName();
         for (Connection candidate : Fun.filter(byDeviceMultiMap, deviceName)) {
            // remove them if circuitId matches
            if (candidate.getCircuitId().equals(circuitId)) {
               byDeviceMultiMap.remove(Fun.t2(deviceName, candidate));
            }
         }
      }

      db.commit();

      return true;
   }

   public Boolean deleteByDeviceName(String deviceName) {

      // not a pretty delete - must find all instances 
      Set<Connection> connections = new HashSet<>();

      // we have to do a full scan because the device name is burried in list.
      // If this is too slow an additional index can be maintained for quick lookup
      for (Map.Entry<String, Connection> entry : byCircuitMap.entrySet()) {
         String key = entry.getKey();
         Connection value = entry.getValue();
         if (value.containsDevice(deviceName)) {
            connections.add(value);
         }
      }
      int cnt = 0;
      for (Connection connection : connections) {

         Connection deleted = byCircuitMap.remove(connection.getCircuitId());
         if (deleted != null) {
            cnt++;
            for (Device device : connection.getDevices()) {
               if (byDeviceMultiMap.remove(Fun.t2(device.getName(), deleted))) {
                  cnt++;
               } else {
                  log.warn("record not found in byDeviceMultiMap for device: {} connection: {}", device.getName(), deleted);
               }
            }
         }
      }

      db.commit();

      return cnt > 0 ? true : false;

   }

   public void clear() {
      byDeviceMultiMap.clear();
      byCircuitMap.clear();
      db.commit();
   }

   public synchronized void close() {
      if (!db.isClosed()) {
         db.close();
      }
   }

   public void dump() {
      System.out.println("byCircuitMap dump: ");
      for (Map.Entry<String, Connection> entry : byCircuitMap.entrySet()) {
         String key = entry.getKey();
         Connection value = entry.getValue();
         System.out.println("key: " + key + " value: " + value);
      }

      System.out.println("byDeviceMultimapMap dump: ");
      Iterator iter = byDeviceMultiMap.iterator();
      while (iter.hasNext()) {
         Fun.Tuple2<String, Connection> tup = (Fun.Tuple2<String, Connection>) iter.next();
         System.out.println("key:" + tup.a + " value: " + tup.b);
      }

   }

   public void initialize() {
      String key = "db." + Config.instance().env() + ".file";
      String value = Config.instance().getString(key);
      if (key == null) {
         throw new IllegalArgumentException("no value for key: " + value);
      }
      this.init(value);
   }

}

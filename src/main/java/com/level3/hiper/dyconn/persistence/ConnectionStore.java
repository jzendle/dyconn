/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.level3.hiper.dyconn.persistence;

import com.level3.hiper.dyconn.config.Config;
import com.level3.hiper.dyconn.model.Connection;
import com.level3.hiper.dyconn.model.Device;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentMap;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;
import org.mapdb.serializer.SerializerArrayTuple;

/**
 *
 * @author zendle.joe
 */
public class ConnectionStore {

   private static final String BY_CIRCUIT = "byCircuit";
   private static final String BY_DEVICE = "byDevice";
   private static final String REPLAY = "replayMsg";

   private ConcurrentMap<String, Connection> byCircuitMap = null;
   private NavigableSet<Object[]> byDeviceMultiMap = null;

   static ConnectionStore instance = null;

   public static ConnectionStore instance() {
      if (instance == null) {
         instance = new ConnectionStore();
      }
      return instance;
   }

   private DB db;

   public void init(String file) {

      db = DBMaker.fileDB(file).closeOnJvmShutdown().make();

      byCircuitMap = (ConcurrentMap<String, Connection>) db.hashMap(BY_CIRCUIT).createOrOpen();

      byDeviceMultiMap = db.treeSet(BY_DEVICE)
              //set tuple serializer
              .serializer(new SerializerArrayTuple(Serializer.STRING, Serializer.JAVA))
              .createOrOpen();
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
         byDeviceMultiMap.add(new Object[]{device.getPreferredName(), conn});
      }
      byCircuitMap.put(circuitId, conn);

      db.commit();

      return true;

   }

   public Connection getByCircuitId(String circuitId) {
      return byCircuitMap.get(circuitId);
   }

   public Collection<Connection> getByDeviceName(String preferredDeviceName) {
      Collection<Connection> ret = new ArrayList<>();

      SortedSet sub;
      sub = byDeviceMultiMap.subSet(new Object[]{preferredDeviceName}, new Object[]{preferredDeviceName, null});

      Object[] arrayArray = sub.toArray();
      for (Object arrayObj : arrayArray) {
         if (arrayObj instanceof Object[]) {
            Object[] array = (Object[]) arrayObj;
            for (int i = 0; i < array.length; i++) {
               Object object = array[i];
               // array contains alternating keys and values
               if (object instanceof Connection) {
                  ret.add((Connection) object);
               }
            }
         }
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
         String deviceName = dev.getPreferredName();
         for (Connection candidate : getByDeviceName(deviceName)) {
            // remove them if circuitId matches
            if (candidate.getCircuitId().equals(circuitId)) {
               byDeviceMultiMap.remove(new Object[]{deviceName, candidate});
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
               if (byDeviceMultiMap.remove(new Object[]{device.getPreferredName(), deleted})) {
                  cnt++;
               } else {
                  System.err.format("record not found in byDeviceMultiMap for device: {} connection: {}", device, deleted);
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
         for (Object object : (Object[]) iter.next()) {
            if (object instanceof String) {
               System.out.print("key: " + object);
            } else {
               System.out.println(" value: " + object);
            }
         }
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

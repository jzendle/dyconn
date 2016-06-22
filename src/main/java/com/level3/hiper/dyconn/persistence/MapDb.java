/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.level3.hiper.dyconn.persistence;

import com.level3.hiper.dyconn.api.Connection;
import com.level3.hiper.dyconn.api.Device;
import java.io.File;
import java.util.Date;
import java.util.NavigableSet;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import org.mapdb.BTreeKeySerializer;
import org.mapdb.Bind;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Fun;

/**
 *
 * @author zendle.joe
 */
public class MapDb {

   public static void main(String[] args) {
      test3(args);
   }

   public static void test1(String[] args) {
      String name = "map";
      DB db = null;
      try {
         db = DBMaker.newFileDB(new File("c:\\temp\\test.db")).make();
         ConcurrentMap<String, String> map = (db.exists(name) ? db.getHashMap(name) : db.createHashMap(name).make());

         String akey = "key_" + new Date().toString();
         map.put(akey, "here");
         db.commit();
         Set<String> keys = map.keySet();
         keys.stream().forEach((key) -> {
            System.out.println("key: " + key + " value: " + map.get(key));
         });
      } catch (Exception e) {

      } finally {
         db.close();
      }
   }

   public static void test2(String[] args) {
      int i = 0;
      String name = "connectionMap";
      try {
         DB db = DBMaker.newFileDB(new File("c:\\temp\\test.db")).make();
         ConcurrentMap<String, Connection> map;
         map = (db.exists(name) ? db.getHashMap(name) : db.createHashMap(name).make());

         for (int j = 10; j < 20; j++) {

            String akey = "circuit_" + j;

            Connection conn = new Connection(akey);
            conn.addDevice(new Device("name", "","inf"));
            conn.setBandwidth(45);
            conn.setCos(3);
            map.put(akey, conn);
         }
         db.commit();
         Set<String> keys = map.keySet();
         keys.stream().forEach((key) -> {
            System.out.println("key: " + key + " value: " + map.get(key));
         });
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public static void test3(String[] args) {
      int i = 0;
      String name = "treeSet";
      DB db = null;
      try {
         db = DBMaker.newFileDB(new File("c:\\temp\\test.db")).make();
         NavigableSet<Fun.Tuple2<String, Connection>> multiMap = (db.exists(name) ? db.getTreeSet(name) : db.createTreeSet(name).serializer(BTreeKeySerializer.TUPLE2).make());

         String akey = "device";
         for (int j = 10; j < 20; j++) {

            Connection conn = new Connection("" + j);
            conn.addDevice(new Device("name", "host", "inf"));
            conn.setBandwidth(45);
            conn.setCos(3);
            multiMap.add(Fun.t2(akey, conn));
         }
         db.commit();
         //multiMap.
         for (Connection fred : Fun.filter(multiMap, akey)) {
            System.out.println("value for key 'device': " + fred);
         }
         for (Connection fred : Fun.filter(multiMap, akey)) {
            if (fred.getCircuitId().equals("11")) {
               multiMap.remove(Fun.t2(akey, fred));
               db.commit();
            }
         }
         for (Connection fred : Fun.filter(multiMap, akey)) {
            System.out.println("after delete value for key 'device': " + fred);
         }
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         db.close();
      }
   }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.level3.hiper.dyconn.persistence;

import java.io.File;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import org.mapdb.DB;
import org.mapdb.DBMaker;

/**
 *
 * @author zendle.joe
 */
public class MapDb {

   public static void main(String[] args) {
      DB db = DBMaker.newFileDB(new File("c:\\temp\\test.db")).make();
      String name = "map";
      ConcurrentMap<String,String> map = (db.exists(name) ? db.getHashMap(name) : db.createHashMap(name).make());

      String akey = "key_" + new Date().toString();
      map.put(akey, "here");
      db.commit();
      Set<String> keys = map.keySet();
      keys.stream().forEach((key) -> {
         System.out.println("key: " + key + " value: " + map.get(key));
      });
   }

}

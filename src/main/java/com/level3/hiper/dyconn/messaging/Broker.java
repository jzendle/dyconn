/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.level3.hiper.dyconn.messaging;

import com.level3.hiper.dyconn.Main;
import com.level3.hiper.dyconn.api.config.Config;
import java.util.Properties;
import javax.jms.ConnectionFactory;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zendle.joe
 */
public class Broker {

   private static final Logger log = LoggerFactory.getLogger(Broker.class);

   private static Broker broker = null;

   String connectionFactory;
   String connectionString;
   String queueName;
   Session session;
   MessageProducer messageProducer;
   Connection connection;

   public void initialize() throws NamingException, JMSException {

      connectionFactory = Config.instance().getString("queue." + Main.env() + ".connect.factory");
      connectionString = Config.instance().getString("queue." + Main.env() + ".connect.string");
      queueName = Config.instance().getString("queue." + Main.env() + ".name");

      Properties props = new Properties();
      props.setProperty("java.naming.factory.initial", connectionFactory);
      props.setProperty("connectionfactory.qpidConnectionFactory", connectionString);
      props.setProperty("destination.queueName", queueName);
      Context context = new InitialContext(props);

      ConnectionFactory cf = (ConnectionFactory) context.lookup("qpidConnectionFactory");
      connection = cf.createConnection();
      connection.start();
      session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
      Destination destination = (Destination) context.lookup("queueName");

      messageProducer = session.createProducer(destination);
      context.close();

   }

   public void send(String message, String type) throws JMSException {
      TextMessage msg = session.createTextMessage(message);
      msg.setStringProperty("operation", type);
      log.debug("sending to queue'{}' msg: {}" , queueName, msg);
      messageProducer.send(msg);
   }

   public void shutdown() {
      try {
         if (session != null) {
            session.close();
         }
      } catch (JMSException ex) {
         log.error("closing session", ex);
      }
      try {
         if (connection != null) {
            connection.close();
         }
      } catch (JMSException ex) {
         log.error("closing session", ex);
      }
   }

   public void reset() throws NamingException, JMSException {
      shutdown();
      initialize();
   }

   public static Broker instance() {
      if (broker == null) {
         broker = new Broker();
      }
      return broker;
   }

}

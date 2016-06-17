package com.level3.hiper.dyconn;

import com.level3.hiper.dyconn.api.config.Config;
import com.level3.hiper.dyconn.messaging.Broker;
import com.level3.hiper.dyconn.persistence.ConnectionStore;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.ws.rs.core.UriBuilder;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.glassfish.jersey.server.ServerProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jzendle
 */
public class Main {

   private static final ExecutorService TASK_EXECUTOR = Executors.newCachedThreadPool();

   private static Logger log = LoggerFactory.getLogger(Main.class);

   public static void main(String[] args) throws IOException, NamingException, JMSException {
      try {
         String bootstrap = "/dyconn-toml.cfg";
         CommandLineParser parser = new DefaultParser();
         Options options = new Options();
         options.addOption("c", "config-file", true, "configuration for hapi dyconn module");
         try {
            CommandLine line = parser.parse(options, args);
            if (line.hasOption("config-file")) {
               bootstrap = line.getOptionValue("config-file");
            }
         } catch (ParseException ex) {
            log.error("command line", ex);
            return;
         }

         // read config file
         log.info("loading configuration");
         Config.instance().initialize(bootstrap);

         // initialize queue subsystem
         log.info("initializing messaging");
         Broker.instance().initialize();

         // initilaize persistence

         log.info("initializing persistence");
         ConnectionStore.instance().initialize();
         String host = getHostName();
         Long port = getPort();

         log.info("Starting Embedded Jersey HTTPServer...");
         HttpServer httpServer = createHttpServer(host, port);
         httpServer.start();
         log.info(String.format("\nJersey Application Server started with WADL available at " + "%sapplication.wadl\n", getURI(host, port)));
         log.info("Started Embedded Jersey HTTPServer Successfully !!!");
      } catch (Throwable t) {
         t.printStackTrace();
      } finally {
//         Broker.instance().shutdown();
      }
   }

   private static HttpServer createHttpServer(String host, Long port) throws IOException {
      // jersey looks at this package to find annotation
      final PackagesResourceConfig prc = new PackagesResourceConfig("com.level3.hiper.dyconn.api");
      final Map<String, Object> prcProperties = prc.getProperties();
      prcProperties.put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
      prcProperties.put(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
      prcProperties.put(ServerProperties.BV_DISABLE_VALIDATE_ON_EXECUTABLE_OVERRIDE_CHECK, true);
      // keeping following as documentation of how to register a filter - although was not satiisfied
      // with behavior - filter was getting called twice per method invocation as if in a chacin
//		prcProperties.put("com.sun.jersey.spi.container.ContainerResponseFilters", "com.level3.hiper.dyconn.api.TimingFilter");
//      prcProperties.put("com.sun.jersey.spi.container.ContainerRequestFilters", "com.level3.hiper.dyconn.api.TimingFilter");
      prc.setPropertiesAndFeatures(prcProperties);

      return HttpServerFactory.create(getURI(host, port), prc);
   }

   private static URI getURI(String host, Long port) {
      return UriBuilder.fromUri("http://" + host + "/").port(port.intValue()).build();
   }

   private static String getHostName() {
      String hostName = "localhost";
      try {
         hostName = InetAddress.getLocalHost().getCanonicalHostName();
      } catch (UnknownHostException e) {
         e.printStackTrace();
      }
      return hostName;
   }

   private static Long getPort() {
      String key = "web." + Config.instance().env() + ".port";
      Long ret = new Long(8085);
      Long tmp = Config.instance().getLong(key);
      if (tmp != null) {
         ret = tmp;
      }
      return ret;
   }
}

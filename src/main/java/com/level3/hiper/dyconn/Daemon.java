package com.level3.hiper.dyconn;

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
import javax.ws.rs.core.UriBuilder;
import org.glassfish.jersey.server.ServerProperties;

/**
 *
 * @author jzendle
 */
public class Daemon {

	private static final ExecutorService TASK_EXECUTOR = Executors.newCachedThreadPool();

	public static void main(String[] args) throws IOException {
		System.out.println("Starting Embedded Jersey HTTPServer...\n");
		HttpServer httpServer = createHttpServer();
		httpServer.start();
		System.out.println(String.format("\nJersey Application Server started with WADL available at " + "%sapplication.wadl\n", getURI()));
		System.out.println("Started Embedded Jersey HTTPServer Successfully !!!");
	}

	private static HttpServer createHttpServer() throws IOException {
      // jersey looks at this package to find annotation
		final PackagesResourceConfig prc = new PackagesResourceConfig("com.level3.hiper.dyconn.api");
		final Map<String, Object> prcProperties = prc.getProperties();
		prcProperties.put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
		prcProperties.put(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
		prcProperties.put(ServerProperties.BV_DISABLE_VALIDATE_ON_EXECUTABLE_OVERRIDE_CHECK, true);
      // keeping following as documentation of how to register a filter - although was not satiisfied
      // with behavior - fillter was getting called twice per method invocation
//		prcProperties.put("com.sun.jersey.spi.container.ContainerResponseFilters", "com.level3.hiper.dyconn.api.TimingFilter");
//      prcProperties.put("com.sun.jersey.spi.container.ContainerRequestFilters", "com.level3.hiper.dyconn.api.TimingFilter");
		prc.setPropertiesAndFeatures(prcProperties);

		return HttpServerFactory.create(getURI(), prc);
	}

	private static URI getURI() {
		return UriBuilder.fromUri("http://" + getHostName() + "/").port(8085).build();
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
}

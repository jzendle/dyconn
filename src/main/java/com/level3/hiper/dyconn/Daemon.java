/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.level3.hiper.dyconn;

import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
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
import org.glassfish.jersey.moxy.json.MoxyJsonConfig;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
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
		final PackagesResourceConfig prc = new PackagesResourceConfig("com.level3.hiper.dyconn.api");
		final Map<String, Object> prcProperties = prc.getProperties();
		prcProperties.put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
		prcProperties.put(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
		prcProperties.put(ServerProperties.BV_DISABLE_VALIDATE_ON_EXECUTABLE_OVERRIDE_CHECK, true);
		prc.setPropertiesAndFeatures(prcProperties);
//		 prc.register(MoxyJsonFeature.class);
//        register(new MoxyJsonConfig().setFormattedOutput(true)
//                // Turn off BV otherwise the entities on server would be validated by MOXy as well.
//                .property(MarshallerProperties.BEAN_VALIDATION_MODE, BeanValidationMode.NONE)
//                .resolver());

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

package com.level3.hiper.dyconn.api;

import com.level3.hiper.dyconn.messaging.Broker;
import com.level3.hiper.dyconn.persistence.JsonMapper;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("network/v1/dynamicConnection")
public class Api {

	@GET
	@Path("/ping")
	@Produces(MediaType.APPLICATION_JSON)
	public Response ping() {
      ResponseWrapper ret = new ResponseWrapper();
      Instant start = Instant.now();
		// ConnectionRequest input = new ConnectionRequest();
		// input.setBandwidth(1000000);
		// input.setCos(Cos.Basic);
		Connection connection = new Connection("23/VLXX/23344/TWCS");
		connection.addDevice(new Device("AUSXTCK1W2001","ae/0"));
		connection.addDevice(new Device("AUSXTCK19K001","ae/1"));
		connection.addDevice(new Device("AUSXTCK1C6001","gig0"));
		connection.setBandwidth(1000000);
		connection.setCos(1);

		// input.setConnection(connection);

      ret.setResponse(connection);

      ret.setRuntime(Duration.between(start ,Instant.now()).toNanos());

		return Response.status(201).entity(ret).build();

	}

	@GET
	@Path("/inventory")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCircuit(@QueryParam("circuitId") String circuitId) {

		Connection inp = new Connection(circuitId);
		inp.addDevice(new Device("dev1","inf1"));
		inp.addDevice(new Device("dev2","inf2"));

		return Response.status(201).entity(inp).build();

	}

	@PUT 
	@Path("/collection")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createConnectionPut(@Valid Connection input) {
		return createConnection(input);

	}
	@POST 
	@Path("/collection")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createConnection(@Valid Connection input) {
      Instant start = Instant.now();
      input.validate();


      String json = null;
      try {
         json = JsonMapper.toJson(input);
      } catch (IOException ex) {
         Logger.getLogger(Api.class.getName()).log(Level.SEVERE, null, ex);
      }

      try {
         Broker.instance().send(json, "start");
      } catch (JMSException ex) {
         Logger.getLogger(Api.class.getName()).log(Level.SEVERE, null, ex);
      }

      
      ResponseWrapper ret = new ResponseWrapper();

      ret.setResponse(input);

      ret.setRuntime(Duration.between(start ,Instant.now()).toNanos());
		return Response.status(201).entity(ret).build();

	}

	@DELETE
	@Path("/collection")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response endConnection(@Valid Connection input) {
      Instant start = Instant.now();
      input.validate();
      ResponseWrapper ret = new ResponseWrapper();

      ret.setResponse(input);
      ret.setRuntime(Duration.between(start ,Instant.now()).toNanos());

		return Response.status(201).entity(ret).build();

	}

}

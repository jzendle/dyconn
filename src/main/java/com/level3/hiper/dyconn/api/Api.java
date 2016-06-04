package com.level3.hiper.dyconn.api;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
		ConnectionRequest input = new ConnectionRequest();
		input.setBandwidth(1000000);
		input.setCos(Cos.Basic);
		Connection aEnd = new Connection("23/VLXX/23344/TWCS");
		aEnd.addDevice(new Device("AUSXTCK1W2001","ae/0"));
		aEnd.addDevice(new Device("AUSXTCK19K001","ae/1"));
		aEnd.addDevice(new Device("AUSXTCK1C6001","gig0"));

		input.setConnection(aEnd);

      ret.setResponse(input);

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

	@POST
	@Path("/collection")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createConnection(@Valid ConnectionRequest input) {
      Instant start = Instant.now();
      ResponseWrapper ret = new ResponseWrapper();
      input.validate();

      ret.setResponse(input);

      ret.setRuntime(Duration.between(start ,Instant.now()).toNanos());
		return Response.status(201).entity(ret).build();

	}

	@DELETE
	@Path("/collection")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response endConnection(@Valid DisconnectRequest input) {
      Instant start = Instant.now();
      ResponseWrapper ret = new ResponseWrapper();
      input.validate();

      ret.setResponse(input);
      ret.setRuntime(Duration.between(start ,Instant.now()).toNanos());

		return Response.status(201).entity(ret).build();

	}

}

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
		ConnectionEnd aEnd = new ConnectionEnd("23/VLXX/23344/TWCS");
		aEnd.addDevice("AUSXTCK1W2001");
		aEnd.addDevice("dal1-er2");
		ConnectionEnd zEnd = new ConnectionEnd("43/KFFN/322768/TWCS");
		zEnd.addDevice("AUSXTXKIW2002");
		zEnd.addDevice("dal1-er1");

		input.setaEnd(aEnd);
		input.setzEnd(zEnd);

      ret.setResponse(input);

      ret.setRuntime(Duration.between(start ,Instant.now()).toNanos());

		return Response.status(201).entity(ret).build();

	}

	@GET
	@Path("/inventory")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCircuit(@QueryParam("circuitId") String circuitId) {

		ConnectionEnd inp = new ConnectionEnd(circuitId);
		inp.addDevice("dev1");
		inp.addDevice("dev2");

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

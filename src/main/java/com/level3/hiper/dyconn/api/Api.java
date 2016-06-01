package com.level3.hiper.dyconn.api;

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
	public ConnectionRequest capacityCheck() {

		ConnectionRequest ret = new ConnectionRequest();
		ret.setBandwidth(1000000);
		ret.setCos(Cos.Basic);
		ConnectionEnd aEnd = new ConnectionEnd("circuitA");
		aEnd.addDevice("dev1");
		aEnd.addDevice("dev2");
		ConnectionEnd zEnd = new ConnectionEnd("circuitB");
		zEnd.addDevice("dev3");
		zEnd.addDevice("dev4");

		ret.setaEnd(aEnd);
		ret.setzEnd(zEnd);

		return ret;

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
	@Path("/metrics")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createConnection(@Valid ConnectionRequest input) {
      input.validate();

      input.setError(new Error());

		return Response.status(201).entity(input).build();

	}

	@DELETE
	@Path("/metrics")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response endConnection(@Valid DisconnectRequest input) {
      input.validate();

      input.setError(new Error());
		return Response.status(201).entity(input).build();

	}

}

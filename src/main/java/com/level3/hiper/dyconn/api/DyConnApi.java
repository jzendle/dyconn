package com.level3.hiper.dyconn.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("api/json/dynamic/connection")
public class DyConnApi {

   @GET
   @Path("/capacity")
   @Produces(MediaType.APPLICATION_JSON)
   public Input capacityCheck() {

      Input inp = new Input("fred");
      inp.addDevice("dev1");
      inp.addDevice("dev2");

      return inp;

   }
   @GET
   @Path("/inventory")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getCircuit(@QueryParam("circuitId") String circuitId) {

      Input inp = new Input(circuitId);
      inp.addDevice("dev1");
      inp.addDevice("dev2");

      return Response.status(201).entity(inp).build();

   }

   @POST
   @Path("/inventory")
   @Consumes(MediaType.APPLICATION_JSON)
   public Response createConnection(Input input) {

      return Response.status(201).entity(input).build();

   }

   @DELETE
   @Path("/inventory")
   @Consumes(MediaType.APPLICATION_JSON)
   public Response endConnection(Input input) {

      return Response.status(201).entity(input).build();

   }


}

package com.level3.hiper.dyconn.api;

import com.level3.hiper.dyconn.messaging.Broker;
import com.level3.hiper.dyconn.persistence.ConnectionStore;
import com.level3.hiper.dyconn.persistence.JsonMapper;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("network/v1/dynamicConnection")
public class Api {

   private static final Logger log = LoggerFactory.getLogger(Api.class);

   private enum Operation {
      START,
      STOP
   }

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
      connection.addDevice(new Device("AUSXTCK1W2001", "ae/0"));
      connection.addDevice(new Device("AUSXTCK19K001", "ae/1"));
      connection.addDevice(new Device("AUSXTCK1C6001", "gig0"));
      connection.setBandwidth(1000000);
      connection.setCos(1);

      // input.setConnection(connection);
      ret.setResponse(connection);

      ret.setRuntime(Duration.between(start, Instant.now()).toNanos() / 1000000.0);

      return Response.status(201).entity(ret).build();

   }

   @GET
   @Path("/inventory")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getCircuit(@QueryParam("circuitId") String circuitId) {

      Connection inp = new Connection(circuitId);
      inp.addDevice(new Device("dev1", "inf1"));
      inp.addDevice(new Device("dev2", "inf2"));

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
      long start = System.currentTimeMillis();
      input.validate();

      // check if we circuit is new
      if (!ConnectionStore.instance().addConnection(input)) {
         throw new IllegalArgumentException("dynamic connection is already being monitored:  " + input);
      }

      sendToBroker(input, Operation.START);

      return buildResponse(input, start);

   }

   @DELETE
   @Path("/collection")
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   public Response endConnection(@Valid Connection input) {
      
       long start = System.currentTimeMillis();
      input.validate();

      String circuitID = input.getCircuitId();
      // check if we circuit is new
      if (!ConnectionStore.instance().deleteByCircuitId(circuitID)) {
         throw new IllegalArgumentException("dynamic connection is not currently being monitored:  " + input);
      }

      sendToBroker(input, Operation.STOP);

      return buildResponse(input, start);
     
   }

   private void sendToBroker(Connection input, Operation op) {

      String json = null;
      try {
         json = JsonMapper.toJson(input);
      } catch (IOException ex) {
         log.error(null, ex);
         throw new IllegalArgumentException("unable to parse input:" + input);
      }

      try {
         Broker.instance().send(json, op.toString().toLowerCase());
      } catch (JMSException ex) {
         log.error(null, ex);
         throw new IllegalArgumentException("unable to queue request:" + input);
      }

   }

   private Response buildResponse(Connection input, long start) {
      ResponseWrapper ret = new ResponseWrapper();
      ret.setResponse(input);
      ret.setRuntime((System.currentTimeMillis() - start) / 1000.0);
      return Response.status(201).entity(ret).build();
   }

}

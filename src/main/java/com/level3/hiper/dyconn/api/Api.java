package com.level3.hiper.dyconn.api;

import com.level3.hiper.dyconn.Main;
import com.level3.hiper.dyconn.api.config.Config;
import com.level3.hiper.dyconn.messaging.Broker;
import com.level3.hiper.dyconn.persistence.ConnectionStore;
import com.level3.hiper.dyconn.persistence.JsonMapper;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.jms.JMSException;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("network/v1/dynamicConnection")
public class Api {

	private static final Logger log = LoggerFactory.getLogger(Api.class);

	private enum Operation {
		START,
		STOP
	}

	private final Error err = new Error();
	private ConnectionStore store = ConnectionStore.instance();

	@Context
	UriInfo uriInfo;

	@GET
	@Path("/ping")
	@Produces(MediaType.APPLICATION_JSON)
	public Response ping() {

		log.info(uriInfo.getPath());
		long start = System.currentTimeMillis();

		ResponseWrapper ret = new ResponseWrapper();
		Connection connection = new Connection("23/VLXX/23344/TWCS");
		connection.addDevice(new Device("AUSXTCK1W2001", "ae/0"));
		connection.addDevice(new Device("AUSXTCK19K001", "ae/1"));
		connection.addDevice(new Device("AUSXTCK1C6001", "gig0"));
		connection.setBandwidth(1000000);
		connection.setCos(1);

		return buildResponse(ret, start, err);

	}

	@GET
	@Path("/collection/circuit")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getConnectionByCircuitId(@QueryParam("id") String circuitId) {
		long start = System.currentTimeMillis();

		log.info(uriInfo.getPath() + " id: " + circuitId);

		Connection ret = null;

		if (validate(circuitId) && 
			(ret = store.getByCircuitId(circuitId)) != null) {
		}

		return buildResponse(ret, start, err);

	}

	@GET
	@Path("/collection/device")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getConnectionByDeviceName(@QueryParam("id") String device) {
		long start = System.currentTimeMillis();
		log.info(uriInfo.getPath() + " id: " + device);
		List<Connection> ret = null;

		if (validate(device) && 
			(ret = store.getByDeviceName(device)) != null) {
		}

		return buildResponse(ret, start, err);

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
		log.info(uriInfo.getPath() + " input: " + input);

		if (validate(input) 
			&& okToAdd(input)
			&& sendToBroker(input, Operation.START)
			&& addConnection(input)
			) 
		{
		}

		return buildResponse(input, start, err);

	}

	@DELETE
	@Path("/collection")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response endConnection(@Valid Connection input) {

		long start = System.currentTimeMillis();
		log.info(uriInfo.getPath() + " input: " + input);

		String circuitID = input.getCircuitId();
		// check if we circuit is new

		if (validate(input)
			&& okToDelete(input)
			&& sendToBroker(input, Operation.STOP)
			&& deleteConnection(circuitID)
			) 
		{
		}

		return buildResponse(input, start, err);

	}

	private boolean sendToBroker(Connection input, Operation op) {

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

		return true;

	}

	private Response buildResponse(Object input, long start, Error err) {
		ResponseWrapper ret = new ResponseWrapper();
		ret.setError(err);
		ret.setUri(uriInfo.getAbsolutePath().toString());
		ret.setHost(Main.host);
		ret.setResponse(input);
		ret.setEnvironment(Config.instance().env());
		ret.setRuntime((System.currentTimeMillis() - start) / 1000.0);
		return Response.status(err.getCode()).entity(ret).build();
	}

	boolean validate(Connection input) {
		try {
			input.validate();
			return true;
		} catch (ValidationException exc) {
			err.setCode(412); // precondition not met;
			err.setMessage(Error.FAILURE);
			err.setDetail(exc.getMessage());
			return false;
		}

	}

	boolean validate(String input) {

		if (input != null) {
			return true;

		} else {
			err.setCode(412); // precondition not met;
			err.setMessage(Error.FAILURE);
			err.setDetail("null input param not allowed");
			return false;
		}

	}

	boolean deleteConnection(String circuitId) {
		boolean ret = true;
		if (!ConnectionStore.instance().deleteByCircuitId(circuitId)) {
			err.setCode(404); // not found
			err.setMessage(Error.FAILURE);
			err.setDetail("dynamic connection is not currently being monitored:  " + circuitId);
			ret = false;
		}

		return ret;
	}

	boolean addConnection(Connection input) {
		boolean ret = true;

		// TODO limit error handling to only backend error conditions
		// since there is a routine to handle precondition
		if (!store.addConnection(input)) {
			err.setCode(409); // conflict
			err.setMessage("connection already being monitored");
			err.setDetail("connection " + input.getCircuitId() + " is currently being monitored");
			ret = false;
		}
		return ret;
	}
	boolean okToAdd(Connection input) {

		boolean ret = true;
		String circuitId = input.getCircuitId();
		if (store.getByCircuitId(circuitId) != null) {
			err.setCode(409); // conflict
			err.setMessage("connection already being monitored");
			err.setDetail("connection " + circuitId + " is currently being monitored");
			ret = false;
		}
		return ret;
	}
	boolean okToDelete(Connection input) {

		boolean ret = true;
		String circuitId = input.getCircuitId();
		if (store.getByCircuitId(circuitId) == null) {
				err.setCode(404); // not found
			err.setMessage(Error.FAILURE);
			err.setDetail("dynamic connection is not currently being monitored:  " + circuitId);
			ret = false;
		}
		return ret;
	}

}

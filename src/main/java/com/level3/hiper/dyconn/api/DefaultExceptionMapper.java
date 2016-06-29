/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.level3.hiper.dyconn.api;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * @author jzendle
 */
@Provider
public class DefaultExceptionMapper implements ExceptionMapper<Throwable> {

   private static final Logger log = LoggerFactory.getLogger(DefaultExceptionMapper.class);
	@Override
	public Response toResponse(Throwable ex) {

		Error errorMessage = new Error();
		errorMessage.setDetail(ex.getMessage());
		errorMessage.setCode(500);
      errorMessage.setMessage(ex.getClass().getCanonicalName());

      log.error(errorMessage.toString(), ex);
      
		return Response.status(errorMessage.getCode())
			.entity(errorMessage)
			.type(MediaType.APPLICATION_JSON)
			.build();
	}
}

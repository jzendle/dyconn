/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.level3.hiper.dyconn.api;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import java.util.concurrent.atomic.AtomicInteger;
// import javax.ws.rs.container.ContainerRequestContext;
// import javax.ws.rs.container.ContainerRequestFilter;
// import javax.ws.rs.container.ContainerResponseContext;
// import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.Provider;
import org.glassfish.jersey.filter.LoggingFilter;
// import org.glassfish.jersey.server.ContainerRequest;
// import org.glassfish.jersey.server.ContainerResponse;

// @Provider
// @Prematching
//public class TimingFilter extends LoggingFilter implements ContainerRequestFilter {
public class TimingFilter implements ContainerRequestFilter,ContainerResponseFilter  {

   static AtomicInteger count = new AtomicInteger(0);

   private static final ThreadLocal<Long> startTime = new ThreadLocal<Long>();
   public static boolean verboseLogging = false;

   public TimingFilter() {
   }

   @Override
  public ContainerResponse filter(ContainerRequest req, ContainerResponse contResp) {
 
      System.out.println("filter(req,resp) counter: " + count.getAndAdd(1));
      System.out.println(System.currentTimeMillis() - startTime.get().longValue());
//        ResponseBuilder resp = Response.fromResponse(contResp.getResponse());
//        resp.header("Access-Control-Allow-Origin", "*")
//                .header("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
// 
//        String reqHead = req.getHeaderValue("Access-Control-Request-Headers");
// 
//        if(null != reqHead && !reqHead.equals("")){
//            resp.header("Access-Control-Allow-Headers", reqHead);
//        }
// 
//        contResp.setResponse(resp.build());
            return contResp;
    }
 
/* 
   @Override
   public void filter(ContainerRequestContext req) {
      startTime.set(System.currentTimeMillis());
   }

   @Override
   public void filter(ContainerRequestContext request, ContainerResponseContext resp) {
      System.out.println(System.currentTimeMillis() - startTime.get().longValue());
   }
*/
   @Override
   public ContainerRequest filter(ContainerRequest cr) {
      System.out.println("filter(Requeest) counter: " + count.getAndAdd(1));
      startTime.set(System.currentTimeMillis());
      return cr;
   }
}
/*
    @Override
    public ContainerRequest filter(ContainerRequest arg0) {
        startTime.set(System.currentTimeMillis());
        return arg0;
    }

    @Override
    public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
        System.out.println(System.currentTimeMillis() - startTime.get().longValue());
        StringBuilder sb = new StringBuilder();
        sb.append("User:").append((request.getUserPrincipal() == null ? "unknown" : request.getUserPrincipal().getName()));
        sb.append(" - Path:").append(request.getRequestUri().getPath());
        //...
    }
 */
/**
 *
 * @author zendle.joe
 */

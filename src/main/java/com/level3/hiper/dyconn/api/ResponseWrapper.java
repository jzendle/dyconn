/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.level3.hiper.dyconn.api;

import java.util.UUID;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author zendle.joe
 */
@XmlRootElement
public class ResponseWrapper {

   
   String identifier = UUID.randomUUID().toString();
   String host = "localhost";
   String uri = "/tmp";
   String version = "1.0";
   String environment = "development";
   String owner = "insupport@level3.com";
   Double runtime = new Double(0.0);
   Error error = new Error();
   // DisconnectRequest response;
   Object response;

   public String getIdentifier() {
      return identifier;
   }

   public void setIdentifier(String identifier) {
      this.identifier = identifier;
   }

   public String getHost() {
      return host;
   }

   public void setHost(String host) {
      this.host = host;
   }

   public String getUri() {
      return uri;
   }

   public void setUri(String uri) {
      this.uri = uri;
   }

   public String getVersion() {
      return version;
   }

   public void setVersion(String version) {
      this.version = version;
   }

   public String getEnvironment() {
      return environment;
   }

   public void setEnvironment(String environment) {
      this.environment = environment;
   }

   public String getOwner() {
      return owner;
   }

   public void setOwner(String owner) {
      this.owner = owner;
   }

   public Double getRuntime() {
      return runtime;
   }

   public void setRuntime(Double runtime) {
      this.runtime = runtime;
   }

   public Error getError() {
      return error;
   }

   public void setError(Error error) {
      this.error = error;
   }

   public Object getResponse() {
      return response;
   }

   public void setResponse(Object connection) {
      this.response = connection;
   }

   
}

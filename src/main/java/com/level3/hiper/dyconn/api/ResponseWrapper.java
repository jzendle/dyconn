/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.level3.hiper.dyconn.api;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author zendle.joe
 */
@XmlRootElement
public class ResponseWrapper {

   
   String identifier = "uuid";
   String host = "localhost";
   String uri = "/tmp";
   String version = "1.0";
   String environment = "development";
   String owner = "insupport@level3.com";
   long runtime = 0;
   Error error = new Error();
   DisconnectRequest response;

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

   public long getRuntime() {
      return runtime;
   }

   public void setRuntime(long runtime) {
      this.runtime = runtime;
   }

   public Error getError() {
      return error;
   }

   public void setError(Error error) {
      this.error = error;
   }

   public DisconnectRequest getResponse() {
      return response;
   }

   public void setResponse(DisconnectRequest request) {
      this.response = request;
   }

   
   
   
}

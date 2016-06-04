package com.level3.hiper.dyconn.api;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Path;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jzendle
 */
@XmlRootElement
public class DisconnectRequest {
		
	private Connection connection;

	public DisconnectRequest() {
	}

	public Connection getConnection() {
		return connection;
	}
	public void setConnection(Connection aEnd) {
		this.connection = aEnd;
	}
   
   public void validate() {
      if ( connection == null ) {
         throw new ValidationException("connection must be populated");
      }
      if ( connection .getDevices() ==  null ) {
         throw new ValidationException("connection must contain devices");
      }
   }
 
}

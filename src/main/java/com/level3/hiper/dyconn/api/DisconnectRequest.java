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
		
	private ConnectionEnd aEnd;
	private ConnectionEnd zEnd;


	public DisconnectRequest() {
	}

	public ConnectionEnd getaEnd() {
		return aEnd;
	}
	public void setaEnd(ConnectionEnd aEnd) {
		this.aEnd = aEnd;
	}

	public ConnectionEnd getzEnd() {
		return zEnd;
	}

	public void setzEnd(ConnectionEnd zEnd) {
		this.zEnd = zEnd;
	}

   public void validate() {
      if ( aEnd == null || zEnd == null) {
         throw new ValidationException("both end points must be populated");
      }
      if ( aEnd .getDevices() ==  null || zEnd .getDevices() == null) {
         throw new ValidationException("both end points must contain devices");
      }
   }
 
}

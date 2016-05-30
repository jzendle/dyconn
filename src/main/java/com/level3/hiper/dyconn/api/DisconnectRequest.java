package com.level3.hiper.dyconn.api;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Path;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jzendle
 */
@XmlRootElement
@Path("/")
public class DisconnectRequest {
	@NotNull
	private ConnectionEnd aEnd;
		
	@NotNull
	private ConnectionEnd zEnd;

	public DisconnectRequest() {
	}

	@NotNull
	public ConnectionEnd getaEnd() {
		return aEnd;
	}
	public void setaEnd(ConnectionEnd aEnd) {
		this.aEnd = aEnd;
	}

	@NotNull(message = "frfrfr")
	public ConnectionEnd getzEnd() {
		return zEnd;
	}

	public void setzEnd(ConnectionEnd zEnd) {
		this.zEnd = zEnd;
	}
   
}

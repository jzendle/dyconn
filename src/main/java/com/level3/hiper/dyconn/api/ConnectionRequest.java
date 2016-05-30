package com.level3.hiper.dyconn.api;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jzendle
 */
@XmlRootElement
public class ConnectionRequest extends DisconnectRequest {

	
	private String cos = "";
	private long bandwidth = 0;

	public ConnectionRequest() {
	}

	public String getCos() {
		return cos;
	}

	public void setCos(String cos) {
		this.cos = cos;
	}

	public long getBandwidth() {
		return bandwidth;
	}

	public void setBandwidth(long bandwidth) {
		this.bandwidth = bandwidth;
	}
	
	

   
}

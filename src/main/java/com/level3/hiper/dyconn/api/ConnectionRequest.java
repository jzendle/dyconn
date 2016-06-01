package com.level3.hiper.dyconn.api;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jzendle
 */
@XmlRootElement
public class ConnectionRequest extends DisconnectRequest {

	
	private Cos cos = Cos.Basic;
	private long bandwidth = 0;

	public ConnectionRequest() {
	}

	public Cos getCos() {
		return cos;
	}

	public void setCos(Cos cos) {
		this.cos = cos;
	}

	public long getBandwidth() {
		return bandwidth;
	}

	public void setBandwidth(long bandwidth) {
		this.bandwidth = bandwidth;
	}

   @Override
   public void validate() {
      super.validate();

      if ( cos == null  )
         throw new ValidationException("invalid cos value: " + cos);
      if ( bandwidth <= 0 )
         throw new ValidationException("invalid bandwitdh value: " + bandwidth);
   }
	
	

   
}

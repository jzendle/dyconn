package com.level3.hiper.dyconn.api;

import com.level3.hiper.dyconn.api.nso.NSOParser;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 *
 * @author jzendle
 */
@JsonIgnoreProperties({"NSOParser"})
public class Request implements IValidate {

	private Integer bandwidth;
	private Integer cos;
	private String circuitId = "";
	private String devices = ""; // this node will contain NSO xml doc
	private NSOParser nso = null;
	// private List<Device> devices = new ArrayList<>();

	private static Set<Integer> validCos = new HashSet<>(Arrays.asList(1, 3, 5));

	public Request() {
	}

	public Request(String circuitId) {
		this.circuitId = circuitId;
	}

	public String getCircuitId() {
		return circuitId;
	}

	public void setCircuitId(String circuitId) {
		this.circuitId = circuitId;
	}

	public String getDevices() {
		return devices;
	}

	public void setDevices(String devices) {
		this.devices = devices;
	}


	@Override
	public void validate() throws ValidationException {
		if (circuitId == null || "".equals(circuitId)) {
			throw new ValidationException("circuitId cannot be empty");
		}
		if (!validCos.contains(cos)) {
			throw new ValidationException("invalid value for cos: " + cos);
		}
		if (bandwidth == null || bandwidth < 0) {
			throw new ValidationException("invalid value for bandwidth: " + bandwidth);
		}
		if ( devices == null || "".equals(devices)) {
			throw new ValidationException("devices attribute cannot be empty");
			
		}
		
		nso = new NSOParser(devices);
		
		nso.validate();
		
	}

	public Integer getBandwidth() {
		return bandwidth;
	}

	public void setBandwidth(Integer bandwidth) {
		this.bandwidth = bandwidth;
	}

	public Integer getCos() {
		return cos;
	}

	public void setCos(Integer cos) {
		this.cos = cos;
	}
	
	public NSOParser getNSOParser() {
		return nso;
	}

	@Override
	public String toString() {
		return "Connection{" + " circuitId=" + circuitId  + " bandwidth=" + bandwidth + " cos=" + cos + " devices=" + devices + " '}'";
	}

}

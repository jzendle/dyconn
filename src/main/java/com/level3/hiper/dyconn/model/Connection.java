package com.level3.hiper.dyconn.model;

import com.level3.hiper.dyconn.api.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 *
 * @author jzendle
 */
@JsonIgnoreProperties({"created"})
public class Connection implements Serializable, Comparable<Connection> {

	private Integer bandwidth;
	private Integer cos;
	private String circuitId = "";
	private Collection<Device> devices = new ArrayList<>();

	private static Set<Integer> validCos = new HashSet<>(Arrays.asList(1, 3, 5));

	private Date created;

	public Connection() {
	}

	public Connection(String circuitId) {
		this.circuitId = circuitId;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getCircuitId() {
		return circuitId;
	}

	public void setCircuitId(String circuitId) {
		this.circuitId = circuitId;
	}

	public Collection<Device> getDevices() {
		return devices;
	}

	public void setDevices(Collection<Device> devices) {
		this.devices = devices;
	}

	public void addDevice(Device device) {
		this.devices.add(device);
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

	@Override
	public String toString() {
		return "Connection{" + " circuitId=" + circuitId  + " bandwidth=" + bandwidth + " cos=" + cos + " devices=" + devices + " '}'";
	}

	@Override
	public int compareTo(Connection o) {
		return this.circuitId.compareTo(o.circuitId);
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 29 * hash + Objects.hashCode(this.circuitId);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Connection other = (Connection) obj;
		if (!Objects.equals(this.circuitId, other.circuitId)) {
			return false;
		}
		return true;
	}

	public Boolean containsDevice(String name) {
		boolean ret = false;
		for (Device device : devices) {
			if (name.equals(device.getTid()) ||name.equals(device.getHostname()) ) {
				ret = true;
				break;
			}
		}

		return ret;
	}

}

package com.org.id.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="ACTIVE_SESSION")
public class TabEntity {
	
	private String ipAddress;
	
	@Column(name="ip_address")
	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

}

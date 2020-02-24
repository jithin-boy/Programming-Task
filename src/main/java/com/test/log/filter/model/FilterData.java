package com.test.log.filter.model;

import java.util.List;

public class FilterData {

	private String ipAddress;
	private List<String> debugFlag;
	
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public List<String> getDebugFlag() {
		return debugFlag;
	}
	public void setDebugFlag(List<String> debugFlag) {
		this.debugFlag = debugFlag;
	}
	
}

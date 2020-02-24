package com.test.log.filter.model;

import java.util.List;

public class Filter {
	
	private String logFile;
	
	private List<FilterData> data;

	public String getLogFile() {
		return logFile;
	}

	public void setLogFile(String logFile) {
		this.logFile = logFile;
	}

	public List<FilterData> getData() {
		return data;
	}

	public void setData(List<FilterData> data) {
		this.data = data;
	}


	
	

}

package com.test.log.filter.model;

public class ResponseObject {
	

	private Object response;
	private String status;
	
	public ResponseObject(Object response, String status)
	{
		this.response = response;
		this.status = status;
	}

	public Object getResponse() {
		return response;
	}

	public void setResponse(Object response) {
		this.response = response;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
	
	
	
	

}

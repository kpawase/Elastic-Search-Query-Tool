package com.querytool.model;

import java.util.Date;

public class ErrorDetailsDto {
	private Date timestamp;
	private String message;
	private Integer statusCode;

	public ErrorDetailsDto(Date timestamp, String message, Integer statusCode) {
		super();
		this.timestamp = timestamp;
		this.message = message;
		this.statusCode = statusCode;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

}

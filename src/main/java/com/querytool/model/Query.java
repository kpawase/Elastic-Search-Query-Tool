package com.querytool.model;

public class Query {

	private String matchConstrain;
	private String fieldName;
	private Object fieldValue;
	private String queryType;
	

	public String getMatchConstrain() {
		return matchConstrain;
	}

	public void setMatchConstrain(String matchConstrain) {
		this.matchConstrain = matchConstrain;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Object getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(Object fieldValue) {
		this.fieldValue = fieldValue;
	}

	
	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	@Override
	public String toString() {
		return "Query [matchConstrain=" + matchConstrain + ", fieldName=" + fieldName + ", fieldValue=" + fieldValue
				+ "]";
	}

	
	
}

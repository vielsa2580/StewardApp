package com.example.stewardimperial.models;

import java.util.List;

import org.json.JSONObject;

public final  class MODELResultSet {

	String error,message;	
	JSONObject jsonObject;
	List<?> list;


	//===========================================Constructors====================================================================================================================================

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}

	public MODELResultSet(String error) {
		super();
		this.error = error;
	}

	public MODELResultSet(JSONObject jsonObject) {
		super();
		this.jsonObject = jsonObject;
	}

	public MODELResultSet(String error, String message) {
		super();
		this.error = error;
		this.message = message;
	}

	public MODELResultSet() {
		super();
	}

	//===========================================Getters & Setters====================================================================================================================================	

	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}

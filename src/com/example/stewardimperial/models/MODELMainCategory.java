package com.example.stewardimperial.models;

public class MODELMainCategory {
	
	String id,name;

	public MODELMainCategory(String id,String name){
		super();
		this.id = id;
		this.name = name;
	}
	
	public MODELMainCategory (){
		super();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

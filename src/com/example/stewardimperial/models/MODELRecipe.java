package com.example.stewardimperial.models;



public class MODELRecipe {
	
	int id;
	String quantityUsed;
//	==============================================CONSTRUCTORS========================================================================
	public MODELRecipe(){
		super();
	}

	public MODELRecipe(int id, String quantityUsed){
		super();
		this.id = id;
		this.quantityUsed = new String(quantityUsed);
	}
	
//	==============================================GETTERS & SETTERS========================================================================

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getQuantityUsed() {
		return quantityUsed;
	}

	public void setQuantityUsed(String quantityUsed) {
		this.quantityUsed = quantityUsed;
	}

}

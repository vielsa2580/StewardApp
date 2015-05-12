package com.example.stewardimperial.models;



public class MODELSizeType {

	String sizeTypeName,price;

	//	==============================================CONSTRUCTORS========================================================================
	public MODELSizeType(){
		super();
	}


	public MODELSizeType(String sizeTypeName, String price) {
		super();
		this.sizeTypeName = new String(sizeTypeName);
		this.price = new String(price);
	}

	//	==============================================GETTERS & SETTERS========================================================================

	public String getSizeTypeName() {
		return sizeTypeName;
	}


	public void setSizeTypeName(String sizeTypeName) {
		this.sizeTypeName = sizeTypeName;
	}


	public String getPrice() {
		return price;
	}


	public void setPrice(String price) {
		this.price = price;
	}


}

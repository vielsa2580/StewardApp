package com.example.stewardimperial.models;



public class MODELTable {
	
	int id,quantity;
	String tableName;
	
//	==============================================CONSTRUCTORS========================================================================
	public MODELTable(){
		super();
	}

	public MODELTable(int id, int quantity, String tableName){
		super();
		this.id = id;
		this.quantity = quantity;
		this.tableName = tableName;
	}

	public MODELTable(MODELTable modelTable) {
		super();
		this.id = modelTable.getId();
		this.quantity = modelTable.getQuantity();
		this.tableName = new String(modelTable.getTableName());
	}
	
//	==============================================GETTERS & SETTERS========================================================================
	
	
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
}

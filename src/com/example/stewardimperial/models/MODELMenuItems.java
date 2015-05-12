package com.example.stewardimperial.models;

import java.util.ArrayList;
import java.util.List;

public class MODELMenuItems {

	int id;
	String itemName, catIdFk, comments, tax, imageUrl, kitchenIdFk,
	venueTypeIdFk, priority,itemDes,feedback,selectedSize,selectedQuantity,cookingStatus;

	List<MODELRecipe> recipeList;
	List<MODELSizeType> sizeTypeList;

	//	==============================================CONSTRUCTORS========================================================================	

	public MODELMenuItems(){
		super();
	}

	public MODELMenuItems(MODELMenuItems modelMenuItem){
		super();
		
		this.id = modelMenuItem.getId();
		this.itemName = new String(modelMenuItem.getItemName());
		this.catIdFk = new String(modelMenuItem.getCatIdFk());
		this.comments = new String(modelMenuItem.getComments());
		this.tax = new String(modelMenuItem.getTax());
		this.imageUrl = new String(modelMenuItem.getImageUrl());
		this.kitchenIdFk = new String(modelMenuItem.getKitchenIdFk());
		this.cookingStatus = new String();
		this.venueTypeIdFk = new String(modelMenuItem.getVenueTypeIdFk());
		this.priority = new String(modelMenuItem.getPriority());
		this.itemDes = new String(modelMenuItem.getItemDes());
		this.feedback = modelMenuItem.getFeedback() != null ?new String(modelMenuItem.getFeedback()):"";
		this.selectedSize = modelMenuItem.getSelectedSize() != null ? new String(modelMenuItem.getSelectedSize()): "";
		this.selectedQuantity = modelMenuItem.getSelectedQuantity() != null ? new String(modelMenuItem.getSelectedQuantity()): "";

		this.recipeList = new ArrayList<MODELRecipe>();
		this.sizeTypeList = new ArrayList<MODELSizeType>();
		
		for (int m = 0; m < modelMenuItem.getRecipeList().size(); m++) {
			this.recipeList.add(new MODELRecipe(modelMenuItem.getRecipeList().get(m).getId(), modelMenuItem.getRecipeList().get(m).getQuantityUsed()));
		}
		
		for (int m = 0; m < modelMenuItem.getSizeTypeList().size(); m++) {
			this.sizeTypeList.add(new MODELSizeType(new String(modelMenuItem.getSizeTypeList().get(m).getSizeTypeName()), new String(modelMenuItem.getSizeTypeList().get(m).getPrice())));
		}
		
	}

	//	==============================================GETTERS & SETTERS========================================================================	

	public String getCookingStatus() {
		return cookingStatus;
	}

	public void setCookingStatus(String cookingStatus) {
		this.cookingStatus = cookingStatus;
	}

	
	public String getSelectedQuantity() {
		return selectedQuantity;
	}

	public void setSelectedQuantity(String selectedQuantity) {
		this.selectedQuantity = selectedQuantity;
	}
	
	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public String getSelectedSize() {
		return selectedSize;
	}

	public void setSelectedSize(String selectedSize) {
		this.selectedSize = selectedSize;
	}
	
	
	public String getItemDes() {
		return itemDes;
	}

	public void setItemDes(String itemDes) {
		this.itemDes = itemDes;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getCatIdFk() {
		return catIdFk;
	}

	public void setCatIdFk(String catIdFk) {
		this.catIdFk = catIdFk;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getTax() {
		return tax;
	}

	public void setTax(String tax) {
		this.tax = tax;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getKitchenIdFk() {
		return kitchenIdFk;
	}

	public void setKitchenIdFk(String kitchenIdFk) {
		this.kitchenIdFk = kitchenIdFk;
	}

	public String getVenueTypeIdFk() {
		return venueTypeIdFk;
	}

	public void setVenueTypeIdFk(String venueTypeIdFk) {
		this.venueTypeIdFk = venueTypeIdFk;
	}


	public String getPriority() {
		return priority;
	}



	public void setPriority(String priority) {
		this.priority = priority;
	}



	public List<MODELRecipe> getRecipeList() {
		return recipeList;
	}

	public void setRecipeList(List<MODELRecipe> recipeList) {
		this.recipeList = recipeList;
	}

	public List<MODELSizeType> getSizeTypeList() {
		return sizeTypeList;
	}

	public void setSizeTypeList(List<MODELSizeType> sizeTypeList) {
		this.sizeTypeList = sizeTypeList;
	}

}

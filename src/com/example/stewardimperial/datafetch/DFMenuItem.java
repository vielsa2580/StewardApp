package com.example.stewardimperial.datafetch;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.stewardimperial.connectivity.ServerConnect;
import com.example.stewardimperial.models.MODELMenuItems;
import com.example.stewardimperial.models.MODELRecipe;
import com.example.stewardimperial.models.MODELResultSet;
import com.example.stewardimperial.models.MODELSizeType;
import com.example.stewardimperial.utils.UTILConstants;

public class DFMenuItem {

	public MODELResultSet retrieveMenuItem(){

		List<MODELMenuItems> listCategory = new ArrayList<MODELMenuItems>();
		MODELMenuItems modelMenuItems;
		JSONObject jsonObj = new JSONObject();
		MODELResultSet modelJsonResponse = new MODELResultSet();

		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("branchId", UTILConstants.BRANCH_ID));

		modelJsonResponse = new ServerConnect().requestServer("Items.php", UTILConstants.HTTP_POST, params);
		jsonObj = modelJsonResponse.getJsonObject();

		if (modelJsonResponse.getError().equals("success")) {

			try {

				jsonObj = modelJsonResponse.getJsonObject();

				if (((JSONArray)jsonObj.get("data")).length() == 0 || jsonObj.get("data") == null) {
					modelMenuItems = new MODELMenuItems();
					listCategory.add(modelMenuItems);
				}else{
					JSONArray arrayOne = new JSONArray();	
					arrayOne = (JSONArray) jsonObj.get("data");
					JSONObject jsonObject2;
					for (int i = 0; i < arrayOne.length(); i++) {

						jsonObject2=(JSONObject) arrayOne.get(i);

						if (jsonObject2.getString("itemId") !=null && jsonObject2.getString("itemName") != null && jsonObject2.getString("categoryId") != null 
								&& jsonObject2.getString("comments") != null && jsonObject2.getString("priority") != null && jsonObject2.getString("recipe") != null && jsonObject2.getString("imageURL") != null
								&& jsonObject2.getString("kitchenId") != null && jsonObject2.getString("venueType") != null && jsonObject2.getString("itemType") != null
								&& jsonObject2.getString("priority") != null && jsonObject2.getString("itemDes") != null) {

							modelMenuItems = new MODELMenuItems();
							modelMenuItems.setId(Integer.parseInt(jsonObject2.getString("itemId")));
							modelMenuItems.setComments(jsonObject2.getString("comments"));
							modelMenuItems.setCatIdFk(jsonObject2.getString("categoryId"));
							modelMenuItems.setImageUrl(jsonObject2.getString("imageURL"));
							modelMenuItems.setItemName(jsonObject2.getString("itemName"));
							modelMenuItems.setKitchenIdFk(jsonObject2.getString("kitchenId"));
							modelMenuItems.setPriority(jsonObject2.getString("priority"));
							modelMenuItems.setTax(jsonObject2.getString("tax"));
							modelMenuItems.setVenueTypeIdFk(jsonObject2.getString("venueType"));
							modelMenuItems.setItemDes(jsonObject2.getString("itemDes"));

							//-------------------------------------PARSING RECIPE---------------------------------------------------
							MODELRecipe modelRecipe;
							List<MODELRecipe> listRecipe = new ArrayList<MODELRecipe>();
							JSONArray recipeArray = new JSONArray();
							recipeArray = (JSONArray)jsonObject2.get("recipe");

							for (int j = 0; j < recipeArray.length() ; j++) {

								JSONObject jsonObjectRecipe = new JSONObject();
								jsonObjectRecipe = (JSONObject) recipeArray.get(i);

								if (jsonObjectRecipe.get("id") != null && jsonObjectRecipe.get("quant") != null) {
									modelRecipe = new MODELRecipe(Integer.parseInt(jsonObjectRecipe.get("id").toString()),
											jsonObjectRecipe.get("quant").toString());

									listRecipe.add(modelRecipe);
								}
							}
							
							modelMenuItems.setRecipeList(listRecipe);//--------------adding the recipe list here
							
							//-----------------------------------PARSING SIZE TYPE-------------------------------------------------
							
							MODELSizeType modelSizeType;
							List<MODELSizeType> listSizeType = new ArrayList<MODELSizeType>();
							JSONArray sizeTypeArray = new JSONArray();
							sizeTypeArray = (JSONArray)jsonObject2.get("itemType");
							
							for (int j = 0; j < sizeTypeArray.length(); j++) {
								
								JSONObject jsonObjectSizeType = new JSONObject();
								jsonObjectSizeType = (JSONObject)sizeTypeArray.get(j);
								
								if (jsonObjectSizeType.get("name") != null && jsonObjectSizeType.get("price") != null) {
									modelSizeType = new MODELSizeType(jsonObjectSizeType.get("name").toString(),jsonObjectSizeType.get("price").toString());

									listSizeType.add(modelSizeType);
								}

							}
							
							modelMenuItems.setSizeTypeList(listSizeType);//--------------adding the size type list here

							listCategory.add(modelMenuItems);
						}else{
							modelJsonResponse.setError(UTILConstants.ERROR_MSG);
							modelJsonResponse.setMessage("DATA parsing prob");
						}

					}

				}

			}catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			modelJsonResponse.setList(listCategory);
		}else{

			String errorMsg = null;
			try {
				if (modelJsonResponse.getJsonObject() != null) {
					errorMsg = ""+modelJsonResponse.getJsonObject().get("data");	
				}else{
					errorMsg = "NULL VALUE";
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			modelJsonResponse.setError(UTILConstants.ERROR_MSG);
			modelJsonResponse.setMessage(""+errorMsg);

		}
		return modelJsonResponse;

	}
}

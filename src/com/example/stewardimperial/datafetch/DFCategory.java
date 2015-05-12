package com.example.stewardimperial.datafetch;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.stewardimperial.connectivity.ServerConnect;
import com.example.stewardimperial.models.MODELMainCategory;
import com.example.stewardimperial.models.MODELResultSet;
import com.example.stewardimperial.utils.UTILConstants;

public class DFCategory {

	public MODELResultSet retrieveCategories(){

		List<MODELMainCategory> listCategory = new ArrayList<MODELMainCategory>();
		MODELMainCategory modelCategory;
		JSONObject jsonObj = new JSONObject();
		MODELResultSet modelJsonResponse = new MODELResultSet();

		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("branchId", UTILConstants.BRANCH_ID));

		modelJsonResponse = new ServerConnect().requestServer("Category.php", UTILConstants.HTTP_POST, params);
		jsonObj = modelJsonResponse.getJsonObject();

		if (modelJsonResponse.getError().equals(UTILConstants.SUCCESS_MSG)) {

			try {

				jsonObj = modelJsonResponse.getJsonObject();

				if (((JSONArray)jsonObj.get("data")).length() == 0 || jsonObj.get("data") == null) {
					modelCategory = new MODELMainCategory();
					listCategory.add(modelCategory);
				}else{
					JSONArray arrayOne = new JSONArray();	
					arrayOne = (JSONArray) jsonObj.get("data");
					JSONObject jsonObject2;
					for (int i = 0; i < arrayOne.length(); i++) {

						jsonObject2=(JSONObject) arrayOne.get(i);

						if (jsonObject2.getString("categoryId") !=null && jsonObject2.getString("categoryName") != null) {
							modelCategory = new MODELMainCategory(jsonObject2.getString("categoryId"), jsonObject2.getString("categoryName"));			
							listCategory.add(modelCategory);
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

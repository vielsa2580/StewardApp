package com.example.stewardimperial.datafetch;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import com.example.stewardimperial.connectivity.ServerConnect;
import com.example.stewardimperial.models.MODELResultSet;
import com.example.stewardimperial.utils.UTILConstants;

public class DFSendToKitchen {

	public MODELResultSet retrieveCategories(){

		MODELResultSet modelJsonResponse = new MODELResultSet();

		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("branchId", UTILConstants.BRANCH_ID));
		
		JSONArray jsonAraay = new JSONArray(); //put ur arraylist here

		modelJsonResponse = new ServerConnect().requestServer("Category.php", UTILConstants.HTTP_POST, params);

		if (modelJsonResponse.getError().equals("success")) {
			
			
			
		}else{
			
			String errorMsg = null;
			try {
				errorMsg = ""+modelJsonResponse.getJsonObject().get("data");
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

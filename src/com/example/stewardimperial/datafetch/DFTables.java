package com.example.stewardimperial.datafetch;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.stewardimperial.connectivity.ServerConnect;
import com.example.stewardimperial.models.MODELResultSet;
import com.example.stewardimperial.models.MODELTable;
import com.example.stewardimperial.utils.UTILConstants;

public class DFTables {

	public MODELResultSet retrieveTables(){

		List<MODELTable> listTable = new ArrayList<MODELTable>();
		MODELTable modelTable;
		JSONObject jsonObj = new JSONObject();

		MODELResultSet modelJsonResponse = new MODELResultSet();

		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("branchId", UTILConstants.BRANCH_ID));

		modelJsonResponse = new ServerConnect().requestServer("Table.php", UTILConstants.HTTP_POST, params);

		if (modelJsonResponse != null) {

			if (modelJsonResponse.getError().equals("success")) {

				try {

					jsonObj = modelJsonResponse.getJsonObject();

					if (((JSONArray)jsonObj.get("data")).length() == 0 || jsonObj.get("data") == null) {
						modelTable = new MODELTable();
						listTable.add(modelTable);
					}else{
						JSONArray arrayOne = new JSONArray();	
						arrayOne = (JSONArray) jsonObj.get("data");
						JSONObject jsonObject2;
						for (int i = 0; i < arrayOne.length(); i++) {

							jsonObject2=(JSONObject) arrayOne.get(i);
							if (jsonObject2.getString("tableId") !=null && jsonObject2.getString("tableQuan") != null && jsonObject2.getString("tableName") != null) {
								modelTable = new MODELTable(Integer.parseInt(jsonObject2.getString("tableId")),Integer.parseInt(jsonObject2.getString("tableQuan")),jsonObject2.getString("tableName"));			
								listTable.add(modelTable);
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
				modelJsonResponse.setList(listTable);
			}else{

				String errorMsg = null;
				try {
					if (modelJsonResponse.getJsonObject() != null) {
						errorMsg = ""+modelJsonResponse.getJsonObject().get("data");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				modelJsonResponse.setError(UTILConstants.ERROR_MSG);
				modelJsonResponse.setMessage(""+errorMsg);

			}
		}else{
			modelJsonResponse.setError(UTILConstants.ERROR_MSG);
			modelJsonResponse.setMessage("Null Value");
		}
		return modelJsonResponse;

	}
}

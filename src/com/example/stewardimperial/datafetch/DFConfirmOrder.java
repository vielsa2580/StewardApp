package com.example.stewardimperial.datafetch;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;
import com.example.stewardimperial.connectivity.ServerConnect;
import com.example.stewardimperial.models.MODELResultSet;
import com.example.stewardimperial.utils.UTILConstants;

public class DFConfirmOrder {

	public MODELResultSet confirmOrder(String name){
		
		  List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
	        params.add(new BasicNameValuePair("data", name));
	        params.add(new BasicNameValuePair("branchId", UTILConstants.BRANCH_ID));
	        
	        MODELResultSet modelJsonResponse = new MODELResultSet();
	        modelJsonResponse = new ServerConnect().requestServer("StewardConfirmOrder.php", UTILConstants.HTTP_POST, params);
//	        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
		
		return modelJsonResponse;
	}
}

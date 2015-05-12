package com.example.stewardimperial.datafetch;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;

import com.example.stewardimperial.connectivity.ServerConnect;
import com.example.stewardimperial.models.MODELResultSet;
import com.example.stewardimperial.utils.UTILConstants;
import com.example.stewardimperial.utils.UTILSharedPreference;

public class DFOrderCooked {

	public MODELResultSet cookedOrder(Activity activity){
		
		  List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		  params.add(new BasicNameValuePair("branchId", UTILConstants.BRANCH_ID));
		  params.add(new BasicNameValuePair("stewardId", UTILSharedPreference.getPreference(activity, UTILConstants.USER_NAME)));
//	        params.add(new BasicNameValuePair("pass", password));
	        
	        MODELResultSet modelJsonResponse = new MODELResultSet();
	        modelJsonResponse = new ServerConnect().requestServer("StewardOrderComplete.php", UTILConstants.HTTP_POST, params);
//	        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
		
		return modelJsonResponse;
	}
}

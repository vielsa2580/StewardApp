package com.example.stewardimperial.datafetch;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;

import com.example.stewardimperial.connectivity.ServerConnect;
import com.example.stewardimperial.models.MODELResultSet;
import com.example.stewardimperial.utils.UTILConstants;
import com.example.stewardimperial.utils.UTILSharedPreference;

public class DFLogout {

	public MODELResultSet logout(Activity activity){
		
		  List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
	        params.add(new BasicNameValuePair("user_name", UTILSharedPreference.getPreference(activity, UTILConstants.USER_NAME)));
	        
	        MODELResultSet modelJsonResponse = new MODELResultSet();
	        modelJsonResponse = new ServerConnect().requestServer("StewardLogout.php", UTILConstants.HTTP_POST, params);
//	        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
		
		return modelJsonResponse;
	}
}

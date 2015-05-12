package com.example.stewardimperial.datafetch;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;
import com.example.stewardimperial.connectivity.ServerConnect;
import com.example.stewardimperial.models.MODELResultSet;
import com.example.stewardimperial.utils.UTILConstants;

public class DFLogin {

	public MODELResultSet login(String name, String password){
		
		  List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
	        params.add(new BasicNameValuePair("user_name", name));
	        params.add(new BasicNameValuePair("pass", password));
	        
	        MODELResultSet modelJsonResponse = new MODELResultSet();
	        modelJsonResponse = new ServerConnect().requestServer("StewardLogin.php", UTILConstants.HTTP_POST, params);
//	        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
		
		return modelJsonResponse;
	}
}
